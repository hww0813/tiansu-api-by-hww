//package com.yuanqing.project.tiansu.controller;
//
//import com.alibaba.fastjson.JSONObject;
//
//import com.yuanqing.framework.web.controller.BaseController;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.BeanUtils;
//import org.springframework.format.annotation.DateTimeFormat;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.annotation.Resource;
//import javax.validation.Valid;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * 摄像头Controller
// * @author xucan
// */
//@RestController
//@RequestMapping(value = "/api/device/sip-device")
//@CrossOrigin
//@Api(value = "摄像头", description = "摄像头相关Api")
//public class CameraController extends BaseController {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(CameraController.class);
//
//    @Resource
//    private CameraManager cameraManager;
//    @Resource
//    private ServerTreeManager serverTreeManager;
//    @Resource
//    private HomePageManager homePageManager;
//
//    //导入excel
////    @PostMapping(value = "/import")
////    @ApiOperation(value = "导入摄像头列表")
////    public Map<String, Object> importExcel(@RequestParam(value = "file", required = false) MultipartFile file) {
////        Map<String, Object> map = new HashMap<String, Object>();
////        String result = cameraManager.readExcelFile(file);
////        map.put("message", result);
////        return map;
////    }
//
//
//    @GetMapping("/list")
//    @ApiOperation(value = "获取摄像头列表", httpMethod = "GET")
//    public AjaxResult getAll(@RequestParam(value = "status", required = false) String status,
//                             @RequestParam(value = "isGb", required = false) String isGb,
//                             @RequestParam(value = "deviceName", required = false) String deviceName,
//                             @RequestParam(value = "deviceDomain", required = false) String deviceDomain,
//                             @RequestParam(value = "deviceCode", required = false) String deviceCode,
//                             @RequestParam(value = "region[]", required = false) String[] regionList,
//                             @RequestParam(value = "ipAddress", required = false) String ipAddress,
//                             @RequestParam(value = "id", required = false) Long id,
//                             @RequestParam(value = "manufacturer", required = false) String manufacturer,
//                             @RequestParam(value = "sipServerId", required = false) Long sipServerId,
//                             @RequestParam(value = "source", required = false) Long source,
//                             @RequestParam(required = false) String orderType,
//                             @RequestParam(required = false) String orderValue) throws Exception {
//
//        JSONObject filters = new JSONObject();
//        filters.put("status", status);
//        filters.put("isGb", isGb);
//        filters.put("deviceName", deviceName);
//        filters.put("deviceDomain", deviceDomain);
//        filters.put("deviceCode", deviceCode);
//        filters.put("ipAddress", ipAddress);
//        filters.put("sipServerId", sipServerId);
//        filters.put("manufacturer", manufacturer);
//        filters.put("source", source);
//        filters.put("id", id);
//        if (orderType != null && orderValue != null && !orderType.isEmpty() && !orderValue.isEmpty()  ) {
//            filters.put("orderType", orderValue + " " + orderType);
//        }
//        filters = RegionUtil.setRegion(filters,regionList);
//        LOGGER.debug("pageNum:{} pageSize:{} filters:{}", pageNum, pageSize, filters);
//        PageInfo<Camera> page = cameraManager.page(pageNum, pageSize, filters);
//        System.out.println(page.toString()+"---------->");
//        return ResultUtils.success(page);
//    }
//
//    @GetMapping("/visited/list")
//    @ApiOperation(value = "获取摄像头列表", httpMethod = "GET")
//    public Result getVisitedNumberList(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
//                                       @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
//                                       @RequestParam(value = "stime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime stime,
//                                       @RequestParam(value = "etime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime etime,
//                                       @RequestParam(value = "status", required = false) String status,
//                                       @RequestParam(value = "deviceName", required = false) String deviceName,
//                                       @RequestParam(value = "deviceCode", required = false) String deviceCode,
//                                       @RequestParam(value = "region[]", required = false) String[] regionList,
//                                       @RequestParam(value = "ipAddress", required = false) String ipAddress,
//                                       @RequestParam(value = "id", required = false) Long id,
//                                       @RequestParam(value = "manufacturer", required = false) String manufacturer,
//                                       @RequestParam(value = "sipServerId", required = false) Long sipServerId) throws Exception {
//
//        JSONObject filters = new JSONObject();
//        filters.put("stime", stime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
//        filters.put("etime", etime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
//        filters.put("status", status);
//        filters.put("deviceName", deviceName);
//        filters.put("deviceCode", deviceCode);
//        filters.put("ipAddress", ipAddress);
//        filters.put("sipServerId", sipServerId);
//        filters.put("manufacturer", manufacturer);
//        filters.put("id", id);
//        filters = RegionUtil.setRegion(filters,regionList);
//        LOGGER.debug("pageNum:{} pageSize:{} filters:{}", pageNum, pageSize, filters);
//        PageInfo<Camera> page = cameraManager.visitedPage(pageNum, pageSize, filters);
//        return ResultUtils.success(page);
//    }
//
//    @GetMapping("/findEventCameras")
//    @ApiOperation(value = "获取告警摄像头列表", httpMethod = "GET")
//    public Result findEventCameras(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
//                                   @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
//                                   @RequestParam(value = "deviceCode", required = false) String deviceCode,
//                                   @RequestParam(value = "ipAddress", required = false) String ipAddress,
//                                   @RequestParam(value = "id", required = false) Long id) throws Exception {
//
//        JSONObject filters = new JSONObject();
//        filters.put("deviceCode", deviceCode);
//        filters.put("ipAddress", ipAddress);
//        filters.put("id", id);
//        LOGGER.debug("pageNum:{} pageSize:{} filters:{}", pageNum, pageSize, filters);
//        PageInfo<Camera> page = cameraManager.findEventCameras(pageNum, pageSize, filters);
//        return ResultUtils.success(page);
//
//    }
//
//    @PostMapping
//    @ApiOperation(value = "新增一个摄像头", httpMethod = "POST")
//    public Result postSipDevice(@Valid @RequestBody CameraDto dto, BindingResult bindingResult) {
//
//        if (bindingResult.hasErrors()) {
//            return ResultUtils.error(ResultEnum.PARAM_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage());
//        }
//        cameraManager.save(doForward(dto));
//        return ResultUtils.success();
//    }
//
//    @PutMapping
//    @ApiOperation(value = "更新一个摄像头", httpMethod = "PUT")
//    public Result putSipDevice(@Valid @RequestBody CameraDto dto, BindingResult bindingResult) {
//
//        if (bindingResult.hasErrors()) {
//            return ResultUtils.error(ResultEnum.PARAM_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage());
//        }
//        cameraManager.save(doForward(dto));
//        return ResultUtils.success();
//    }
//
//
//    @PutMapping("/changAllStatus")
//    @ApiOperation(value = "更新所有摄像头状态为已确认摄像头", httpMethod = "PUT")
//    public Result putAllCameraStatus() {
//        cameraManager.changAllStatus();
//        return ResultUtils.success();
//    }
//
//    @PutMapping("/updateStatus")
//    @ApiOperation(value = "批量更新摄像头状态为已确认", httpMethod = "PUT")
//    public Result updateStatus(@Valid @RequestBody JSONObject jsonObject) {
//        String str1 = String.valueOf(jsonObject.get("id"));
//        String str = str1.substring(1, str1.length() - 1);
//        List<Camera> list = new ArrayList<Camera>();
//        String[] array = str.split(",");
//        for (String i : array) {
//            Camera camera = new Camera();
//            Long l = 0L;
//            l = Long.valueOf(i.trim());
//            camera.setId(l);
//            list.add(camera);
//        }
//        cameraManager.changStatus(list);
//        return ResultUtils.success();
//    }
//
//    @DeleteMapping
//    @ApiOperation(value = "删除一个摄像头", httpMethod = "DELETE")
//    public Result deleteSipDevice(@RequestParam(name = "id") Long id) {
//        if (id == null) {
//            return ResultUtils.error(ResultEnum.PARAM_ERROR);
//        }
//        cameraManager.deleteById(id);
//        return ResultUtils.success();
//    }
//
//    @GetMapping("/status")
//    @ApiOperation(value = "当天摄像头的状态")
//    public Result getStatus() {
//        return ResultUtils.success(cameraManager.getCurrentStatus());
//    }
//
//    @GetMapping("/active")
//    @ApiOperation(value = "活跃摄像头列表")
//    public Result getActiveCamera(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
//                                  @RequestParam(value = "pageSize", defaultValue = "20") int pageSize) {
//        return ResultUtils.success(cameraManager.getActiveCamera(pageNum, pageSize));
//    }
//
//    @GetMapping("/getNonNationalCameraList")
//    @ApiOperation(value = "获取首页国标、非国标编号摄像头列表")
//    public Result getNonNationalCameraList(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
//                                           @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
//                                           @RequestParam(value = "type", required = false) String type) {
//        JSONObject filters = new JSONObject();
//        int isGb = type.equals("gb") ? 1 : 0;
//        filters.put("isGb", isGb);
//        return ResultUtils.success(cameraManager.getNonNationalCameraList(pageNum, pageSize, filters));
//    }
//
//    @GetMapping("/getCameraDatas")
//    @ApiOperation(value = "获取首页摄像头数据", httpMethod = "GET")
//    public Result getCameraDatas() {
//        return ResultUtils.success(homePageManager.findObject("camera","camera_num"));
//    }
//
//    @GetMapping("/findChildCameras")
//    @ApiOperation(value = "获取子级摄像头", httpMethod = "GET")
//    public Result getChildCamera(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
//                                 @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
//                                 @RequestParam(value = "serverDomain", required = false) String serverDomain,
//                                 @RequestParam(value = "serverIp", required = false) Long serverIp) {
//        JSONObject filters = new JSONObject();
//        filters.put("deviceDomain", serverDomain);
//        filters.put("domainIp", serverIp);
//        return ResultUtils.success(cameraManager.findChild(pageNum, pageSize, filters));
//    }
//
//    @GetMapping("/getNonNationalCamera")
//    @ApiOperation(value = "获取首页国标、非国标编号摄像头数量占比", httpMethod = "GET")
//    public Result getNonNationalCamera() {
//        return ResultUtils.success(cameraManager.getNonNationalCamera());
//    }
//
//    @GetMapping("/changestatus")
//    @ApiOperation(value = "更新状态", httpMethod = "GET")
//    public Result changeStatus(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
//                               @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
//                               @RequestParam(value = "status", required = false) String status,
//                               @RequestParam(value = "deviceName", required = false) String deviceName,
//                               @RequestParam(value = "region[]", required = false) String[] regionList,
//                               @RequestParam(value = "level", required = false) Long level,
//                               @RequestParam(value = "ipAddress", required = false) String ipAddress) {
//        JSONObject filters = new JSONObject();
//        filters.put("status", status);
//        filters.put("deviceName", deviceName);
//        filters.put("ipAddress", ipAddress);
//        if (level != null) {
//            //level为系统设置地区的等级
//            if (level == 1) {
//                if (regionList != null) {
//                    String region = regionList[regionList.length - 1];
//                    if (regionList.length == 1) {
//                        String cityRegion = region.substring(0, 4);
//                        filters.put("cityRegion", cityRegion);
//                    }
//                    if (regionList.length == 2) {
//                        String countryRegion = region.substring(0, 6);
//                        filters.put("countryRegion", countryRegion);
//                    }
//                }
//            } else {
//                if (regionList != null) {
//                    String region = regionList[regionList.length - 1];
//                    String countryRegion = region.substring(0, 6);
//                    filters.put("countryRegion", countryRegion);
//                }
//            }
//        }
//        PageInfo<Camera> page = cameraManager.page(pageNum, pageSize, filters);
//        List<Camera> list = new ArrayList<Camera>();
//        boolean flag = false;
//        if (page.getSize() > 0) {
//            for (int i = 0; i < page.getEndRow(); i++) {
//                if (page.getList().get(i).getStatus().getValue().equals("1") || page.getList().get(i).getStatus().getLabel().equals("新发现") || page.getList().get(i).getStatus().getLabel().equals("NEW")) {
//                    list.add(page.getList().get(i));
//                }
//            }
//            flag = cameraManager.changStatus(list);
//        } else {
//            flag = true;
//        }
//
//        return ResultUtils.success(flag);
//    }
//
//    @GetMapping("/sessionCameraList")
//    @ApiOperation(value = "获取会话相关的摄像头列表", httpMethod = "GET")
//    public Result getSessionCameraList(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
//                                       @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
//                                       @RequestParam(value = "stime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime stime,
//                                       @RequestParam(value = "etime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime etime,
//                                       @RequestParam(value = "sessionId", required = false) Long sessionId,
//                                       @RequestParam(value = "deviceCode", required = false) String deviceCode,
//                                       @RequestParam(value = "ipAddress", required = false) String ipAddress) {
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("sessionId", sessionId);
//        jsonObject.put("deviceCode", deviceCode);
//        jsonObject.put("ip", ipAddress);
//        if(stime != null) {
//            jsonObject.put("stime", stime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
//        }
//        if(etime != null) {
//            jsonObject.put("etime", etime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
//        }
//        PageInfo<Camera> pageInfo = cameraManager.getSessionCameraList(pageNum, pageSize, jsonObject);
//        return ResultUtils.success(pageInfo);
//    }
//
//    //导入外部设备表excel
//    @PostMapping(value = "/importExt")
//    @ApiOperation(value = "导入外部摄像头列表")
//    public Map<String, Object> importExtExcel(@RequestParam(value = "file", required = false) MultipartFile file) {
//        Map<String, Object> map = new HashMap<String, Object>();
//        String result = cameraManager.readExtExcelFile(file);
//        map.put("message", result);
//        return map;
//    }
//
//    @Override
//    protected Camera doForward(CameraDto dto) {
//        Camera entity = new Camera();
//        BeanUtils.copyProperties(dto, entity);
//        return entity;
//    }
//
//    @Override
//    protected CameraDto doBackward(Camera camera) {
//        CameraDto dto = new CameraDto();
//        BeanUtils.copyProperties(camera, dto);
//        return dto;
//    }
//}
