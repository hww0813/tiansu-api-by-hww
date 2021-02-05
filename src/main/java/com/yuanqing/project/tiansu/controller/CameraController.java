package com.yuanqing.project.tiansu.controller;

import com.alibaba.fastjson.JSONObject;

import com.yuanqing.common.enums.SaveType;
import com.yuanqing.common.utils.StringUtils;
import com.yuanqing.common.utils.ip.IpUtils;
import com.yuanqing.framework.web.controller.BaseController;
import com.yuanqing.framework.web.domain.AjaxResult;
import com.yuanqing.framework.web.page.TableDataInfo;
import com.yuanqing.project.tiansu.domain.assets.Camera;
import com.yuanqing.project.tiansu.domain.assets.dto.CameraDto;
import com.yuanqing.project.tiansu.service.assets.ICameraService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 摄像头Controller
 * @author xucan
 */
@RestController
@RequestMapping(value = "/api/device/sip-device")
@CrossOrigin
@Api(value = "摄像头", description = "摄像头相关Api")
public class CameraController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CameraController.class);

    @Autowired
    private ICameraService cameraService;

    @GetMapping("/list")
    @ApiOperation(value = "获取摄像头列表", httpMethod = "GET")
    public TableDataInfo getAll(@RequestParam(value = "status", required = false) Integer status,
                                @RequestParam(value = "isGb", required = false) Integer isGb,
                                @RequestParam(value = "deviceName", required = false) String deviceName,
                                @RequestParam(value = "deviceDomain", required = false) String deviceDomain,
                                @RequestParam(value = "deviceCode", required = false) String deviceCode,
                                @RequestParam(value = "region[]", required = false) String[] regionList,
                                @RequestParam(value = "ipAddress", required = false) String ipAddress,
                                @RequestParam(value = "id", required = false) Long id,
                                @RequestParam(value = "manufacturer", required = false) String manufacturer,
                                @RequestParam(value = "sipServerId", required = false) Long sipServerId,
                                @RequestParam(value = "source", required = false) Long source,
                                @RequestParam(required = false) String orderType,
                                @RequestParam(required = false) String orderValue) throws Exception {

        Camera camera = new Camera();
        camera.setStatus(status);
        camera.setIsGb(isGb);
        camera.setDeviceName(deviceName);
        camera.setDeviceDomain(deviceDomain);
        camera.setDeviceCode(deviceCode);
        if(StringUtils.isNotEmpty(ipAddress)){
            camera.setIpAddress(IpUtils.ipToLong(ipAddress));
        }
        camera.setSipServerId(sipServerId);
        camera.setManufacturer(manufacturer);
        camera.setId(id);


        startPage();
        List<Camera> getList = cameraService.getList(camera);

        return getDataTable(getList);
    }
//
//    @GetMapping("/visited/list")
//    @ApiOperation(value = "获取摄像头列表", httpMethod = "GET")
//    public AjaxResult getVisitedNumberList(@RequestParam(value = "getListNum", defaultValue = "1") int getListNum,
//                                       @RequestParam(value = "getListSize", defaultValue = "20") int getListSize,
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
////        filters = RegionUtil.setRegion(filters,regionList);
//        LOGGER.debug("getListNum:{} getListSize:{} filters:{}", getListNum, getListSize, filters);
//        List<Camera> getList = cameraService.visitedgetList(filters);
//        return AjaxResult.success(getList);
//    }
//
//    @GetMapping("/findEventCameras")
//    @ApiOperation(value = "获取告警摄像头列表", httpMethod = "GET")
//    public AjaxResult findEventCameras(@RequestParam(value = "getListNum", defaultValue = "1") int getListNum,
//                                   @RequestParam(value = "getListSize", defaultValue = "20") int getListSize,
//                                   @RequestParam(value = "deviceCode", required = false) String deviceCode,
//                                   @RequestParam(value = "ipAddress", required = false) String ipAddress,
//                                   @RequestParam(value = "id", required = false) Long id) throws Exception {
//
//        JSONObject filters = new JSONObject();
//        filters.put("deviceCode", deviceCode);
//        filters.put("ipAddress", ipAddress);
//        filters.put("id", id);
//        LOGGER.debug("getListNum:{} getListSize:{} filters:{}", getListNum, getListSize, filters);
//        List<Camera> getList = cameraService.findEventCameras(getListNum, getListSize, filters);
//        return AjaxResult.success(getList);
//
//    }
//
    @PostMapping
    @ApiOperation(value = "新增一个摄像头", httpMethod = "POST")
    public AjaxResult postSipDevice(@Valid @RequestBody CameraDto dto) {
        cameraService.save(doForward(dto),SaveType.INSERT);
        return AjaxResult.success();
    }

    @PutMapping
    @ApiOperation(value = "更新一个摄像头", httpMethod = "PUT")
    public AjaxResult putSipDevice(@Valid @RequestBody CameraDto dto) {
        cameraService.save(doForward(dto), SaveType.UPDATE);
        return AjaxResult.success();
    }

