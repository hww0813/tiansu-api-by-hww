package com.yuanqing.project.tiansu.controller.tripartite;

import com.alibaba.fastjson.JSONObject;
import com.yuanqing.common.utils.StringUtils;
import com.yuanqing.framework.web.domain.AjaxResult;
import com.yuanqing.project.tiansu.domain.macs.MacsConfig;
import com.yuanqing.project.tiansu.mapper.operation.OperationBehaviorMapper;
import com.yuanqing.project.tiansu.service.assets.IClientService;
import com.yuanqing.project.tiansu.service.assets.IServerTreeService;
import com.yuanqing.project.tiansu.service.macs.IMacsConfigService;
import com.yuanqing.project.tiansu.service.operation.IOperationBehaviorService;
import com.yuanqing.project.tiansu.service.operation.impl.OperationBehaviorServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Created by xucan on 2020-12-07 11:43
 * @author xucan
 */

@RestController
@RequestMapping(value = "/tripartite/alarm")
@CrossOrigin
@Api(value = "告警接口", description = "告警服务相关接口")
public class AlarmController {


    @Resource
    private IClientService clientService;

    @Resource
    private IServerTreeService serverTreeService;

    @Resource
    private IMacsConfigService macsConfigService;

    @Resource
    private IOperationBehaviorService operationBehaviorService;

    @GetMapping("/clientById")
    public AjaxResult clientById(@ApiParam("客户端ID")@Valid @RequestParam(value = "id") Long id) {
        if (id == null) {
            return AjaxResult.error();
        }
        return AjaxResult.success(clientService.findById(id));
    }

    @GetMapping("/serverByIP")
    public AjaxResult deleteSipClient(@ApiParam("IP地址")@Valid @RequestParam(value = "ipAddress") Long ipAddress) {
        if (ipAddress == null) {
            return AjaxResult.error();
        }
        return AjaxResult.success(serverTreeService.findOne(ipAddress));
    }

    @GetMapping("/getRegion")
    public AjaxResult getRegion() {
        return AjaxResult.success(macsConfigService.getRegionList());
    }

    @GetMapping("/getLoginIpsCount")
    @ApiOperation(value = "获取天数范围内用户登陆的ip数量", httpMethod = "GET")
    public AjaxResult getLoginIpsCount(@ApiParam("用户名")@RequestParam(value = "username") String username,
                                       @ApiParam("天数")@RequestParam(value = "days") Integer days) throws ExecutionException, InterruptedException {
        JSONObject filters = new JSONObject();
        filters.put("username", username);

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String startTime = now.minus(days, ChronoUnit.DAYS).format(dtf);
        String endTime = now.format(dtf);

        filters.put("startDate", startTime);
        filters.put("endDate",  endTime);

        //总量
        CompletableFuture<Integer> count = CompletableFuture.supplyAsync(() -> operationBehaviorService.selectOperationByTimeAndName(filters));
        return AjaxResult.success(count.get());
    }

    @GetMapping("/getLoginIps")
    @ApiOperation(value = "获取天数范围内用户登陆的ip", httpMethod = "GET")
    public AjaxResult getLoginIps(@ApiParam("天数")@RequestParam(value = "days") Integer days) {
        JSONObject filters = new JSONObject();

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String startTime = now.minus(days, ChronoUnit.DAYS).format(dtf);
        String endTime = now.format(dtf);

        filters.put("startDate", startTime);
        filters.put("endDate",  endTime);
        List<String> usernames = operationBehaviorService.selectOperationNamesByTime(filters);

        Map<String, List<Long>> result = new HashMap<>();
        for(String username : usernames) {
            if (StringUtils.isEmpty(username)) {
                continue;
            }
            filters.put("username", username);
            List<Long> ips =  operationBehaviorService.selectOperationIpsByTimeAndName(filters);
            result.put(username, ips);
        }

        return AjaxResult.success(result);
    }

    @GetMapping("/getOperRatio")
    @ApiOperation(value = "获取下班操作数与上班操作数的比值", httpMethod = "GET")
    public AjaxResult getOperRatio(@ApiParam("上班开始时间") @RequestParam(value = "startDate") @DateTimeFormat(pattern = "HH:mm:ss") LocalTime startDate,
                                   @ApiParam("下班时间") @RequestParam(value = "endDate") @DateTimeFormat(pattern = "HH:mm:ss") LocalTime endDate) {

        String startStr = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE) + " " + startDate.format(DateTimeFormatter.ISO_LOCAL_TIME);
        String endStr = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE) + " " + endDate.format(DateTimeFormatter.ISO_LOCAL_TIME);

//        Integer workTimeNum = operationBehaviorMapper.getOperNumByTime(startStr, endStr);
        Long closeingTimeNum = operationBehaviorService.getOperNumByTime(endStr, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        Long preTimeNum = operationBehaviorService.getOperNumByTime(getTodayStartTime(), startStr);

//        if (workTimeNum != 0) {
//            double ratio = (preTimeNum + closeingTimeNum) * 1.0 / workTimeNum;
//            return AjaxResult.success(ratio);
//        } else {
//            return AjaxResult.success(0);
//        }

        return AjaxResult.success(preTimeNum + closeingTimeNum);
    }

    private String getTodayStartTime() {
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
        return formater.format(new Date())+ " 00:00:00";
    }

}
