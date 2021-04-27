package com.yuanqing.project.tiansu.service.assets.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yuanqing.common.enums.*;
import com.yuanqing.common.exception.CustomException;
import com.yuanqing.common.utils.DateUtils;
import com.yuanqing.common.utils.SequenceIdGenerator;
import com.yuanqing.common.utils.StringUtils;
import com.yuanqing.common.utils.ip.IpUtils;
import com.yuanqing.common.constant.IConstants;
import com.yuanqing.project.tiansu.domain.assets.*;
import com.yuanqing.project.tiansu.mapper.assets.CameraMapper;
import com.yuanqing.project.tiansu.mapper.assets.ClientTerminalMapper;
import com.yuanqing.project.tiansu.mapper.assets.ExternalDeviceMapper;
import com.yuanqing.project.tiansu.service.assets.IBusiCameraHistoryService;
import com.yuanqing.project.tiansu.service.assets.ICameraService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 摄像头实现类
 * Created by xucan on 2021-01-15 16:08
 * @author xucan
 */

@Service
public class CameraServiceImpl implements ICameraService {

    private static final Logger log = LoggerFactory.getLogger(CameraServiceImpl.class);

    @Autowired
    private CameraMapper cameraMapper;

    @Autowired
    private ClientTerminalMapper clientTerminalMapper;

    @Resource
    private ExternalDeviceMapper busiExternalDeviceMapper;

    @Resource
    private IBusiCameraHistoryService busiCameraHistoryService;


    private static SequenceIdGenerator cameraIdGenerator = new SequenceIdGenerator(1, 1);

    private static SequenceIdGenerator extDeviceIdGenerator = new SequenceIdGenerator(1, 1);

    @Override
    public List<Camera> getListWithOrder(Camera camera, String orderStr) {
        return cameraMapper.getListWithOrder(camera, orderStr);
    }

    @Override
    public List<Camera> getSessionCameraList(JSONObject filters) {
        List<Camera> list = cameraMapper.getSessionCameraList(filters);
        return list;
    }

    @Override
    public boolean findCamera(Camera camera) {
        List<Camera> list = getList(camera);
        return (list.size() == 0);
    }

    @Override
    public Map<Integer, Long> getCurrentStatus() {
        Camera cameraFilters = (Camera) DateUtils.getDayTime(Camera.class);
        List<Camera> list = cameraMapper.getList(cameraFilters);
        return list.stream().collect(Collectors.groupingBy(Camera::getStatus, Collectors.counting()));
    }

    @Override
    public List<Camera> getActiveCamera() {
        Camera camera = (Camera) DateUtils.getDayTime(Camera.class);
        return cameraMapper.getList(camera);
    }

    @Override
    public List<Camera> getNonNationalCameraList(Camera camera) {
            return cameraMapper.getList(camera);
    }

    @Override
    public boolean changStatus(String[] ids) {
        return cameraMapper.changStatus(ids);
    }

    @Override
    public boolean changAllStatus() {
        return cameraMapper.changAllStatus();
    }

    @Override
    public Map<String, Long> getNonNationalCamera() {
        List<JSONObject> list = cameraMapper.gourpByGb();
        HashMap<String, Long> map = new HashMap<>();
        for (JSONObject jsonObject : list ){
            if(jsonObject.getBoolean("type")){
                map.put("gb",jsonObject.getLongValue("count"));
            }else{
                map.put("ngb",jsonObject.getLongValue("count"));
            }
        }
        return map;
    }

    @Override
    public Long saveExternalDevice(ExternalDevice entity) {

        return null;
    }

