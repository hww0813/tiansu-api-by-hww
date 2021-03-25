package com.yuanqing.project.tiansu.controller.analysis;

import com.alibaba.fastjson.JSONObject;
import com.yuanqing.common.utils.StringUtils;
import com.yuanqing.common.utils.ip.IpUtils;
import com.yuanqing.framework.web.controller.BaseController;
import com.yuanqing.framework.web.domain.AjaxResult;
import com.yuanqing.framework.web.domain.PageResult;
import com.yuanqing.project.tiansu.domain.analysis.CameraVisit;
import com.yuanqing.project.tiansu.domain.analysis.Statistics;
import com.yuanqing.project.tiansu.domain.analysis.TerminalVisit;
import com.yuanqing.project.tiansu.domain.assets.Camera;
import com.yuanqing.project.tiansu.domain.operation.OperationBehavior;
import com.yuanqing.project.tiansu.service.analysis.IStatisticsService;
import com.yuanqing.project.tiansu.service.assets.ICameraService;
import com.yuanqing.project.tiansu.service.operation.IOperationBehaviorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xucan
 * @version 1.0
 * @Date 2021-03-03 11:02
 */

@RestController
@RequestMapping(value = "/api/analysis/client/visit")
@Api(value = "客户端访问接口", description = "客户端访问相关Api")
public class TerminalVisitedController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TerminalVisitedController.class);

    @Autowired
    private IStatisticsService statisticsService;

    @Autowired
    private IOperationBehaviorService operationBehaviorService;

    @Autowired
    private ICameraService cameraService;


    @GetMapping("/list")
    @ApiOperation(value = "获取客户端访问列表", httpMethod = "GET")
    public AjaxResult getClientVisitPage(@RequestParam(value = "startDate", required = false) String startDate,
                                         @RequestParam(value = "endDate", required = false) String endDate,
                                         @RequestParam(value = "clientIp", required = false) String clientIp,
                                         @RequestParam(value = "action", required = false) Integer action,
                                         @RequestParam(value = "user", required = false) String username,
                                         @RequestParam(required = false) String orderType,
                                         @RequestParam(required = false) String orderValue) {
        TerminalVisit terminalVisit = new TerminalVisit();

        terminalVisit.setstartDate(startDate);
        terminalVisit.setendDate(endDate);
        terminalVisit.setIpAddress(IpUtils.ipToLong(clientIp));
        terminalVisit.setUsername(username);
        terminalVisit.setAction(action);

        String orderStr = null;
        if (!StringUtils.isEmpty(orderType) && !StringUtils.isEmpty(orderValue)) {
            orderStr = orderValue + " " + orderType;
        }

        startPage();
        List<TerminalVisit> visitList = statisticsService.getTerminalVisit(terminalVisit, orderStr);

        return AjaxResult.success(getDataTable(visitList));

    }

    @GetMapping("/visitCnt")
    @ApiOperation(value = "获取客户端访问次数列表", httpMethod = "GET")
    public AjaxResult getClientVisitCntList(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                            @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                            @RequestParam(value = "startDate", required = false) String startDate,
                                            @RequestParam(value = "endDate", required = false) String endDate,
                                            @RequestParam(value = "clientId", required = false) Long clientId,
                                            @RequestParam(value = "cameraId", required = false) Long cameraId,
                                            @RequestParam(value = "dstIp", required = false) String dstIp,
                                            @RequestParam(value = "srcIp", required = false) String srcIp,
                                            @RequestParam(value = "username", required = false) String username,
                                            @RequestParam(value = "dstCode", required = false) String dstCode,
                                            @RequestParam(value = "action", required = false) String action) throws Exception {

        OperationBehavior operationBehavior = new OperationBehavior();
        operationBehavior.setClientId(clientId);
        operationBehavior.setCameraId(cameraId);
        operationBehavior.setDstIp(IpUtils.ipToLong(dstIp));
        operationBehavior.setDstCode(dstCode);
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

        List<JSONObject> terminalVisitedCameraList = statisticsService.associateCameraInfo(operationBehaviorList);

        return AjaxResult.success(getDataTable(terminalVisitedCameraList, (Integer) data.get("total")));


    }

    @GetMapping("/relatedCamera")
    @ApiOperation(value = "获取客户端访问相关摄像头", httpMethod = "GET")
    public AjaxResult getClientVisitRelatedCameraList(@RequestParam(value = "clientId",required = false) Long clientId,
                                                  @RequestParam(value = "action", required = false) Integer action,
                                                  @RequestParam(value = "cameraCode", required = false) String cameraCode,
                                                  @RequestParam(value = "cameraName", required = false) String cameraName,
                                                  @RequestParam(value = "cameraIp", required = false) String cameraIp,
                                                  @RequestParam(value = "username", required = false) String username,
                                                  @RequestParam(value = "clientIp", required = false) String clientIp,
                                                  @RequestParam(value = "region[]", required = false) Integer region,
                                                  @RequestParam(value = "startDate", required = false) String startDate,
                                                  @RequestParam(value = "endDate", required = false) String endDate) {

        Statistics statistics = new Statistics();
        statistics.setAction(action);
        statistics.setDstCode(cameraCode);
        statistics.setUsername(username);
        statistics.setSrcIp(IpUtils.ipToLong(clientIp));
        statistics.setstartDate(startDate);
        statistics.setendDate(endDate);

        Camera camera = new Camera();
        camera.setDeviceName(cameraName);
        camera.setIpAddress(IpUtils.ipToLong(cameraIp));
        camera.setRegion(region);


        List<Statistics> statisticsList = statisticsService.getList(statistics);

        List<String> cameraCodeList = statisticsList.stream().map(f -> f.getDstCode()).collect(Collectors.toList());

        startPage();
        List<Camera> cameraList = cameraService.batchGetCameraByCode(cameraCodeList,camera);

        List<CameraVisit> cameraVisitList = new ArrayList<>();

        if(CollectionUtils.isEmpty(cameraList)){
            return AjaxResult.success(getDataTable(null));
        }

        cameraList.stream().forEach(f ->{

            Long sum = statisticsList.stream()
                    .filter(h -> h.getDstCode().equals(f.getDeviceCode()))
                    .mapToLong(h -> h.getCount())
                    .sum();

            CameraVisit cameraVisit = new CameraVisit();
            cameraVisit.setDeviceName(f.getDeviceName());
            cameraVisit.setDeviceCode(f.getDeviceCode());
            cameraVisit.setIpAddress(f.getIpAddress());
            cameraVisit.setRegionName(f.getRegionName());
            cameraVisit.setVisitCnt(sum);

            cameraVisitList.add(cameraVisit);
        });


        return AjaxResult.success(getDataTable(cameraVisitList,cameraList));

    }


}
