package com.yuanqing.project.tiansu.controller.assets;

import com.alibaba.fastjson.JSONObject;

import com.yuanqing.common.enums.SaveType;
import com.yuanqing.common.utils.StringUtils;
import com.yuanqing.common.utils.ip.IpUtils;
import com.yuanqing.framework.redis.RedisCache;
import com.yuanqing.framework.web.controller.BaseController;
import com.yuanqing.framework.web.domain.AjaxResult;
import com.yuanqing.framework.web.page.TableDataInfo;
import com.yuanqing.project.tiansu.domain.assets.Camera;
import com.yuanqing.project.tiansu.domain.assets.dto.CameraDto;
import com.yuanqing.project.tiansu.service.assets.ICameraService;
import com.yuanqing.project.tiansu.service.feign.AlarmFeignClient;
import com.yuanqing.project.tiansu.service.macs.IMacsConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yuanqing.common.constant.Constants.*;

/**
 * 摄像头Controller
 *
 * @author xucan
 */
@RestController
@RequestMapping(value = "/api/device/sip-device")
@CrossOrigin
@Api(value = "摄像头", description = "摄像头相关Api")
public class CameraController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CameraController.class);

//    @Value("${tiansu.alarmhost}")
//    private String alarmHost;

    @Autowired
    private ICameraService cameraService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private IMacsConfigService macsConfigService;

    @Resource
    private AlarmFeignClient alarmFeignClient;

    @GetMapping("/list")
    @ApiOperation(value = "获取摄像头列表", httpMethod = "GET")
    public AjaxResult getAll(@ApiParam("状态")@RequestParam(value = "status", required = false) Integer status,
                             @ApiParam("是否国标")@RequestParam(value = "isGb", required = false) Integer isGb,
                             @ApiParam("设备名称")@RequestParam(value = "deviceName", required = false) String deviceName,
                             @ApiParam("设备域")@RequestParam(value = "deviceDomain", required = false) String deviceDomain,
                             @ApiParam("设备编码")@RequestParam(value = "deviceCode", required = false) String deviceCode,
                             @ApiParam("区域ID")@RequestParam(value = "regionId", required = false) Integer regionId,
                             @ApiParam("IP地址")@RequestParam(value = "ipAddress", required = false) String ipAddress,
                             @ApiParam("ID")@RequestParam(value = "id", required = false) Long id,
                             @ApiParam("设备厂商")@RequestParam(value = "manufacturer", required = false) String manufacturer,
                             @ApiParam("信令服务器ID")@RequestParam(value = "sipServerId", required = false) Long sipServerId,
                             @ApiParam("来源")@RequestParam(value = "source", required = false) Long source,
                             @ApiParam("排序")@RequestParam(required = false) String orderType,
                             @ApiParam("排序对象")@RequestParam(required = false) String orderValue) {

        Camera camera = new Camera();
        camera.setStatus(status);
        camera.setIsGb(isGb);
        camera.setDeviceName(deviceName);
        camera.setRegion(regionId);
        camera.setSource(source);

        camera.setDeviceDomain(deviceDomain);
        camera.setDeviceCode(deviceCode);
        camera.setIpAddress(IpUtils.ipToLong(ipAddress));

        camera.setSipServerId(sipServerId);
        camera.setManufacturer(manufacturer);
        camera.setId(id);

        String orderStr = null;
        if (!StringUtils.isEmpty(orderType) && !StringUtils.isEmpty(orderValue)) {
            orderStr = orderValue + " " + orderType;
        }

        startPage();
        List<Camera> getList = cameraService.getListWithOrder(camera, orderStr);

        macsConfigService.setLowerRegionByCamera(getList);

        AjaxResult success = AjaxResult.success(getDataTable(getList));
        return success;
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

    @GetMapping("/findEventCameras")
    @ApiOperation(value = "获取告警摄像头列表", httpMethod = "GET")
    public AjaxResult findEventCameras(@ApiParam("页码数")@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                       @ApiParam("行数")@RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                       @ApiParam("设备编码")@RequestParam(value = "deviceCode", required = false) String deviceCode,
                                       @ApiParam("IP地址")@RequestParam(value = "ipAddress", required = false) String ipAddress,
                                       @ApiParam("ID")@RequestParam(value = "id", required = false) Long id,
                                       @ApiParam("排序")@RequestParam(required = false) String orderType,
                                       @ApiParam("排序对象")@RequestParam(required = false) String orderValue) {
        List<Camera> list = new ArrayList<>();
//        String url = alarmHost + "/BusiEvent/getCameraId";
//        String result = HttpUtils.sendGet(url, "event_id=" + id);
        String result = alarmFeignClient.getCameraId(id);
        JSONObject resultObj = JSONObject.parseObject(result);

        Long code = resultObj.getLong("code");
        if (code == 200) {
            String ids = resultObj.getString("msg");
            JSONObject filters = new JSONObject();
            filters.put("deviceCode", deviceCode);
            filters.put("ipAddress", ipAddress);
            filters.put("id", ids);
            String orderStr = null;
            if (!StringUtils.isEmpty(orderType) && !StringUtils.isEmpty(orderValue)) {
                orderStr = orderValue + " " + orderType;
            }
            filters.put("orderType", orderStr);
            startPage();
            list = cameraService.findEventCameras(filters);
        }
        return AjaxResult.success(getDataTable(list));
    }

    @PostMapping
    @ApiOperation(value = "新增一个摄像头", httpMethod = "POST")
    public AjaxResult postSipDevice(@ApiParam("摄像头DTO对象")@Valid @RequestBody CameraDto dto) {
        cameraService.save(doForward(dto), SaveType.INSERT);
        return AjaxResult.success();
    }

    @PutMapping
    @ApiOperation(value = "更新一个摄像头", httpMethod = "PUT")
    public AjaxResult putSipDevice(@ApiParam("摄像头DTO对象")@Valid @RequestBody CameraDto dto) {
        cameraService.save(doForward(dto));
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
    public AjaxResult updateStatus(@ApiParam("勾选的摄像头id")@Valid @RequestBody JSONObject jsonObject) {
        String str1 = String.valueOf(jsonObject.get("id"));
        String str = str1.substring(1, str1.length() - 1);
        String[] array = str.split(",");
        cameraService.changStatus(array);
        return AjaxResult.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除一个摄像头", httpMethod = "DELETE")
    public AjaxResult deleteSipDevice(@ApiParam("勾选的摄像头id")@RequestParam(name = "id") Long id) {
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
        startPage();
        List<Camera> list = cameraService.getScreenList(new Camera());
        return getDataTable(list);
    }

    @GetMapping("/getNonNationalCameraList")
    @ApiOperation(value = "获取首页国标、非国标编号摄像头列表")
    public TableDataInfo getNonNationalCameraList(@ApiParam("摄像头编码类型")@RequestParam(value = "type", required = false) String type) {
        Camera camera = new Camera();
        int isGb = "gb".equals(type) ? 1 : 0;
        camera.setIsGb(isGb);
        return getDataTable(cameraService.getNonNationalCameraList(camera));
    }


    @GetMapping("/getCameraDatas")
    @ApiOperation(value = "获取首页摄像头数据", httpMethod = "GET")
    public AjaxResult getCameraDatas() {
        return AjaxResult.success("success", redisCache.getCacheObject(INDEX_CAMERA_COUNTS_CACHE));
    }


    @GetMapping("/getNonNationalCamera")
    @ApiOperation(value = "获取首页国标、非国标编号摄像头数量占比", httpMethod = "GET")
    public AjaxResult getNonNationalCamera() {
        return AjaxResult.success(cameraService.getNonNationalCamera());
    }


    @GetMapping("/sessionCameraList")
    @ApiOperation(value = "获取会话相关的摄像头列表", httpMethod = "GET")
    public AjaxResult getSessionCameraList(@ApiParam("页码数")@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                           @ApiParam("行数")@RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                           @ApiParam("开始时间")@RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime stime,
                                           @ApiParam("结束时间")@RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime etime,
                                           @ApiParam("会话ID")@RequestParam(value = "sessionId", required = false) Long sessionId,
                                           @ApiParam("设备编码")@RequestParam(value = "deviceCode", required = false) String deviceCode,
                                           @ApiParam("设备名称")@RequestParam(value = "deviceName", required = false) String deviceName,
                                           @ApiParam("IP地址")@RequestParam(value = "ipAddress", required = false) String ipAddress) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sessionId", sessionId);
        jsonObject.put("deviceCode", deviceCode);
        jsonObject.put("deviceName", deviceName);
        jsonObject.put("ip", IpUtils.ipToLong(ipAddress));
        if (stime != null) {
            jsonObject.put("stime", stime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        }
        if (etime != null) {
            jsonObject.put("etime", etime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        }
        startPage();
        List<Camera> list = cameraService.getSessionCameraList(jsonObject);
        return AjaxResult.success(getDataTable(list));
    }

    //导入外部设备表excel
    @PostMapping(value = "/importExt")
    @ApiOperation(value = "导入外部摄像头列表")
    public Map<String, Object> importExtExcel(@ApiParam("外部设备表excel")@RequestParam(value = "file", required = false) MultipartFile file) {
        Map<String, Object> map = new HashMap<String, Object>();
        String AjaxResult = cameraService.readExtExcelFile(file);
        map.put("message", AjaxResult);
        return map;
    }


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
