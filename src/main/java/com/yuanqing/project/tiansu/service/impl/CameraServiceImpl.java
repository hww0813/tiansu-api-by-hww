package com.yuanqing.project.tiansu.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.yuanqing.common.constant.Constants;
import com.yuanqing.common.enums.DeviceType;
import com.yuanqing.common.enums.GBEnum;
import com.yuanqing.common.queue.CameraMap;
import com.yuanqing.common.queue.ClientTerminalMap;
import com.yuanqing.common.utils.DateUtils;
import com.yuanqing.project.tiansu.domain.Camera;
import com.yuanqing.project.tiansu.domain.ClientTerminal;
import com.yuanqing.project.tiansu.domain.ExternalDevice;
import com.yuanqing.project.tiansu.mapper.CameraMapper;
import com.yuanqing.project.tiansu.mapper.ClientTerminalMapper;
import com.yuanqing.project.tiansu.service.ICameraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.multipart.MultipartFile;
import static com.yuanqing.common.constant.Constants.CAMERA_CACHE_NAME;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 摄像头实现类
 * Created by xucan on 2021-01-15 16:08
 * @author xucan
 */
public class CameraServiceImpl implements ICameraService {

    @Autowired
    private CameraMapper cameraMapper;

    @Autowired
    private ClientTerminalMapper clientTerminalMapper;

    @Override
    public List<Camera> findEventCameras(JSONObject filters) {
        List<Camera> list = cameraMapper.findEventCameras(filters);
        return list;
    }

    @Override
    @Cacheable(value = CAMERA_CACHE_NAME, key = "#deviceCode", unless = "#result == null")
    public Camera findOne(String deviceCode) {

        List<Camera> list = cameraMapper.findOne(deviceCode);

        return (list.size() > 0 ? list.get(0) : null) ;
    }

    @Override
    public boolean findCamera(Camera camera) {
        Camera camera1 = cameraMapper.findCamera(camera);
        return (camera1 == null);
    }

    @Override
    public Map<Integer, Long> getCurrentStatus() {
        JSONObject filters = DateUtils.getDayTime();
        List<Camera> list = cameraMapper.getList(filters);
        return list.stream().collect(Collectors.groupingBy(Camera::getStatus, Collectors.counting()));
    }

    @Override
    public List<Camera> getActiveCamera() {
        JSONObject filters = DateUtils.getDayTime();
        List<Camera> list = cameraMapper.getActiveCamera(filters);
        return list;
    }

    @Override
    public List<Camera> getNonNationalCameraList(JSONObject filter) {
            return cameraMapper.getList(filter);
    }

    @Override
    public List<Camera> findChild(JSONObject filters) {
        return cameraMapper.findChild(filters);
    }

    @Override
    public boolean changStatus(List<Camera> list) {
        return cameraMapper.changStatus(list);
    }

    @Override
    public boolean changAllStatus() {
        return cameraMapper.changAllStatus();
    }

    @Override
    public Map<String, Long> getNonNationalCamera() {
        Map<String, Long> group = new HashMap<>();
        JSONObject jsonObject = new JSONObject();
        List<Camera> gbList = cameraMapper.getGbList();
        List<Camera> ngbList = cameraMapper.getNgbList();
        group.put("gb", Long.parseLong(gbList.size() + ""));
        group.put("ngb", Long.parseLong(ngbList.size() + ""));
        return group;
    }

    @Override
    public List<JSONObject> getAllToReport(JSONObject filters) {
        return null;
    }