    @Override
    public void dealCamera(ExternalDevice entity, List<Camera> addList, List<Camera> updateList, Map<String, Object> cameraCodeMap) {
        /*
         *在导入外部设备时，也要同时往历史表里面备份
         * 现根据私标作为key来查历史表
         * */
        String dstCode = entity.getDeviceId();


        // 导入时更新摄像头主表里的区域信息，ip和摄像头名称。
        String deviceId = StringUtils.replaceBlank(entity.getDeviceId());
        String gbId = StringUtils.replaceBlank(entity.getGbId());
        Camera cameraNGB = null;
        Camera cameraGB = null;
        //根据私标查找摄像头camera
        if (deviceId != null && !deviceId.equals("") && cameraCodeMap.containsKey(deviceId)) {
            cameraNGB = (Camera) cameraCodeMap.get(deviceId);
        }

        //根据国标查找摄像头camera1
        if (gbId != null && !gbId.equals("") && cameraCodeMap.containsKey(gbId)) {
            cameraGB = (Camera) cameraCodeMap.get(gbId);
        }

        //如果私标对应摄像头为空
        if (cameraNGB == null) {
            //如果根据国标查找摄像头存在
            //更新摄像头相关信息
            if (cameraGB != null) {
                cameraGB.setId(cameraGB.getId());
                // 获取摄像头编号的前六位  编号的长度为20
                if (gbId != null && !gbId.equals("")) {
                    cameraGB.setDeviceCode(gbId);
                    cameraGB.setIsGb(Integer.parseInt(GBEnum.GB.getValue()));
                } else {
                    cameraGB.setDeviceCode(entity.getDeviceId());
                    cameraGB.setIsGb(Integer.parseInt(GBEnum.NGB.getValue()));
                }
                if (gbId != null && !gbId.equals("") && gbId.length() == 20) {
                    try {
                        cameraGB.setRegion(Integer.parseInt(gbId.substring(0, 6)));
                    } catch (Exception e) {
                        cameraGB.setRegion(null);
                    }
                } else {
                    cameraGB.setRegion(null);
                }
                String manufacturer = entity.getManufacturer();
                if (manufacturer != null && !"".equals(manufacturer)) {
                    cameraGB.setManufacturer(manufacturer);
                }

                String deviceName = entity.getDeviceName();
                if (deviceName != null && !"".equals(deviceName)) {
                    cameraGB.setDeviceName(deviceName);
                }

                try {
                    cameraGB.setIpAddress(entity.getIpAddress());
                } catch (Exception e) {
                    cameraGB.setIpAddress(null);
                }

                cameraGB.setIsImport(ImportEnum.IMPORT_CAMERA.getValue());
                cameraGB.setIsProbe(ProbeEnum.IMPORT.getValue());
                updateList.add(cameraGB);
            }
            //私标对应摄像头存在，更新对应摄像头信息
        } else {
            cameraNGB.setId(cameraNGB.getId());
            // 获取摄像头编号的前六位  编号的长度为20
            if (gbId != null && !gbId.equals("")) {
                cameraNGB.setDeviceCode(gbId);
                cameraNGB.setIsGb(Integer.parseInt(GBEnum.GB.getValue()));
            } else {
                cameraNGB.setDeviceCode(entity.getDeviceId());
                cameraNGB.setIsGb(Integer.parseInt(GBEnum.NGB.getValue()));
            }
            if (gbId != null && !gbId.equals("") && gbId.length() == 20) {
                try {
//                    Region region1 = new Region();
//                    region1.setId(Long.parseLong(gbId.substring(0, 6)));// 行政区域
                    cameraNGB.setRegion(Integer.parseInt(gbId.substring(0, 6)));
                } catch (Exception e) {
                    cameraNGB.setRegion(null);
                }
            } else {
                cameraNGB.setRegion(null);
            }
            String manufacturer = entity.getManufacturer();
            if (manufacturer != null && !"".equals(manufacturer)) {
                cameraNGB.setManufacturer(manufacturer);
            }

            String deviceName = entity.getDeviceName();
            if (deviceName != null && !"".equals(deviceName)) {
                cameraNGB.setDeviceName(deviceName);
            }
            try {
                cameraNGB.setIpAddress(entity.getIpAddress());
            } catch (Exception e) {
                cameraNGB.setIpAddress(null);
            }
            cameraNGB.setIsImport(ImportEnum.IMPORT_CAMERA.getValue());
            cameraNGB.setIsProbe(ProbeEnum.IMPORT.getValue());
            updateList.add(cameraNGB);
        }
        //如果私标和国标对应摄像头都不存在
        if (cameraNGB == null && cameraGB == null) {
            try {
                cameraNGB = new Camera();
                cameraNGB.setId(cameraIdGenerator.nextId());
                cameraNGB.setDeviceName(entity.getDeviceName());
            } catch (Exception e) {
//                camera.setDeviceName("");
            }
            //设置地域：国标编码前六位
//                String gbId = this.replaceBlank(entity.getGbId());
            if (gbId != null && !gbId.equals("")) {
                cameraNGB.setDeviceCode(gbId);
                cameraNGB.setIsGb(Integer.valueOf(GBEnum.GB.getValue()));
            } else {
                cameraNGB.setDeviceCode(entity.getDeviceId());
                cameraNGB.setIsGb(Integer.valueOf(GBEnum.NGB.getValue()));
            }
            if (gbId != null && !gbId.equals("")) {
                try {
//                    Region region1 = new Region();
//                    region1.setId(Long.valueOf(gbId.substring(0, 6)));// 行政区域
                    cameraNGB.setRegion(Integer.parseInt(gbId.substring(0, 6)));
                } catch (Exception e) {
                    cameraNGB.setRegion(null);
                }
            } else {
                cameraNGB.setRegion(null);
            }
            try {
                cameraNGB.setIpAddress(entity.getIpAddress());
            } catch (Exception e) {
                cameraNGB.setIpAddress(null);
            }

            try {
                cameraNGB.setDeviceDomain(entity.getDomainId());
            } catch (Exception e) {
                cameraNGB.setDeviceDomain("");
            }

            try {
                if (entity.getLatitude() != null && !"".equals(entity.getLatitude())) {
                    cameraNGB.setLatitude(Double.valueOf(entity.getLatitude()));
                } else {
                    cameraNGB.setLatitude(null);
                }
            } catch (Exception e) {
                cameraNGB.setLatitude(null);
            }

            try {
                if (entity.getLongitude() != null && !"".equals(entity.getLongitude())) {
                    cameraNGB.setLongitude(Double.valueOf(entity.getLongitude()));
                } else {
                    cameraNGB.setLongitude(null);
                }
            } catch (Exception e) {
                cameraNGB.setLongitude(null);
            }

            try {
                cameraNGB.setManufacturer(entity.getManufacturer());
            } catch (Exception e) {
                cameraNGB.setManufacturer("");
            }
            try {
                cameraNGB.setCreateTime(entity.getCreateTime());
            } catch (Exception e) {
                cameraNGB.setCreateTime(null);
            }

            try {
                cameraNGB.setUpdateTime(entity.getUpdateTime());
            } catch (Exception e) {
                cameraNGB.setUpdateTime(null);
            }
            cameraNGB.setIsProbe(ProbeEnum.IMPORT.getValue());
            cameraNGB.setIsImport(ImportEnum.IMPORT_CAMERA.getValue());
//            count++;
            addList.add(cameraNGB);
        }
    }

