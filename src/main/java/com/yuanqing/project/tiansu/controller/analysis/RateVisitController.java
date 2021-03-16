package com.yuanqing.project.tiansu.controller.analysis;

import com.yuanqing.common.utils.DateUtils;
import com.yuanqing.common.utils.ip.IpUtils;
import com.yuanqing.framework.redis.RedisCache;
import com.yuanqing.framework.web.controller.BaseController;
import com.yuanqing.framework.web.domain.AjaxResult;
import com.yuanqing.project.tiansu.domain.analysis.CameraVisit;
import com.yuanqing.project.tiansu.domain.assets.Camera;
import com.yuanqing.project.tiansu.domain.assets.ClientTerminal;
import com.yuanqing.project.tiansu.domain.macs.MacsRegion;
import com.yuanqing.project.tiansu.service.analysis.IStatisticsService;
import com.yuanqing.project.tiansu.service.assets.ICameraService;
import com.yuanqing.project.tiansu.service.assets.IClientService;
import com.yuanqing.project.tiansu.service.assets.IClientTerminalService;
import com.yuanqing.project.tiansu.service.macs.IMacsConfigService;
import io.swagger.annotations.ApiOperation;
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
    private IClientService clientService;


    @GetMapping(value = "/list")
    @ApiOperation(value = "首页访问率查询", httpMethod = "GET")
    public AjaxResult rateList(@RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                               @RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
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
    public AjaxResult getVisitedCntList(Camera camera) {

        CameraVisit cameraVisit = new CameraVisit();

        cameraVisit.setstartDate(camera.getstartDate());
        cameraVisit.setendDate(camera.getendDate());

        camera.setstartDate(null);
        camera.setendDate(null);

        List<Camera> cameraList = cameraService.getList(camera);

        List<String> cameraCodeList = statisticsService.getCameraVisited(cameraList, cameraVisit);

        startPage();

        List<Camera> finalCameraList = cameraService.batchGetCameraByCode(cameraCodeList, null);

        return AjaxResult.success(getDataTable(finalCameraList));

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