    @Override
    public String readExcelFile(MultipartFile file) {
        return null;
//        String result = "";
//        //创建处理EXCEL的类
//        ReadExcel readExcel = new ReadExcel();
//        int count = 0;
//        //解析excel，获取上传的事件单
//        List<Map<String, Object>> cameraList = readExcel.getExcelInfo(file);
//        //至此已经将excel中的数据转换到list里面了,接下来就可以操作list,可以进行保存到数据库,或者其他操作,
//        for (Map<String, Object> camera : cameraList) {
//            Camera camera1 = new Camera();
//            try {
//                camera1.setDeviceCode(camera.get("deviceCode").toString());
//            } catch (NullPointerException e) {
//                camera1.setDeviceCode("");
//            }
//            try {
//                camera1.setDeviceDomain(camera.get("deviceDomain").toString());
//            } catch (NullPointerException e) {
//                camera1.setDeviceDomain("");
//            }
//            try {
//                camera1.setDeviceName(camera.get("deviceName").toString());
//            } catch (NullPointerException e) {
//                camera1.setDeviceName("");
//            }
//            try {
//                camera1.setMacAddress(camera.get("macAddress").toString());
//            } catch (NullPointerException e) {
//                camera1.setMacAddress("");
//            }
//            try {
//                if (camera.get("longitude") != null && !camera.get("longitude").equals("")) {
//                    camera1.setLongitude(Double.valueOf(camera.get("longitude").toString()));
//                } else {
//                    camera1.setLongitude(null);
//                }
//            } catch (NullPointerException e) {
//                camera1.setLongitude(null);
//            }
//            try {
//                if (camera.get("latitude") != null && !camera.get("latitude").equals("")) {
//                    camera1.setLatitude(Double.valueOf(camera.get("latitude").toString()));
//                } else {
//                    camera1.setLatitude(null);
//                }
//            } catch (NullPointerException e) {
//                camera1.setLatitude(null);
//            }
//            try {
//                Region region1 = new Region();
//                region1.setId(Long.valueOf(camera.get("region").toString()));// 行政区域
//                camera1.setRegion(region1);
//            } catch (NullPointerException e) {
//                //获取摄像头编号的前六位  编号的长度为20
//                String code = camera.get("deviceCode").toString();
//                if (code != null && !code.equals("") && code.length() == 20) {
//                    Region region1 = new Region();
//                    region1.setId(Long.valueOf(camera.get("deviceCode").toString().substring(0, 6)));// 行政区域
//                    camera1.setRegion(region1);
//                } else {
//                    camera1.setRegion(null);
//                }
//            }
//
//            try {
//                camera1.setIpAddress(Long.valueOf(camera.get("ipAddress").toString()));
//            } catch (NullPointerException e) {
//                camera1.setIpAddress(null);
//            }
//            try {
//                String age = String.valueOf(camera.get("status").toString());
//                if (age.equals("已确认") || age.equals("0")) {
//                    camera1.setStatus(DeviceStatus.CONFIRM);
//                } else if (age.equals("新发现") || age.equals("1")) {
//                    camera1.setStatus(DeviceStatus.NEW);
//                } else if (age.equals("变更") || age.equals("2")) {
//                    camera1.setStatus(DeviceStatus.CHANGED);
//                } else if (age.equals("未授权") || age.equals("3")) {
//                    camera1.setStatus(DeviceStatus.UNAUTHORIZED);
//                } else {
//                    camera1.setStatus(DeviceStatus.CONFIRM);
//                }
//            } catch (NullPointerException e) {
//                camera1.setStatus(DeviceStatus.NEW);
//            }
//
//            try {
//                camera1.setDeviceType(camera.get("deviceType").toString());
//            } catch (NullPointerException e) {
//                camera1.setDeviceType("");
//            }
//            try {
//                camera1.setDomainIp(Long.valueOf(camera.get("domainIp").toString()));
//            } catch (NullPointerException e) {
//                camera1.setDomainIp(null);
//            }
//            try {
//                String gb = String.valueOf(camera.get("isGb").toString());
//                if (gb.equals("非国标") || gb.equals("0")) {
//                    // 是否国标
//                    camera1.setIsGb(GBEnum.NGB);
//                } else if (gb.equals("国标") || gb.equals("1")) {
//                    // 是否国标
//                    camera1.setIsGb(GBEnum.GB);
//                } else {
//                    // 是否国标
//                    camera1.setIsGb(null);
//                }
//            } catch (NullPointerException e) {
//                // 是否国标
//                camera1.setIsGb(null);
//            }
//            try {
//                if (camera.get("domainPort") != null && !camera.get("domainPort").equals("")) {
//                    camera1.setDomainPort(Long.valueOf(camera.get("domainPort").toString()));
//                } else {
//                    camera1.setDomainPort(null);
//                }
//            } catch (NullPointerException e) {
//                camera1.setDomainPort(null);
//            }
//            boolean flag = this.findCamera(camera1);
//
//            if (flag) {
//                try {
//                    Long id = this.save(camera1);
//                    if (id > 0) {
//                        count++;
//                        result = "上传成功";
//                    } else {
//                        result = "上传失败";
//                    }
//                } catch (Exception e) {
//                    result = "上传失败";
//                }
//            } else {
//                result = "该摄像头已经存在";
//            }
//             /*int ret=cameraMapper.insertCamera(camera1);
//             if(ret == 0){
//                 result = "插入数据库失败";
//             }*/
//        }
////        if (cameraList != null && !cameraList.isEmpty()) {
//        if (count > 0) {
//            result = "上传成功";
//        } else {
//            result = "上传失败";
//        }
//        return result;
    }

