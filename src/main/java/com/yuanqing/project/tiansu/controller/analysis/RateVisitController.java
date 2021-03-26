package com.yuanqing.project.tiansu.controller.analysis;

import com.yuanqing.common.utils.DateUtils;
import com.yuanqing.common.utils.ip.IpUtils;
import com.yuanqing.framework.redis.RedisCache;
import com.yuanqing.framework.web.controller.BaseController;
import com.yuanqing.framework.web.domain.AjaxResult;
import com.yuanqing.framework.web.domain.PageResult;
import com.yuanqing.project.tiansu.domain.analysis.CameraVisit;
import com.yuanqing.project.tiansu.domain.assets.Camera;
import com.yuanqing.project.tiansu.domain.assets.ClientTerminal;
import com.yuanqing.project.tiansu.domain.macs.MacsRegion;
import com.yuanqing.project.tiansu.domain.operation.OperationBehavior;
import com.yuanqing.project.tiansu.service.analysis.IStatisticsService;
import com.yuanqing.project.tiansu.service.assets.ICameraService;
import com.yuanqing.project.tiansu.service.assets.IClientService;
import com.yuanqing.project.tiansu.service.assets.IClientTerminalService;
import com.yuanqing.project.tiansu.service.macs.IMacsConfigService;
import com.yuanqing.project.tiansu.service.operation.IOperationBehaviorService;
import io.swagger.annotations.ApiOperation;
import org.aspectj.weaver.loadtime.Aj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

import static com.yuanqing.common.constant.Constants.INDEX_VISITED_RATE_CACHE;

/**
 * @author Dong.Chao
 * @Classname RateVisitController
 * @Description 首页摄像头访问率分析
 * @Date 2021/3/10 9:59
 * @Version V1.0
 */
@RestController
@RequestMapping(value = "/api/analysis/visit/rate")
public class RateVisitController extends BaseController {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private IMacsConfigService macsConfigService;

    @Autowired
    private ICameraService cameraService;

    @Autowired
    private IStatisticsService statisticsService;

    @Autowired
    private IClientTerminalService clientTerminalService;

    @Autowired
    private IOperationBehaviorService operationBehaviorService;

    @Autowired
    private IClientService clientService;


    @GetMapping(value = "/list")
    @ApiOperation(value = "首页访问率查询", httpMethod = "GET")
    public AjaxResult rateList(@RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDate startDate,
                               @RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDate endDate,
                               @RequestParam(value = "pageNum", required = false) String pageNum,
                               @RequestParam(value = "pageSize", required = false) String pageSize) {
        String time = DateUtils.getTimeType(startDate, endDate);
        return AjaxResult.success("success", redisCache.getCacheObject(INDEX_VISITED_RATE_CACHE + "_" + time));
    }

    @GetMapping(value = "/region")
    @ApiOperation(value = "首页访问率查询", httpMethod = "GET")
    public AjaxResult getRegion() {
        MacsRegion region = macsConfigService.getRegion(null);
        return AjaxResult.success(region);

    }

    @GetMapping(value = "/cameraCnt")
    @ApiOperation(value = "获取访问率相关摄像头列表", httpMethod = "GET")
    public AjaxResult getCameraCntList(Camera camera) {

        startPage();
        List<Camera> cameraList = cameraService.getList(camera);

        return AjaxResult.success(getDataTable(cameraList));

    }

    @GetMapping(value = "/visitedCnt")
    @ApiOperation(value = "获取访问分析列表", httpMethod = "GET")
    public AjaxResult getVisitedCntList(@RequestParam(value = "cameraIp",required = false) String cameraIp,
                                        Camera camera) {

        CameraVisit cameraVisit = new CameraVisit();

        cameraVisit.setstartDate(camera.getstartDate());
        cameraVisit.setendDate(camera.getendDate());

        camera.setstartDate(null);
        camera.setendDate(null);
        camera.setIpAddress(IpUtils.ipToLong(cameraIp));

        List<Camera> cameraList = cameraService.getList(camera);

        List<String> cameraCodeList = statisticsService.getCameraVisited(cameraList, cameraVisit);

        startPage();

        List<Camera> finalCameraList = cameraService.batchGetCameraByCode(cameraCodeList, new Camera());

        return AjaxResult.success(getDataTable(finalCameraList));

    }

    @GetMapping(value = "/visitCnt")
    @ApiOperation(value = "获取访问分析列表", httpMethod = "GET")
    public PageResult getVisitCntList(@RequestParam(name = "pageNum", required = false, defaultValue = "1") int pageNum,
                              @RequestParam(name = "pageSize", required = false, defaultValue = "20") int pageSize,
                              @RequestParam(value = "cityCode", required = false) Integer region,
                              @RequestParam(value = "srcIp", required = false) String srcIp,
                              @RequestParam(value = "dstIp", required = false) String dstIp,
                              @RequestParam(value = "action", required = false) String action,
                              @RequestParam(value = "deviceCode", required = false) String deviceCode,
                              @RequestParam(value = "username", required = false) String username,
                              @RequestParam(value = "startDate") String startDate,
                              @RequestParam(value = "endDate") String  endDate) throws Exception {

        // TODO: 没看懂这里是干嘛用的？还搞一个cameraCodeList？
//        Camera camera = new Camera();
//        camera.setRegion(region);
//        List<Camera> cameraList = cameraService.getList(camera);
//
//        List<String> cameraCodeList = statisticsService.getCameraVisited(cameraList, new CameraVisit());

        OperationBehavior operationBehavior = new OperationBehavior();
        operationBehavior.setNum(pageNum);
        operationBehavior.setSize(pageSize);
        operationBehavior.setSrcIp(IpUtils.ipToLong(srcIp));
        operationBehavior.setDstCode(deviceCode);
        operationBehavior.setUsername(username);
        operationBehavior.setAction(action);
        operationBehavior.setDstIp(IpUtils.ipToLong(dstIp));
        operationBehavior.setstartDate(startDate);
        operationBehavior.setendDate(endDate);

        PageResult visitedRateRelatedOperation = operationBehaviorService.getVisitedRateRelatedOperation(null, operationBehavior);

        return visitedRateRelatedOperation;


    }

    @GetMapping(value = "/clientCnt")
    @ApiOperation(value = "获取访问分析列表", httpMethod = "GET")
    public AjaxResult getClientCntPage(@RequestParam(value = "cityCode", required = false) Integer region,
                                       @RequestParam(value = "status", required = false) Integer status,
                                       @RequestParam(value = "ipAddress", required = false) String ipAddress,
                                       @RequestParam(value = "startDate") String startDate,
                                       @RequestParam(value = "endDate") String endDate) {
        Camera camera = new Camera();
        camera.setRegion(region);

        ClientTerminal clientTerminal = new ClientTerminal();
        clientTerminal.setStatus(status);
        clientTerminal.setIpAddress(IpUtils.ipToLong(ipAddress));
        clientTerminal.setstartDate(startDate);
        clientTerminal.setendDate(endDate);

        List<Camera> cameraList = cameraService.getList(camera);

        List<Long> terminalIpList = statisticsService.getTerminalVisited(cameraList, clientTerminal);

        startPage();
        List<ClientTerminal> clientTerminalList = clientTerminalService.getTerminalByIpList(terminalIpList, clientTerminal);

        return AjaxResult.success(getDataTable(clientTerminalList));

    }


}