    @Override
    public List<Camera> batchGetCameraById(List<Long> cameraIdList) {

        if(CollectionUtils.isEmpty(cameraIdList)){
            log.error("cameraIdList为空");
            return null;
        }

        List<Camera> cameraList = cameraMapper.batchGetCameraById(cameraIdList);

        return cameraList;
    }

    @Override
    public List<Camera> batchGetCameraByCode(List<String> cameraCodeList,Camera camera) {

        if(CollectionUtils.isEmpty(cameraCodeList)){
            log.error("cameraCodeList为空");
            return null;
        }

        List<Camera> cameraList = cameraMapper.batchGetCameraByCode(cameraCodeList,camera);

        return cameraList;
    }


    @Override
    public void updateIsNotServer(Long ipAddress) {
        cameraMapper.updateIsNotServer(ipAddress);
    }

    @Override
    public Long save(Camera camera) {
        Long ip = camera.getIpAddress();
        ClientTerminal clientTerminal = clientTerminalMapper.findByIpAddress(ip);
        if (camera.getId() == null) {

            if (GBEnum.NGB.equals(camera.getIsGb())) {
                if (clientTerminal != null) {
                    camera.setDeviceType(DeviceType.USER.getValue());
                }
            }
            cameraMapper.insert(camera);
        } else {
            //判断该设备是否是国标，如果为国标直接入库，为私标则根据服务器ip和终端ip过滤
            //如果服务器或者终端不为空，则将该摄像头类型设定为服务器
            if (GBEnum.NGB.equals(camera.getIsGb())) {
                if (clientTerminal != null) {
                    camera.setDeviceType(DeviceType.USER.getValue());
                }
            }
            cameraMapper.update(camera);
        }
        return camera.getId();
    }

    @Override
    public Long save(@Valid @NotNull(message = "保存或更新的实体不能为空") Camera entity, SaveType type) {
        throw new CustomException("暂不支持这种保存方式,无需SaveType");
    }

    @Override
    public void deleteById(Long id) {

        Camera camera = findById(id);
        if (camera == null) {
            throw new RuntimeException("entity not existed");
        }
        //删除数据库
        cameraMapper.delete(id);
//        //删除缓存
//        delCameraMap(camera);

    }

