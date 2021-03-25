package com.yuanqing.project.tiansu.controller.analysis;

import com.alibaba.fastjson.JSONObject;
import com.yuanqing.common.utils.ip.IpUtils;
import com.yuanqing.framework.web.controller.BaseController;
import com.yuanqing.framework.web.domain.AjaxResult;
import com.yuanqing.framework.web.domain.PageResult;
import com.yuanqing.project.tiansu.domain.analysis.CameraVisit;
import com.yuanqing.project.tiansu.domain.analysis.Statistics;
import com.yuanqing.project.tiansu.domain.assets.Camera;
import com.yuanqing.project.tiansu.domain.operation.OperationBehavior;
import com.yuanqing.project.tiansu.service.analysis.IStatisticsService;
import com.yuanqing.project.tiansu.service.assets.ICameraService;
import com.yuanqing.project.tiansu.service.operation.IOperationBehaviorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author xucan
 * @version 1.0
 * @Date 2021-03-08 15:19
 */

@RestController
@RequestMapping(value = "/api/analysis/camera/visited")
@Api(value = "摄像头被访问接口", description = "摄像头被访问相关Api")
public class CameraVisitedController extends BaseController {


    @Autowired
    private IStatisticsService statisticsService;

    @Autowired
    private ICameraService cameraService;

    @Autowired
    private IOperationBehaviorService operationBehaviorService;


    @GetMapping("/list")
    @ApiOperation(value = "获取摄像头被访问列表", httpMethod = "GET")
    public AjaxResult getCameraVisitedList(@RequestParam(value = "action",required = false) Integer action,
                                           @RequestParam(value = "cameraIp",required = false) String cameraIp,
                                           Camera camera) {

        CameraVisit cameraVisit = new CameraVisit();
        cameraVisit.setAction(action);

        cameraVisit.setstartDate(camera.getstartDate());
        cameraVisit.setendDate(camera.getendDate());

        camera.setstartDate(null);
        camera.setendDate(null);
        camera.setIpAddress(IpUtils.ipToLong(cameraIp));

        startPage();

        List<Camera> cameraList = cameraService.getList(camera);

        List<CameraVisit> cameraVisitList = statisticsService.getCameraVisit(cameraList,cameraVisit);

        return AjaxResult.success(getDataTable(cameraVisitList));


    }

    @GetMapping("/relatedClient")
    @ApiOperation(value = "获取摄像头被访问相关客户端", httpMethod = "GET")
    public AjaxResult getCameraVisitedRelatedClientPage(@RequestParam(value = "deviceCode", required = false) String deviceCode,
                                                    @RequestParam(value = "action", required = false) Integer action,
                                                    @RequestParam(value = "startDate", required = false) String startDate,
                                                    @RequestParam(value = "endDate", required = false) String endDate) {
        Statistics statistics = new Statistics();

        statistics.setstartDate(startDate);
        statistics.setendDate(endDate);
        statistics.setAction(action);
        statistics.setDstCode(deviceCode);

        startPage();
        List<Statistics> statisticsList = statisticsService.getList(statistics);

        return AjaxResult.success(getDataTable(statisticsList));

    }

    @GetMapping("/cnt")
    @ApiOperation(value = "获取摄像头被访问次数", httpMethod = "GET")
    public AjaxResult getClientVisitDetailList(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                           @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                           @RequestParam(value = "startDate", required = false) String  startDate,
                                           @RequestParam(value = "endDate", required = false) String endDate,
                                           @RequestParam(value = "clientId", required = false) Long clientId,
                                           @RequestParam(value = "cameraId", required = false) Long cameraId,
                                           @RequestParam(value = "srcIp", required = false) String srcIp,
                                           @RequestParam(value = "username", required = false) String username,
                                           @RequestParam(value = "srcCode", required = false) String srcCode,
                                           @RequestParam(value = "action", required = false) String action) throws Exception {

        OperationBehavior operationBehavior = new OperationBehavior();
        operationBehavior.setClientId(clientId);
        operationBehavior.setCameraId(cameraId);
        operationBehavior.setDstCode(srcCode);
        operationBehavior.setAction(action);
        operationBehavior.setSrcIp(IpUtils.ipToLong(srcIp));
        operationBehavior.setUsername(username);
        operationBehavior.setstartDate(startDate);
        operationBehavior.setendDate(endDate);
        operationBehavior.setSize(pageSize);
        operationBehavior.setNum(pageNum);

        PageResult pageResult = operationBehaviorService.queryOperationList(operationBehavior);

        PageResult data = (PageResult)pageResult.get("data");

        List<OperationBehavior> operationBehaviorList = (List<OperationBehavior>) data.get("list");

        List<JSONObject> terminalVisitedCameraList = statisticsService.associateTerminalInfo(operationBehaviorList);

        return AjaxResult.success(getDataTable(terminalVisitedCameraList, (Integer) data.get("total")));


    }


}
