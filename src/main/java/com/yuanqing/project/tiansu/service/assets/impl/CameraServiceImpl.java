package com.yuanqing.project.tiansu.service.assets.impl;

import com.alibaba.fastjson.JSONObject;
import com.yuanqing.common.constant.Constants;
import com.yuanqing.common.enums.DeviceType;
import com.yuanqing.common.enums.GBEnum;
import com.yuanqing.common.enums.SaveType;
import com.yuanqing.common.exception.CustomException;
import com.yuanqing.common.queue.CameraMap;
import com.yuanqing.common.queue.ClientTerminalMap;
import com.yuanqing.common.utils.DateUtils;
import com.yuanqing.common.utils.StringUtils;
import com.yuanqing.project.tiansu.domain.IConstants;
import com.yuanqing.project.tiansu.domain.assets.*;
import com.yuanqing.project.tiansu.mapper.assets.BusiExternalDeviceMapper;
import com.yuanqing.project.tiansu.mapper.assets.CameraMapper;
import com.yuanqing.project.tiansu.mapper.assets.ClientTerminalMapper;
import com.yuanqing.project.tiansu.service.assets.ICameraService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import static com.yuanqing.common.constant.Constants.CAMERA_CACHE_NAME;

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

    @Autowired
    private CameraMapper cameraMapper;

    @Autowired
    private ClientTerminalMapper clientTerminalMapper;

    @Resource
    private BusiExternalDeviceMapper busiExternalDeviceMapper;

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
    public Long saveExternalDevice(BusiExternalDevice entity) {

        return null;
    }

    @Override
    public void dealCamera(BusiExternalDevice entity, List<Camera> addList, List<Camera> updateList, Map<String, Object> cameraCodeMap) {
        /*
         *在导入外部设备时，也要同时往历史表里面备份
         * 现根据私标作为key来查历史表
         * */
//        String dstCode = entity.getDeviceId();
////        try {
////            Long dstDeviceIp = IPv4Util.ipToLong(this.replaceBlank(entity.getIpAddress()));
////        }catch (Exception e) {
////            System.out.println("------------------------88888888888888888888888-------------------------");
////            Long dstDeviceIp = null;
////        }
//
//        // 导入时更新摄像头主表里的区域信息，ip和摄像头名称。
//        String deviceId = this.replaceBlank(entity.getDeviceId());
//        String gbId = this.replaceBlank(entity.getGbId());
//        Camera camera = null;
//        Camera camera1 = null;
//        //根据私标查找摄像头camera
//        if (deviceId != null && !deviceId.equals("") && cameraCodeMap.containsKey(deviceId)) {
//            camera = (Camera) cameraCodeMap.get(deviceId);
//        }
//
//        //根据国标查找摄像头camera1
//        if (gbId != null && !gbId.equals("") && cameraCodeMap.containsKey(gbId)) {
//            camera1 = (Camera) cameraCodeMap.get(gbId);
//        }
//
//        //如果私标对应摄像头为空
//        if (camera == null) {
//            //如果根据国标查找摄像头存在
//            //更新摄像头相关信息
//            if (camera1 != null) {
//                camera1.setId(camera1.getId());
//                // 获取摄像头编号的前六位  编号的长度为20
//                if (gbId != null && !gbId.equals("")) {
//                    camera1.setDeviceCode(gbId);
//                    camera1.setIsGb(GBEnum.GB);
//                } else {
//                    camera1.setDeviceCode(entity.getDeviceId());
//                    camera1.setIsGb(GBEnum.NGB);
//                }
//                if (gbId != null && !gbId.equals("") && gbId.length() == 20) {
//                    try {
//                        Region region1 = new Region();
//                        region1.setId(Long.parseLong(gbId.substring(0, 6)));// 行政区域
//                        camera1.setRegion(region1);
//                    } catch (Exception e) {
//                        camera1.setRegion(null);
//                    }
//                } else {
//                    camera1.setRegion(null);
//                }
//                String manufacturer = entity.getManufacturer();
//                if (manufacturer != null && !"".equals(manufacturer)) {
//                    camera1.setManufacturer(manufacturer);
//                }
//
//                String deviceName = entity.getDeviceName();
//                if (deviceName != null && !"".equals(deviceName)) {
//                    camera1.setDeviceName(deviceName);
//                }
//                String ip = this.replaceBlank(entity.getIpAddress());
//                if (ip != null && !"".equals(ip)) {
//                    try {
//                        camera1.setIpAddress(IpUtils.IPv4ToLong(ip));
//                    } catch (Exception e) {
//                        camera1.setIpAddress(null);
//                    }
//                }
//                camera1.setIsImport(ImportEnum.IMPORT_CAMERA.getValue());
//                camera1.setIsProbe(ProbeEnum.IMPORT.getValue());
//                updateList.add(camera1);
//            }
//            //私标对应摄像头存在，更新对应摄像头信息
//        } else {
//            camera.setId(camera.getId());
//            // 获取摄像头编号的前六位  编号的长度为20
//            if (gbId != null && !gbId.equals("")) {
//                camera.setDeviceCode(gbId);
//                camera.setIsGb(GBEnum.GB);
//            } else {
//                camera.setDeviceCode(entity.getDeviceId());
//                camera.setIsGb(GBEnum.NGB);
//            }
//            if (gbId != null && !gbId.equals("") && gbId.length() == 20) {
//                try {
//                    Region region1 = new Region();
//                    region1.setId(Long.parseLong(gbId.substring(0, 6)));// 行政区域
//                    camera.setRegion(region1);
//                } catch (Exception e) {
//                    camera.setRegion(null);
//                }
//            } else {
//                camera.setRegion(null);
//            }
//            String manufacturer = entity.getManufacturer();
//            if (manufacturer != null && !"".equals(manufacturer)) {
//                camera.setManufacturer(manufacturer);
//            }
//
//            String deviceName = entity.getDeviceName();
//            if (deviceName != null && !"".equals(deviceName)) {
//                camera.setDeviceName(deviceName);
//            }
//            String ip = this.replaceBlank(entity.getIpAddress());
//            if (ip != null && !"".equals(ip)) {
//                try {
//                    camera.setIpAddress(IpUtils.IPv4ToLong(ip));
//                } catch (Exception e) {
//                    camera.setIpAddress(null);
//                }
//            }
//            camera.setIsImport(ImportEnum.IMPORT_CAMERA.getValue());
//            camera.setIsProbe(ProbeEnum.IMPORT.getValue());
//            updateList.add(camera);
//        }
//        //如果私标和国标对应摄像头都不存在
//        if (camera == null && camera1 == null) {
//            try {
//                camera = new Camera();
//                camera.setDeviceName(entity.getDeviceName());
//            } catch (Exception e) {
////                camera.setDeviceName("");
//            }
//            //设置地域：国标编码前六位
////                String gbId = this.replaceBlank(entity.getGbId());
//            if (gbId != null && !gbId.equals("")) {
//                camera.setDeviceCode(gbId);
//                camera.setIsGb(GBEnum.GB);
//            } else {
//                camera.setDeviceCode(entity.getDeviceId());
//                camera.setIsGb(GBEnum.NGB);
//            }
//            if (gbId != null && !gbId.equals("")) {
//                try {
//                    Region region1 = new Region();
//                    region1.setId(Long.valueOf(gbId.substring(0, 6)));// 行政区域
//                    camera.setRegion(region1);
//                } catch (Exception e) {
//                    camera.setRegion(null);
//                }
//            } else {
//                camera.setRegion(null);
//            }
//            try {
//                camera.setIpAddress(IpUtils.IPv4ToLong(this.replaceBlank(entity.getIpAddress())));
//            } catch (Exception e) {
//                camera.setIpAddress(null);
//            }
//
//            try {
//                camera.setDeviceDomain(entity.getDomainId());
//            } catch (Exception e) {
//                camera.setDeviceDomain("");
//            }
//
//            try {
//                if (entity.getLatitude() != null && !"".equals(entity.getLatitude())) {
//                    camera.setLatitude(Double.valueOf(entity.getLatitude()));
//                } else {
//                    camera.setLatitude(null);
//                }
//            } catch (Exception e) {
//                camera.setLatitude(null);
//            }
//
//            try {
//                if (entity.getLongitude() != null && !"".equals(entity.getLongitude())) {
//                    camera.setLongitude(Double.valueOf(entity.getLongitude()));
//                } else {
//                    camera.setLongitude(null);
//                }
//            } catch (Exception e) {
//                camera.setLongitude(null);
//            }
//
//            try {
//                camera.setManufacturer(entity.getManufacturer());
//            } catch (Exception e) {
//                camera.setManufacturer("");
//            }
//            try {
//                camera.setCreateTime(entity.getCreateTime());
//            } catch (Exception e) {
//                camera.setCreateTime(null);
//            }
//
//            try {
//                camera.setUpdateTime(entity.getUpdateTime());
//            } catch (Exception e) {
//                camera.setUpdateTime(null);
//            }
//            camera.setIsProbe(ProbeEnum.IMPORT.getValue());
//            camera.setIsImport(ImportEnum.IMPORT_CAMERA.getValue());
//            count++;
//            addList.add(camera);
//        }
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
        //删除缓存
        delCameraMap(camera);

    }

    @Override
    public Camera findById(Long id) {
        return cameraMapper.findById(id);
    }

    @Override
    public List<Camera> getList(Camera camera) {
        return cameraMapper.getList(camera);
    }

    /**
     * 删除CameraMap缓存
     * @param camera 摄像头对象
     */
    private void delCameraMap(Camera camera){
        //删除缓存中的数据
        String cameraKey = String.format(Constants.TWO_FORMAT, Constants.CAMERA, camera.getDeviceCode());
        CameraMap.remove(cameraKey);
    }


    @Override
    public String readExtExcelFile(MultipartFile file) {
        String result = "";
        //创建处理EXCEL的类
        ReadExcel readExcel = new ReadExcel();
        int count = 0;
        //解析excel，获取上传的事件单
        List<Map<String, Object>> extCameraList = readExcel.getExtCameraExcelInfo(file);
        //至此已经将excel中的数据转换到list里面了,接下来就可以操作list,可以进行保存到数据库,或者其他操作,

        //获得外部设备表的所有数据,并且new一个外部设备新增 list和一个外部设备更新list
        List<BusiExternalDevice> externalDeviceList = busiExternalDeviceMapper.selectBusiExternalDeviceList(new BusiExternalDevice());
        List<BusiExternalDevice> addDeviceList = new ArrayList<>();
        List<BusiExternalDevice> updateDeviceList = new ArrayList<>();

        //根据国标编码为key,将数据put到HashMap1中;根据设备编码即私标编码为key,将数据put到HashMap2中
        Map<String, Object> deviceGbIdMap = new HashMap<>();
        Map<String, Object> deviceIdMap = new HashMap<>();
        for (BusiExternalDevice b : externalDeviceList) {
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
            BusiExternalDevice externalDevice = new BusiExternalDevice();
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
                externalDevice.setIpAddress(extCamera.get("ipAddress").toString());
            } catch (Exception e) {
                externalDevice.setIpAddress(null);
            }

            try {
                if (extCamera.get("longitude") != null && !extCamera.get("longitude").equals("")) {
                    externalDevice.setLongitude(extCamera.get("longitude").toString());
                } else {
                    externalDevice.setLongitude(null);
                }
            } catch (Exception e) {
                externalDevice.setLongitude(null);
            }

            try {
                if (extCamera.get("latitude") != null && !extCamera.get("latitude").equals("")) {
                    externalDevice.setLatitude(extCamera.get("latitude").toString());
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

            BusiExternalDevice oldExternalDevice = null;

            // 根据编码在外部资源表中查询
            String gbId = externalDevice.getGbId().trim();
            String deviceId = externalDevice.getDeviceId().trim();
            if (StringUtils.isNotBlank(gbId) && deviceGbIdMap.containsKey(gbId)) {
//                oldExternalDevice = externalDeviceMapper.findByGbId(gbId);
                oldExternalDevice = (BusiExternalDevice) deviceGbIdMap.get(gbId);
            } else if (StringUtils.isNotBlank(deviceId) && deviceIdMap.containsKey(deviceId)) {
//                oldExternalDevice = externalDeviceMapper.findByDeviceId(deviceId);
                oldExternalDevice = (BusiExternalDevice) deviceIdMap.get(deviceId);
            }

            if (oldExternalDevice != null) {
                externalDevice.setId(oldExternalDevice.getId());
                updateDeviceList.add(externalDevice);
            } else {
                addDeviceList.add(externalDevice);
            }

            this.dealCamera(externalDevice, addCameraList, updateCameraList, cameraCodeMap);
        }
        //外部设备新增和更新批量入库;i=0表示新增，i=1表示更新
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
//            for(Camera entity:addCameraList){
//                //摄像头导入时新增/更新都要处理缓存
//                String cameraKey = String.format(IConstants.TWO_FORMAT, IConstants.CAMERA, entity.getDeviceCode());
////                redisCache.setCacheObject(cameraKey, entity, 2, TimeUnit.DAYS);
//                CameraMap.put(cameraKey,entity);
//                CameraHistory cameraHistory = new CameraHistory();
//                BeanUtils.copyProperties(entity, cameraHistory);
//                //传到摄像头历史处理队列
//                CameraHistoryCollection.push(cameraHistory);
//            }
        }
        if (updateCameraList.size() > 0) {
            this.cameraSave(updateCameraList, 1);
            count = 1;
//            for(Camera entity:updateCameraList){
//                //摄像头导入时新增/更新都要处理缓存
//                String cameraKey = String.format(IConstants.TWO_FORMAT, IConstants.CAMERA, entity.getDeviceCode());
////                redisCache.setCacheObject(cameraKey, entity, 2, TimeUnit.DAYS);
//                CameraMap.put(cameraKey,entity);
//                CameraHistory cameraHistory = new CameraHistory();
//                BeanUtils.copyProperties(entity, cameraHistory);
//                //传到摄像头历史处理队列
//                CameraHistoryCollection.push(cameraHistory);
//            }
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
     * @param device
     */
    private void deviceSave(List<BusiExternalDevice> device, int j) {
        for (int i = 0; i <= device.size() / batchCount; i++) {
            if (i == device.size() / batchCount) {
                if (device.subList(i * batchCount, device.size()).size() > 0) {
                    if (j == 0) {
                        busiExternalDeviceMapper.batchInsert(device.subList(i * batchCount, device.size()));
                    } else if (j == 1) {
                        busiExternalDeviceMapper.batchUpdate(device.subList(i * batchCount, device.size()));
                    }
                }
            } else {
                if (j == 0) {
                    busiExternalDeviceMapper.batchInsert(device.subList(i * batchCount, (i + 1) * batchCount));
                } else if (j == 1) {
                    busiExternalDeviceMapper.batchUpdate(device.subList(i * batchCount, (i + 1) * batchCount));
                }
            }
        }
    }

    /**
     * 摄像头新增/更新入库
     *
     * @param camera
     */
    private void cameraSave(List<Camera> camera, int j) {
        for (int i = 0; i <= camera.size() / batchCount; i++) {
            if (i == camera.size() / batchCount) {
                if (camera.subList(i * batchCount, camera.size()).size() > 0) {
                    if (j == 0) {
                        cameraMapper.batchInsert(camera.subList(i * batchCount, camera.size()));
                    } else if (j == 1) {
                        cameraMapper.batchUpdate(camera.subList(i * batchCount, camera.size()));
                    }
                }
            } else {
                if (j == 0) {
                    cameraMapper.batchInsert(camera.subList(i * batchCount, (i + 1) * batchCount));
                } else if (j == 1) {
                    cameraMapper.batchUpdate(camera.subList(i * batchCount, (i + 1) * batchCount));
                }
            }
        }
    }
}
