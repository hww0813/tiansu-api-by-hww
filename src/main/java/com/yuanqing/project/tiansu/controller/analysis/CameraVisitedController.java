package com.yuanqing.project.tiansu.controller.analysis;

import com.alibaba.fastjson.JSONObject;
import com.yuanqing.common.utils.StringUtils;
import com.yuanqing.common.utils.bean.BeanUtils;
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
import io.swagger.annotations.ApiParam;
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
    public AjaxResult getCameraVisitedList(@ApiParam("动作类型")@RequestParam(value = "action",required = false) Integer action,
                                           @ApiParam("摄像头IP")@RequestParam(value = "cameraIp",required = false) String cameraIp,
                                           @ApiParam("摄像头实体")Camera camera,
                                           @ApiParam("排序")@RequestParam(required = false) String orderType,
                                           @ApiParam("排序对象")@RequestParam(required = false) String orderValue) {

        CameraVisit cameraVisit = new CameraVisit();
        cameraVisit.setAction(action);

        cameraVisit.setstartDate(camera.getstartDate());
        cameraVisit.setendDate(camera.getendDate());

        camera.setstartDate(null);
        camera.setendDate(null);
        camera.setIpAddress(IpUtils.ipToLong(cameraIp));

        String orderStr = null;
        if (!StringUtils.isEmpty(orderType) && !StringUtils.isEmpty(orderValue)) {
            orderStr = orderValue + " " + orderType;
        }

        startPage();

        logger.error("------------>"+camera.toString());

        List<Camera> cameraList = null;

        cameraList = cameraService.getList(camera);
        List<CameraVisit> cameraVisitList = statisticsService.getCameraVisit(cameraList, cameraVisit, orderStr);

        return AjaxResult.success(getDataTable(cameraVisitList));


    }

    @GetMapping("/relatedClient")
    @ApiOperation(value = "获取摄像头被访问相关客户端", httpMethod = "GET")
    public AjaxResult getCameraVisitedRelatedClientPage(@ApiParam("设备编码")@RequestParam(value = "deviceCode", required = false) String deviceCode,
                                                        @ApiParam("动作类型")@RequestParam(value = "action", required = false) Integer action,
                                                        @ApiParam("开始时间")@RequestParam(value = "startDate", required = false) String startDate,
                                                        @ApiParam("结束时间")@RequestParam(value = "endDate", required = false) String endDate) {
        Statistics statistics = new Statistics();

        statistics.setstartDate(startDate);
        statistics.setendDate(endDate);
        statistics.setAction(action);
        statistics.setDstCode(deviceCode);

        startPage();
        List<Statistics> statisticsList = statisticsService.getClientUserList(statistics);

        return AjaxResult.success(getDataTable(statisticsList));

    }

    @GetMapping("/cnt")
    @ApiOperation(value = "获取摄像头被访问次数", httpMethod = "GET")
    public AjaxResult getClientVisitDetailList(@ApiParam("页码数")@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                               @ApiParam("行数")@RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                               @ApiParam("开始时间")@RequestParam(value = "startDate", required = false) String  startDate,
                                               @ApiParam("结束时间")@RequestParam(value = "endDate", required = false) String endDate,
                                               @ApiParam("客户端ID")@RequestParam(value = "clientId", required = false) Long clientId,
                                               @ApiParam("摄像头ID")@RequestParam(value = "cameraId", required = false) Long cameraId,
                                               @ApiParam("源IP")@RequestParam(value = "srcIp", required = false) String srcIp,
                                               @ApiParam("目的设备编码")@RequestParam(value = "deviceCode", required = false) String dstCode,
                                               @ApiParam("用户名")@RequestParam(value = "username", required = false) String username,
                                               @ApiParam("源设备编码")@RequestParam(value = "srcCode", required = false) String srcCode,
                                               @ApiParam("动作类型")@RequestParam(value = "action", required = false) String action) throws Exception {

        OperationBehavior operationBehavior = new OperationBehavior();
        operationBehavior.setClientId(clientId);
        operationBehavior.setCameraId(cameraId);
        operationBehavior.setDstCode(srcCode);
        operationBehavior.setAction(action);
        operationBehavior.setSrcIp(IpUtils.ipToLong(srcIp));
        operationBehavior.setUsername(username);
        operationBehavior.setstartDate(startDate);
        operationBehavior.setDstCode(dstCode);
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