    @Override
    public Camera findById(Long id) {
        return cameraMapper.findById(id);
    }

    @Override
    public Camera findByCode(String code) {
        return cameraMapper.findByCode(code);
    }

    @Override
    public List<Camera> getList(Camera camera) {
        return cameraMapper.getList(camera);
    }

//    /**
//     * 删除CameraMap缓存
//     * @param camera 摄像头对象
//     */
//    private void delCameraMap(Camera camera){
//        //删除缓存中的数据
//        String cameraKey = String.format(Constants.TWO_FORMAT, Constants.CAMERA, camera.getDeviceCode());
//        CameraMap.remove(cameraKey);
//    }


    @Override
    public String readExtExcelFile(MultipartFile file) {
        String result = "";
        //创建处理EXCEL的类
        ReadExcel readExcel = new ReadExcel();
        // TODO: 感觉这个count只能知道是否入库成功。。。
        int count = 0;
        //解析excel，获取上传的事件单
        List<Map<String, Object>> extCameraList = readExcel.getExtCameraExcelInfo(file);
        //至此已经将excel中的数据转换到list里面了,接下来就可以操作list,可以进行保存到数据库,或者其他操作,

        //获得外部设备表的所有数据,并且new一个外部设备新增 list和一个外部设备更新list
        List<ExternalDevice> externalDeviceList = busiExternalDeviceMapper.getList(new ExternalDevice());
        List<ExternalDevice> addDeviceList = new ArrayList<>();
        List<ExternalDevice> updateDeviceList = new ArrayList<>();

        //根据国标编码为key,将数据put到HashMap1中;根据设备编码即私标编码为key,将数据put到HashMap2中
        Map<String, Object> deviceGbIdMap = new HashMap<>();
        Map<String, Object> deviceIdMap = new HashMap<>();
        for (ExternalDevice b : externalDeviceList) {
            deviceGbIdMap.put(b.getGbId(), b);
            deviceIdMap.put(b.getDeviceId(), b);
        }

        //获得摄像头表的所有数据,并且new一个摄像头新增 list和一个摄像头更新list
        List<Camera> cameraList = cameraMapper.getList(new Camera());
        List<Camera> addCameraList = new ArrayList<>();
        List<Camera> updateCameraList = new ArrayList<>();

        //根据devicecode为key,将数据put到HashMap中
        Map<String, Object> cameraCodeMap = new HashMap<>();
        for (Camera b : cameraList) {
            cameraCodeMap.put(b.getDeviceCode(), b);
        }
        for (Map<String, Object> extCamera : extCameraList) {
            ExternalDevice externalDevice = new ExternalDevice();
            try {
                externalDevice.setDeviceId(extCamera.get("deviceId").toString());
            } catch (Exception e) {
                externalDevice.setDeviceId("");
            }
            try {
                externalDevice.setDeviceName(extCamera.get("deviceName").toString());
            } catch (Exception e) {
                externalDevice.setDeviceName("");
            }

            try {
                externalDevice.setDomainId(extCamera.get("region").toString());
            } catch (Exception e) {
                externalDevice.setDomainId("");
            }

            try {
                externalDevice.setGbId(extCamera.get("gbId").toString());
            } catch (Exception e) {
                externalDevice.setGbId("");
            }

            try {
                externalDevice.setIpAddress(IpUtils.ipToLong(extCamera.get("ipAddress").toString()));
            } catch (Exception e) {
                externalDevice.setIpAddress(null);
            }

            try {
                if (extCamera.get("longitude") != null && !extCamera.get("longitude").equals("")) {
                    externalDevice.setLongitude((Double) extCamera.get("longitude"));
                } else {
                    externalDevice.setLongitude(null);
                }
            } catch (Exception e) {
                externalDevice.setLongitude(null);
            }

            try {
                if (extCamera.get("latitude") != null && !extCamera.get("latitude").equals("")) {
                    externalDevice.setLatitude((Double) extCamera.get("latitude"));
                } else {
                    externalDevice.setLatitude(null);
                }
            } catch (Exception e) {
                externalDevice.setLatitude(null);
            }

            try {
                externalDevice.setManufacturer(extCamera.get("manufacturer").toString());
            } catch (Exception e) {
                externalDevice.setManufacturer("");
            }

            ExternalDevice oldExternalDevice = null;

            // 根据编码在外部资源表中查询
            String gbId = externalDevice.getGbId().trim();
            String deviceId = externalDevice.getDeviceId().trim();
            if (StringUtils.isNotBlank(gbId) && deviceGbIdMap.containsKey(gbId)) {
//                oldExternalDevice = externalDeviceMapper.findByGbId(gbId);
                oldExternalDevice = (ExternalDevice) deviceGbIdMap.get(gbId);
            } else if (StringUtils.isNotBlank(deviceId) && deviceIdMap.containsKey(deviceId)) {
//                oldExternalDevice = externalDeviceMapper.findByDeviceId(deviceId);
                oldExternalDevice = (ExternalDevice) deviceIdMap.get(deviceId);
            }

            if (oldExternalDevice != null) {
                externalDevice.setId(oldExternalDevice.getId());
                updateDeviceList.add(externalDevice);
            } else {
                externalDevice.setId(extDeviceIdGenerator.nextId());
                addDeviceList.add(externalDevice);
            }

            this.dealCamera(externalDevice, addCameraList, updateCameraList, cameraCodeMap);
        }

        // 外部设备新增和更新批量入库;i=0表示新增，i=1表示更新
        if (addDeviceList.size() > 0) {
            this.deviceSave(addDeviceList, 0);
            count = 1;
        }
        if (updateDeviceList.size() > 0) {
            this.deviceSave(updateDeviceList, 1);
            count = 1;
        }

        //摄像头新增和更新批量入库;i=0表示新增，i=1表示更新
        if (addCameraList.size() > 0) {
            this.cameraSave(addCameraList, 0);
            count = 1;
            for (Camera entity : updateCameraList) {
                //摄像头导入时新增/更新都要处理缓存
                String cameraKey = String.format(IConstants.TWO_FORMAT, IConstants.CAMERA, entity.getDeviceCode());
//                redisCache.setCacheObject(cameraKey, entity, 2, TimeUnit.DAYS);
//                CameraMap.put(cameraKey, entity);
                BusiCameraHistory cameraHistory = new BusiCameraHistory();
                BeanUtils.copyProperties(entity, cameraHistory);
                handleCameraHistory(cameraHistory);
            }
        }
        if (updateCameraList.size() > 0) {
            this.cameraSave(updateCameraList, 1);
            count = 1;
            for (Camera entity : updateCameraList) {
                //摄像头导入时新增/更新都要处理缓存
                String cameraKey = String.format(IConstants.TWO_FORMAT, IConstants.CAMERA, entity.getDeviceCode());
//                redisCache.setCacheObject(cameraKey, entity, 2, TimeUnit.DAYS);
//                CameraMap.put(cameraKey, entity);
                BusiCameraHistory cameraHistory = new BusiCameraHistory();
                BeanUtils.copyProperties(entity, cameraHistory);
                handleCameraHistory(cameraHistory);
            }
        }
        if (count > 0) {
            result = "上传成功";
        } else {
            result = "上传失败";
        }
        return result;
    }

