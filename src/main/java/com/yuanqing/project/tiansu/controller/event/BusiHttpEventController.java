package com.yuanqing.project.tiansu.controller.event;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.yuanqing.common.utils.DateUtils;
import com.yuanqing.common.utils.ip.IpUtils;
import com.yuanqing.framework.web.controller.BaseController;
import com.yuanqing.framework.web.domain.AjaxResult;
import com.yuanqing.framework.web.page.TableDataInfo;
import com.yuanqing.project.tiansu.domain.event.BusiHttpEvent;
import com.yuanqing.project.tiansu.service.event.IBusiHttpEventService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.beanutils.ConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;


/**
 * 接口告警Controller
 *
 * @author lvjingjing
 * @date 2021-05-13
 */
@Api(value = "接口告警", description = "接口告警Api")
@RestController
@RequestMapping("/api/httpEvent")
public class BusiHttpEventController extends BaseController {
    @Autowired
    private IBusiHttpEventService busiHttpEventService;

    /**
     * 查询接口告警列表
     */
    @GetMapping("/list")
    @ApiOperation(value = "获取接口告警列表", httpMethod = "GET")
    public TableDataInfo list(@ApiParam("开始时间") @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startDate,
                              @ApiParam("结束时间") @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endDate,
                              @ApiParam("访问地址") @RequestParam(value = "httpUrl", required = false) String httpUrl,
                              @ApiParam("终端IP") @RequestParam(value = "ipAddress", required = false) String ipAddress,
                              @ApiParam("事件来源") @RequestParam(value = "eventSource", required = false) String eventSource,
                              @ApiParam("接口执行状态") @RequestParam(value = "httpStatus", required = false) String httpStatus,
                              @ApiParam("告警等级") @RequestParam(value = "eventLevel", required = false) String eventLevel,
                              @ApiParam("告警状态") @RequestParam(value = "eventStatus", required = false) Integer eventStatus
    ) {
        BusiHttpEvent busiHttpEvent = new BusiHttpEvent();
        busiHttpEvent.setHttpUrl(httpUrl);
        busiHttpEvent.setIpAddress(IpUtils.ipToLong(ipAddress));
        busiHttpEvent.setHttpStatus(httpStatus);
        busiHttpEvent.setEventLevel(eventLevel);
        busiHttpEvent.setEventStatus(eventStatus);
        busiHttpEvent.setEventSource(eventSource);
        startPage();
        List<BusiHttpEvent> list = busiHttpEventService.selectBusiHttpEventList(busiHttpEvent, startDate, endDate);
        return getDataTable(list);
    }

    /**
     * 勾选部分告警，确认状态
     */
    @PutMapping("/editSome")
    @ApiOperation(value = "接口告警确认部分状态", httpMethod = "PUT")
    public AjaxResult editSome(@ApiParam("勾选的需要确认状态的接口告警数据的id") @RequestBody JSONObject jsonObject) {
        String str1 = String.valueOf(jsonObject.get("ids"));
        String str = str1.substring(1, str1.length() - 1);
        String[] strArr = str.split(",");

        Long[] ids = (Long[]) ConvertUtils.convert(strArr, Long.class);
        return toAjax(busiHttpEventService.updateBusiHttpEvent(ids));
    }

    /**
     * 确认所有告警的状态
     */
    @PutMapping("/editAll")
    @ApiOperation(value = "接口告警确认全部状态", httpMethod = "PUT")
    public AjaxResult editAll() {
        return toAjax(busiHttpEventService.updateAllBusiHttpEvent());
    }

    /**
     * 数据来源统计
     */
    @GetMapping("/getOfSources")
    @ApiOperation(value = "获取接口告警列表", httpMethod = "GET")
    public AjaxResult getOfSources(@ApiParam("日期类型") @RequestParam("timeType") String timeType) {
        Date endDate = new Date();
        Date startDate = DateUtils.getOfTimeType(timeType);
        List<Map<String, String>> map = busiHttpEventService.statisticsOfDataSources(startDate, endDate);
        return AjaxResult.success(map);
    }

    /**
     * 告警等级统计
     */
    @GetMapping("/getOfEventLevel")
    @ApiOperation(value = "获取接口告警列表", httpMethod = "GET")
    public AjaxResult getOfEventLevel(@ApiParam("日期类型") @RequestParam("timeType") String timeType) {
        Date endDate = new Date();
        Date startDate = DateUtils.getOfTimeType(timeType);
        List<Map<String, String>> list = busiHttpEventService.alarmLevelStatistics(startDate, endDate);

        //表示一般、重要、严重的等级都有数据，直接返回就可以；否则需要补0
        if (list.size() != 3) {
            list = makeUpPro(list,"一般");
            list = makeUpPro(list,"重要");
            list = makeUpPro(list,"严重");
        }
        return AjaxResult.success(list);
    }

    public List<Map<String, String>> makeUpPro(List<Map<String, String>> list,String level){
        if (!list.toString().contains(level)) {
            Map<String, String> map = new HashMap<>();
            map.put("eventLevel", level);
            map.put("proportion", "0");
            list.add(map);
        }
        return list;
    }


}