    @Override
    public List<Camera> getSessionCameraList(JSONObject filters) {
        return cameraMapper.getSessionCameraList(filters);
    }

    @Override
    public List<Camera> visitedPage(JSONObject filters) {
        return cameraMapper.getAllVisited(filters);
    }

    @Override
    public String readExtExcelFile(MultipartFile file) {
        return null;
    }

    @Override
    public Long saveExternalDevice(ExternalDevice entity) {
//        if (entity.getId() == null) {
//            externalDeviceMapper.insert(entity);
//            //缓存中新增数据
//            String externalDeviceKey = null;
//            if (StringUtils.isNotBlank(entity.getGbId())) {
//                externalDeviceKey = String.format(IConstants.TWO_FORMAT, IConstants.EXTERNAL_DEVICE, entity.getGbId());
////                redisCache.setCacheObject(externalDeviceKey, entity, 2, TimeUnit.DAYS);
//                ExternalDeviceMap.put(externalDeviceKey,entity);
//            }
//            if (StringUtils.isNotBlank(entity.getDeviceId())) {
//                externalDeviceKey = String.format(IConstants.TWO_FORMAT, IConstants.EXTERNAL_DEVICE, entity.getDeviceId());
////                redisCache.setCacheObject(externalDeviceKey, entity, 2, TimeUnit.DAYS);
//                ExternalDeviceMap.put(externalDeviceKey,entity);
//            }
//        } else {
//            externalDeviceMapper.update(entity);
//            //缓存中更新数据
//            String externalDeviceKey = null;
//            if (StringUtils.isNotBlank(entity.getGbId())) {
//                externalDeviceKey = String.format(IConstants.TWO_FORMAT, IConstants.EXTERNAL_DEVICE, entity.getGbId());
////                redisCache.setCacheObject(externalDeviceKey, entity, 2, TimeUnit.DAYS);
//                ExternalDeviceMap.put(externalDeviceKey,entity);
//            }
//            if (StringUtils.isNotBlank(entity.getDeviceId())) {
//                externalDeviceKey = String.format(IConstants.TWO_FORMAT, IConstants.EXTERNAL_DEVICE, entity.getDeviceId());
////                redisCache.setCacheObject(externalDeviceKey, entity, 2, TimeUnit.DAYS);
//                ExternalDeviceMap.put(externalDeviceKey,entity);
//            }
//        }
////        Long regionId = 0l;
//        /*
//         *在导入外部设备时，也要同时往历史表里面备份
//         * 现根据私标作为key来查历史表
//         * */
//        String dstCode = entity.getDeviceId();
//        Long dstDeviceIp = IPv4Util.ipToLong(this.replaceBlank(entity.getIpAddress()));
//        // 导入时更新摄像头主表里的区域信息，ip和摄像头名称。
//        JSONObject filter = new JSONObject();
//        String deviceId = this.replaceBlank(entity.getDeviceId());
//        String gbId = this.replaceBlank(entity.getGbId());
//        Camera camera = null;
//        Camera camera1 = null;
//        //根据私标查找摄像头camera
//        if (deviceId != null && !deviceId.equals("")) {
//            filter.put("deviceCode", deviceId);
//            camera = cameraMapper.findCamera(filter);
//        }
//
//        //根据国标查找摄像头camera1
//        if (gbId != null && !gbId.equals("")) {
//            filter.put("deviceCode", gbId);
//            camera1 = cameraMapper.findCamera(filter);
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
//                    Region region1 = new Region();
//                    region1.setId(Long.parseLong(gbId.substring(0, 6)));// 行政区域
//                    camera1.setRegion(region1);
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
//                    camera1.setIpAddress(IpUtils.IPv4ToLong(ip));
//                }
//                camera1.setIsImport(ImportEnum.IMPORT_CAMERA.getValue());
//                camera.setIsProbe(ProbeEnum.IMPORT.getValue());
//                cameraMapper.update(camera1);
//            }
//            //私标对应摄像头存在，更新对应摄像头信息
//        } else {
////            Camera camera = new Camera();
//            camera.setId(camera.getId());
//            // 获取摄像头编号的前六位  编号的长度为20
////            String gbId = this.replaceBlank(entity.getGbId());
////            System.out.println(")))))))))))))gbID="+gbId+"$$$$$$$$$$$$$$$$$$$$$$$$$");
//            if (gbId != null && !gbId.equals("")) {
//                System.out.println("^^^^^^^^^^^^^^^^^gbID=" + gbId + "************************");
//                camera.setDeviceCode(gbId);
//                camera.setIsGb(GBEnum.GB);
//            } else {
//                camera.setDeviceCode(entity.getDeviceId());
//                camera.setIsGb(GBEnum.NGB);
//            }
//            if (gbId != null && !gbId.equals("") && gbId.length() == 20) {
//                Region region1 = new Region();
//                region1.setId(Long.parseLong(gbId.substring(0, 6)));// 行政区域
//                camera.setRegion(region1);
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
//                camera.setIpAddress(IpUtils.IPv4ToLong(ip));
//            }
//            camera.setIsImport(ImportEnum.IMPORT_CAMERA.getValue());
//            camera.setIsProbe(ProbeEnum.IMPORT.getValue());
//            cameraMapper.update(camera);
//        }
//        //如果私标和国标对应摄像头都不存在
//        if (camera == null && camera1 == null) {
//            try {
//                camera = new Camera();
//                camera.setDeviceName(entity.getDeviceName());
//            } catch (NullPointerException e) {
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
//                Region region1 = new Region();
//                region1.setId(Long.valueOf(gbId.substring(0, 6)));// 行政区域
//                camera.setRegion(region1);
//            } else {
//                camera.setRegion(null);
//            }
//            try {
//                camera.setIpAddress(IpUtils.IPv4ToLong(this.replaceBlank(entity.getIpAddress())));
//            } catch (NullPointerException e) {
//                camera.setIpAddress(null);
//            }
//
//            try {
//                camera.setDeviceDomain(entity.getDomainId());
//            } catch (NullPointerException e) {
//                camera.setDeviceDomain("");
//            }
//
//            try {
//                if (entity.getLatitude() != null && !"".equals(entity.getLatitude())) {
//                    camera.setLatitude(Double.valueOf(entity.getLatitude()));
//                } else {
//                    camera.setLatitude(null);
//                }
//            } catch (NullPointerException e) {
//                camera.setLatitude(null);
//            }
//
//            try {
//                if (entity.getLongitude() != null && !"".equals(entity.getLongitude())) {
//                    camera.setLongitude(Double.valueOf(entity.getLongitude()));
//                } else {
//                    camera.setLongitude(null);
//                }
//            } catch (NullPointerException e) {
//                camera.setLongitude(null);
//            }
//
//            try {
//                camera.setManufacturer(entity.getManufacturer());
//            } catch (NullPointerException e) {
//                camera.setManufacturer("");
//            }
//            try {
//                camera.setCreateTime(entity.getCreateTime());
//            } catch (NullPointerException e) {
//                camera.setCreateTime(null);
//            }
//
//            try {
//                camera.setUpdateTime(entity.getUpdateTime());
//            } catch (NullPointerException e) {
//                camera.setUpdateTime(null);
//            }
//            camera.setIsProbe(ProbeEnum.IMPORT.getValue());
//            camera.setIsImport(ImportEnum.IMPORT_CAMERA.getValue());
//            count++;
//            save(camera);
//            System.out.println(camera.getId());
//
//        }
//        return entity.getId();
        return null;
    }