    private static int batchCount = 500;

    /**
     * 外部设备新增/更新入库
     *
     * @param externalDevices
     */
    private void deviceSave(List<ExternalDevice> externalDevices, int isUpdate) {
        if (isUpdate == 0) {
//            for (int i = 0; i <= externalDevices.size() / batchCount; i++) {
//                if (i == externalDevices.size() / batchCount) {
//                    if (externalDevices.subList(i * batchCount, externalDevices.size()).size() > 0) {
//                        busiExternalDeviceMapper.batchInsert(externalDevices.subList(i * batchCount, externalDevices.size()));
//                    }
//                } else {
//                    busiExternalDeviceMapper.batchInsert(externalDevices.subList(i * batchCount, (i + 1) * batchCount));
//                }
//            }
            for (int i = 0; i < externalDevices.size(); i++) {
                busiExternalDeviceMapper.insert(externalDevices.get(i));
            }
        } else if (isUpdate == 1) {
            for (int i = 0; i < externalDevices.size(); i++) {
                busiExternalDeviceMapper.update(externalDevices.get(i));
            }
        }
    }

    /**
     * 摄像头新增/更新入库
     *
     * @param cameras
     */
    private void cameraSave(List<Camera> cameras, int isUpdate) {
        if (isUpdate == 0) {
//            for (int i = 0; i <= cameras.size() / batchCount; i++) {
//                if (i == cameras.size() / batchCount) {
//                    if (cameras.subList(i * batchCount, cameras.size()).size() > 0) {
//                        cameraMapper.batchInsert(cameras.subList(i * batchCount, cameras.size()));
//                    }
//                } else {
//                    cameraMapper.batchInsert(cameras.subList(i * batchCount, (i + 1) * batchCount));
//                }
//            }

            for (int i = 0; i < cameras.size(); i++) {
                cameraMapper.insert(cameras.get(i));
            }
        } else {
            for (int i = 0; i < cameras.size(); i++) {
                cameraMapper.update(cameras.get(i));
            }
        }
    }

