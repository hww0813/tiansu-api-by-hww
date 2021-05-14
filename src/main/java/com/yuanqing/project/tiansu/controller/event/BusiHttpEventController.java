package com.yuanqing.project.tiansu.controller.event;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yuanqing.common.utils.DateUtils;
import com.yuanqing.common.utils.ip.IpUtils;
import com.yuanqing.framework.web.controller.BaseController;
import com.yuanqing.framework.web.domain.AjaxResult;
import com.yuanqing.framework.web.page.TableDataInfo;
import com.yuanqing.project.tiansu.domain.event.BusiHttpEvent;
import com.yuanqing.project.tiansu.service.event.IBusiHttpEventService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;


/**
 * 接口告警Controller
 *
 * @author lvjingjing
 * @date 2021-05-13
 */
@RestController
@RequestMapping("/api/httpEvent")
public class BusiHttpEventController extends BaseController {
    @Autowired
    private IBusiHttpEventService busiHttpEventService;

    /**
     * 查询接口告警列表
     */
    @GetMapping("/list")
    @ApiOperation(value = "获取接口告警列表" , httpMethod = "GET")
    public TableDataInfo list(@RequestParam(value = "startDate",required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startDate,
                              @RequestParam(value = "endDate",required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endDate,
                              @RequestParam(value = "httpUrl",required = false)String httpUrl,
                              @RequestParam(value = "ipAddress",required = false)String ipAddress,
                              @RequestParam(value = "eventSource",required = false)String eventSource,
                              @RequestParam(value = "httpStatus",required = false)String httpStatus,
                              @RequestParam(value = "eventLevel",required = false)String eventLevel,
                              @RequestParam(value = "eventStatus",required = false)Integer eventStatus
                              ) {
        BusiHttpEvent busiHttpEvent = new BusiHttpEvent();
        busiHttpEvent.setHttpUrl(httpUrl);
        busiHttpEvent.setIpAddress(IpUtils.ipToLong(ipAddress));
        busiHttpEvent.setEventSource(eventSource);
        busiHttpEvent.setHttpStatus(httpStatus);
        busiHttpEvent.setEventLevel(eventLevel);
        busiHttpEvent.setEventStatus(eventStatus);
        startPage();
        List<BusiHttpEvent> list = busiHttpEventService.selectBusiHttpEventList(busiHttpEvent, startDate, endDate);
        return getDataTable(list);
    }

    /**
     * 勾选部分告警，确认状态
     */
    @PutMapping("/editSome")
    @ApiOperation(value = "接口告警确认部分状态" , httpMethod = "PUT")
    public AjaxResult editSome(@RequestParam("ids") Long[] ids) {
        return toAjax(busiHttpEventService.updateBusiHttpEvent(ids));
    }

    /**
     * 确认所有告警的状态
     */
    @PutMapping("/editAll")
    @ApiOperation(value = "接口告警确认全部状态" , httpMethod = "PUT")
    public AjaxResult editAll() {
        return toAjax(busiHttpEventService.updateAllBusiHttpEvent());
    }

    /**
     * 数据来源统计
     */
    @GetMapping("/getOfSources")
    @ApiOperation(value = "获取接口告警列表" , httpMethod = "GET")
    public AjaxResult getOfSources(@RequestParam("timeType") String timeType) {
        Date endDate = new Date();
        Date startDate = DateUtils.getOfTimeType(timeType);
        List<Map<String, String>> map = busiHttpEventService.statisticsOfDataSources(startDate, endDate);
        return AjaxResult.success(map);
    }

    /**
     * 告警等级统计
     */
    @GetMapping("/getOfEventLevel")
    @ApiOperation(value = "获取接口告警列表" , httpMethod = "GET")
    public AjaxResult getOfEventLevel(@RequestParam("timeType") String timeType) {
        Date endDate = new Date();
        Date startDate = DateUtils.getOfTimeType(timeType);
        List<Map<String, String>> list = busiHttpEventService.alarmLevelStatistics(startDate, endDate);

        //表示一般、重要、严重的等级都有数据，直接返回就可以；否则需要补0
        if (list.size() < 3) {
            Map<String, String> map = new HashMap<>();
            String level = null;
            if (!list.contains("一般")) {
                level = "一般";
            } else if (!list.contains("重要")) {
                level = "重要";
            } else if (!list.contains("严重")) {
                level = "严重";
            }
            map.put("eventLevel" , level);
            map.put("proportion" , "0");
            list.add(map);
        }
        return AjaxResult.success(list);
    }


}