    @Override
    public void dealCamera(ExternalDevice entity, List<Camera> addList, List<Camera> updateList, Map<String, Object> cameraCodeMap) {
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
    public void batchInsert(List<Camera> list) {
        if (list == null) {
            throw new RuntimeException("list can't be null");
        }
        cameraMapper.batchInsert(list);
        //新增数据放入缓存中CameraMap
        batchPutCameraMap(list);
    }

    @Override
    public void batchUpdate(List<Camera> list) {
        if (list == null) {
            throw new RuntimeException("list can't be null");
        }
        cameraMapper.batchUpdate(list);
        //更新缓存中的数据
        batchPutCameraMap(list);
    }

    @Override
    public Long findId() {
        return cameraMapper.findId();
    }

    @Override
    public void insert(Camera camera) {
        //判断摄像头
        isGbForCamera(camera);
        //入库
        cameraMapper.insertCamera(camera);
        //存入缓存
        putCameraMap(camera);
    }

    @Override
    public void update(Camera camera) {
        //判断摄像头
        isGbForCamera(camera);
        //更新数据库
        cameraMapper.update(camera);
        //更新缓存
        putCameraMap(camera);
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
    public List<Camera> getList(JSONObject filters) {
        return cameraMapper.getList(filters);
    }


    /**
     * 批量存入CameraMap缓存
     * @param list
     */
    private void batchPutCameraMap(List<Camera> list){
        for (Camera camera : list) {
            putCameraMap(camera);
        }
    }

    /**
     * 单条camera 存入CameraMap缓存
     * @param camera
     */
    private void putCameraMap(Camera camera){
        String cameraKey = String.format(Constants.TWO_FORMAT, Constants.CAMERA, camera.getDeviceCode());
        CameraMap.put(cameraKey,camera);
    }

    /**
     * 删除CameraMap缓存
     * @param camera
     */
    private void delCameraMap(Camera camera){
        //删除缓存中的数据
        String cameraKey = String.format(Constants.TWO_FORMAT, Constants.CAMERA, camera.getDeviceCode());
        CameraMap.remove(cameraKey);
    }

    /**
     * 判断该设备是否是国标，如果为国标直接入库，为私标则根据服务器ip和终端ip过滤
     * 如果服务器或者终端不为空，则将该摄像头类型设定为服务器
     * @param camera
     * @return
     */
    private void isGbForCamera(Camera camera){

        if (GBEnum.NGB.equals(camera.getIsGb())) {
            //先查缓存
            String clientTerminalKey = String.format(Constants.TWO_FORMAT, Constants.CLIENT_TERMINAL, camera.getIpAddress());
            ClientTerminal clientTerminal = ClientTerminalMap.get(clientTerminalKey);
            //缓存中没有，再查数据库
            if (clientTerminal == null) {
                clientTerminal = clientTerminalMapper.findByIpAddress(camera.getIpAddress());
            }
            if (clientTerminal != null) {
                camera.setDeviceType(DeviceType.USER.getValue());
            }
        }
    }
}