    /**
     * 处理摄像头历史：
     * 无同样设备编码就新增记录，
     * 有同样设备编码，有差别才更新。
     * @param cameraHistory
     */
    private void handleCameraHistory(BusiCameraHistory cameraHistory) {
        BusiCameraHistory oldCameraHistory = busiCameraHistoryService.selectBusiCameraHistoryByCode(cameraHistory.getDeviceCode());
        if (oldCameraHistory != null) {
            cameraHistory.setId(oldCameraHistory.getId());

            Long newIp = cameraHistory.getIpAddress();
            Long oldIp = oldCameraHistory.getIpAddress();
            String newName = cameraHistory.getDeviceName();
            String oldName = oldCameraHistory.getDeviceName();
            if (newIp != null && !newIp.equals(oldIp)) {
                // 新IP不为空，且和旧IP不相等
                busiCameraHistoryService.updateBusiCameraHistory(cameraHistory);
                return;
            }
            if (StringUtils.isNotBlank(newName) && !newName.equals(oldName)) {
                // 新名称不为空，且和旧名称不相等
                busiCameraHistoryService.updateBusiCameraHistory(cameraHistory);
                return;
            }
        } else {
            // 没有找到缓存，新增
            busiCameraHistoryService.insertBusiCameraHistory(cameraHistory);
        }
    }

    @Override
    public List<JSONObject> getAllToReport(JSONObject filters) {
        Camera condCamera = new Camera();
        condCamera.setDeviceCode(filters.getString("deviceCode"));
        condCamera.setDeviceName(filters.getString("deviceName"));
        condCamera.setIpAddress(IpUtils.ipToLong(filters.getString("ipAddress")));
        condCamera.setRegion(filters.getInteger("regionId"));
        condCamera.setStatus(filters.getInteger("status"));
        condCamera.setManufacturer(filters.getString("manufacturer"));
        List<Camera> cameraList = cameraMapper.getList(condCamera);

        List<JSONObject> reportList = new ArrayList<JSONObject>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        try {
            if(!CollectionUtils.isEmpty(cameraList)) {
                for (Camera camera : cameraList) {
                    JSONObject jsonObject = new JSONObject();
                    if (!StringUtils.isEmpty(camera.getDeviceName())) {
                        jsonObject.put("deviceName", camera.getDeviceName());
                    } else {
                        jsonObject.put("deviceName", "");
                    }
                    if (camera.getIpAddress() != null) {
                        jsonObject.put("ipAddress", IpUtils.longToIPv4(camera.getIpAddress()));
                    }
                    if (!StringUtils.isEmpty(camera.getRegionName())) {
                        jsonObject.put("name", camera.getRegionName());
                    } else {
                        jsonObject.put("name", "");
                    }
                    if (!StringUtils.isEmpty(camera.getDeviceCode())) {
                        jsonObject.put("deviceCode", camera.getDeviceCode());
                    }
                    if (!StringUtils.isEmpty(camera.getManufacturer())) {
                        jsonObject.put("manufacturer", camera.getManufacturer());
                    } else {
                        jsonObject.put("manufacturer", "");
                    }
                    if (camera.getStatus() != null && 0 == camera.getStatus()) {
                        jsonObject.put("status", "已确认");
                    } else if (camera.getStatus() == null || 1 == camera.getStatus()) {
                        jsonObject.put("status", "新发现");
                    }
                    if (camera.getUpdateTime() != null) {
                        jsonObject.put("updateTime", DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, camera.getUpdateTime()));
                    } else {
                        jsonObject.put("updateTime", "");
                    }

                    reportList.add(jsonObject);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return reportList;
    }

    @Override
    public List<Camera> findEventCameras(JSONObject filters) {
        List<Camera> list = cameraMapper.findEventCameras(filters);
        return list;
    }
}