//
    @PutMapping("/changAllStatus")
    @ApiOperation(value = "更新所有摄像头状态为已确认摄像头", httpMethod = "PUT")
    public AjaxResult putAllCameraStatus() {
        cameraService.changAllStatus();
        return AjaxResult.success();
    }

    @PutMapping("/updateStatus")
    @ApiOperation(value = "批量更新摄像头状态为已确认", httpMethod = "PUT")
    public AjaxResult updateStatus(@Valid @RequestBody JSONObject jsonObject) {
        String str1 = String.valueOf(jsonObject.get("id"));
        String str = str1.substring(1, str1.length() - 1);
        String[] array = str.split(",");
        cameraService.changStatus(array);
        return AjaxResult.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除一个摄像头", httpMethod = "DELETE")
    public AjaxResult deleteSipDevice(@RequestParam(name = "id") Long id) {
        cameraService.deleteById(id);
        return AjaxResult.success();
    }

    @GetMapping("/status")
    @ApiOperation(value = "当天摄像头的状态")
    public AjaxResult getStatus() {
        return AjaxResult.success(cameraService.getCurrentStatus());
    }

    @GetMapping("/active")
    @ApiOperation(value = "活跃摄像头列表")
    public TableDataInfo getActiveCamera() {
        return getDataTable(cameraService.getActiveCamera());
    }

    @GetMapping("/getNonNationalCameraList")
    @ApiOperation(value = "获取首页国标、非国标编号摄像头列表")
    public TableDataInfo getNonNationalCameraList(@RequestParam(value = "type", required = false) String type) {
        Camera camera = new Camera();
        int isGb = type.equals("gb") ? 1 : 0;
        camera.setIsGb(isGb);
        return getDataTable(cameraService.getNonNationalCameraList(camera));
    }

//    @GetMapping("/getCameraDatas")
//    @ApiOperation(value = "获取首页摄像头数据", httpMethod = "GET")
//    public AjaxResult getCameraDatas() {
//        return AjaxResult.success(homegetListManager.findObject("camera","camera_num"));
//    }
//

    @GetMapping("/getNonNationalCamera")
    @ApiOperation(value = "获取首页国标、非国标编号摄像头数量占比", httpMethod = "GET")
    public AjaxResult getNonNationalCamera() {
        return AjaxResult.success(cameraService.getNonNationalCamera());
    }

//
//    @GetMapping("/sessionCameraList")
//    @ApiOperation(value = "获取会话相关的摄像头列表", httpMethod = "GET")
//    public AjaxResult getSessionCameraList(@RequestParam(value = "getListNum", defaultValue = "1") int getListNum,
//                                       @RequestParam(value = "getListSize", defaultValue = "20") int getListSize,
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
//        List<Camera> List = cameraService.getSessionCameraList(getListNum, getListSize, jsonObject);
//        return AjaxResult.success(List);
//    }
//
//    //导入外部设备表excel
//    @PostMapping(value = "/importExt")
//    @ApiOperation(value = "导入外部摄像头列表")
//    public Map<String, Object> importExtExcel(@RequestParam(value = "file", required = false) MultipartFile file) {
//        Map<String, Object> map = new HashMap<String, Object>();
//        String AjaxResult = cameraService.readExtExcelFile(file);
//        map.put("message", AjaxResult);
//        return map;
//    }
//

    protected Camera doForward(CameraDto dto) {
        Camera entity = new Camera();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }

    protected CameraDto doBackward(Camera camera) {
        CameraDto dto = new CameraDto();
        BeanUtils.copyProperties(camera, dto);
        return dto;
    }
}
