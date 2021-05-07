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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.CollectionUtils;
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

        // TODO: 时间范围没有传参
        startPage();
        List<Camera> cameraList = cameraService.getAllList(camera);

        // TODO: 状态还是数字，需要转换
        return AjaxResult.success(getDataTable(cameraList));

    }

    @GetMapping(value = "/visitedCnt")
    @ApiOperation(value = "获取访问分析列表", httpMethod = "GET")
    public AjaxResult getVisitedCntList(@RequestParam(value = "cameraIp", required = false) String cameraIp,
                                        Camera camera) {

        //设置时间查询范围
        CameraVisit cameraVisit = new CameraVisit();

        cameraVisit.setstartDate(camera.getstartDate());
        cameraVisit.setendDate(camera.getendDate());

        camera.setstartDate(null);
        camera.setendDate(null);
        camera.setIpAddress(IpUtils.ipToLong(cameraIp));

        //先根据region和筛选条件，查询符合条件的camera
        List<Camera> cameraList = cameraService.getAllList(camera);
        //根据camera筛选出的device_code和statistics根据时间筛选出的dst_code，筛选device_code
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
                                      @RequestParam(value = "dstCode", required = false) String deviceCode,
                                      @RequestParam(value = "username", required = false) String username,
                                      @RequestParam(value = "dstDeviceName", required = false) String dstDeviceName,
                                      @RequestParam(value = "startDate") String startDate,
                                      @RequestParam(value = "endDate") String endDate) throws Exception {

        // 从 访问率报表 -> 下级地区访问率 -> 访问次数过来时，会只传地区字段
        List<String> cameraCodeList = null;
        if (region != null) {
            Camera camera = new Camera();
            camera.setRegion(region);
            List<Camera> cameraList = cameraService.getAllList(camera);

            //设置时间查询范围
            CameraVisit cameraVisit = new CameraVisit();
            cameraVisit.setstartDate(startDate);
            cameraVisit.setendDate(endDate);

            cameraCodeList = statisticsService.getCameraVisited(cameraList, cameraVisit);

            if(CollectionUtils.isEmpty(cameraCodeList)){
                return PageResult.success();
            }
        }

        OperationBehavior operationBehavior = new OperationBehavior();
        operationBehavior.setNum(pageNum);
        operationBehavior.setSize(pageSize);
        operationBehavior.setSrcIp(IpUtils.ipToLong(srcIp));
        // 从摄像头被访问 -> 相关终端数 -> 访问次数 过来时，会传摄像头编码
        operationBehavior.setDstCode(deviceCode);
        operationBehavior.setUsername(username);
        operationBehavior.setDstDeviceName(dstDeviceName);
        operationBehavior.setAction(action);
        operationBehavior.setDstIp(IpUtils.ipToLong(dstIp));
        operationBehavior.setstartDate(startDate);
        operationBehavior.setendDate(endDate);

        PageResult visitedRateRelatedOperation = operationBehaviorService.getVisitedRateRelatedOperation(cameraCodeList, operationBehavior);

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

        List<Camera> cameraList = cameraService.getAllList(camera);

        List<Long> terminalIpList = statisticsService.getTerminalVisited(cameraList, clientTerminal);

        startPage();
        List<ClientTerminal> clientTerminalList = clientTerminalService.getTerminalByIpList(terminalIpList, clientTerminal);

        return AjaxResult.success(getDataTable(clientTerminalList));

    }


}
