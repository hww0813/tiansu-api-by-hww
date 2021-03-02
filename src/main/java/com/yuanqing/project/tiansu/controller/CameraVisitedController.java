//package com.yuanqing.project.tiansu.controller;
//
//import com.alibaba.fastjson.JSONObject;
//import com.github.pagehelper.PageInfo;
//import com.yuanqing.modal.Result;
//import com.yuanqing.modal.video.OperationBehavior;
//import com.yuanqing.project.tiansu.service.assets.ICameraService;
//import com.yuanqing.project.tiansu.service.assets.IClientService;
//import com.yuanqing.service.analysis.CameraVisitedManager;
//import com.yuanqing.service.assets.ClientManager;
//import com.yuanqing.util.RegionUtil;
//import com.yuanqing.util.ResultUtils;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.format.annotation.DateTimeFormat;
//import org.springframework.web.bind.annotation.*;
//
//import javax.annotation.Resource;
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//
//@RestController
//@RequestMapping(value = "/api/analysis/camera/visited")
//@Api(value = "摄像头被访问接口", description = "摄像头被访问相关Api")
//@CrossOrigin
//public class CameraVisitedController {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(CameraVisitedController.class);
//
//    @Resource
//    private ICameraService cameraService;
//
//    @Resource
//    private CameraVisitedManager cameraVisitedManager;
//
//    @Resource
//    private IClientService clientService;
//
//    @GetMapping("/list")
//    @ApiOperation(value = "获取摄像头被访问列表", httpMethod = "GET")
//    public Result getCameraVisitedList(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
//                                       @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
//                                       @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
//                                       @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
//                                       @RequestParam(value = "cameraIp", required = false) String cameraIp,
//                                       @RequestParam(value = "region[]", required = false) String[] region,
//                                       @RequestParam(value = "cameraCode", required = false) String cameraCode,
//                                       @RequestParam(value = "cameraName", required = false) String cameraName,
//                                       @RequestParam(value = "action", required = false) String action,
//                                       @RequestParam(value = "cameraId", required = false) String cameraId,
//                                       @RequestParam(required = false) String orderType,
//                                       @RequestParam(required = false) String orderValue) {
//
//        JSONObject filters = new JSONObject();
//        if (startDate != null) filters.put("startDate", startDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
//        if (startDate != null) filters.put("endDate", endDate.plusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE));
//        filters.put("cameraIp", cameraIp);
//        filters.put("cameraCode", cameraCode);
//        filters.put("cameraName", cameraName);
//        filters.put("action", action);
//        filters.put("cameraId", cameraId);
//        filters = RegionUtil.setRegion(filters, region);
//        if (!orderType.isEmpty() && !orderValue.isEmpty()) {
//            filters.put("orderType", orderValue + " " + orderType);
//        }
//        PageInfo<JSONObject> pageInfo = cameraVisitedManager.page(pageNum, pageSize, filters);
//
//        return ResultUtils.success(pageInfo);
//    }
//
//    @GetMapping("/cnt")
//    @ApiOperation(value = "获取摄像头被访问次数", httpMethod = "GET")
//    public Result getClientVisitDetailList(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
//                                           @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
//                                           @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
//                                           @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
//                                           @RequestParam(value = "clientId", required = false) Long clientId,
//                                           @RequestParam(value = "cameraId", required = false) Long cameraId,
//                                           @RequestParam(value = "srcIp", required = false) String srcIp,
//                                           @RequestParam(value = "username", required = false) String username,
//                                           @RequestParam(value = "srcCode", required = false) String srcCode,
//                                           @RequestParam(value = "action", required = false) String action) {
//        JSONObject filters = new JSONObject();
//        if (startDate != null) filters.put("startDate", startDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
//        if (startDate != null) filters.put("endDate", endDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
//        filters.put("clientId", clientId);
//        filters.put("cameraId", cameraId);
//        filters.put("srcIp", srcIp);
//        filters.put("username", username);
//        filters.put("srcCode", srcCode);
//        filters.put("action", action);
//
//        PageInfo<OperationBehavior> pageInfo = cameraVisitedManager.getCameraVisitedCntList(pageNum, pageSize, filters);
//        return ResultUtils.success(pageInfo);
//    }
//
//    @GetMapping("/relatedClient")
//    @ApiOperation(value = "获取摄像头被访问相关客户端", httpMethod = "GET")
//    public Result getCameraVisitedRelatedClientPage(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
//                                                    @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
//                                                    @RequestParam(value = "clientId", required = false) Long clientId,
//                                                    @RequestParam(value = "id", required = false) Long cameraId,
//                                                    @RequestParam(value = "action", required = false) String action,
//                                                    @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
//                                                    @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
//        JSONObject filters = new JSONObject();
//        filters.put("clientId", clientId);
//        filters.put("cameraId", cameraId);
//        filters.put("action", action);
//        if (startDate != null) filters.put("startDate", startDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
//        if (startDate != null) filters.put("endDate", endDate.plusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE));
//        PageInfo<JSONObject> pageInfo = cameraVisitedManager.getCameraVisitedRelatedClientPage(pageNum, pageSize, filters);
//        return ResultUtils.success(pageInfo);
//    }
//
//
//}
