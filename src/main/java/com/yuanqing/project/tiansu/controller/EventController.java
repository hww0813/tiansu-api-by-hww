package com.yuanqing.project.tiansu.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yuanqing.common.utils.StringUtils;
import com.yuanqing.common.utils.http.HttpUtils;
import com.yuanqing.framework.web.domain.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Dong.Chao
 * @Classname EventController
 * @Description
 * @Date 2021/2/26 14:34
 * @Version V1.0
 */

@RestController
@RequestMapping(value = "/api/event")
@CrossOrigin
@Api(value = "告警事件")
public class EventController {


    @Value("${tiansu.alarmhost}")
    private String alarmHost;

    @GetMapping(value = "/list")
    @ApiOperation(value = "获取告警事件列表", httpMethod = "GET")
    public PageResult getAll(@RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
                             @RequestParam(name = "pageSize", defaultValue = "20") int pageSize,
                             @RequestParam(value = "stime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime stime,
                             @RequestParam(value = "etime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime etime,
                             @RequestParam(value = "eventSource", required = false) String eventSource,
                             @RequestParam(value = "strategyName", required = false) String strategyName,
                             @RequestParam(value = "status", required = false) String status,
                             @RequestParam(value = "eventCategory", required = false) String eventCategory,
                             @RequestParam(value = "eventLevel", required = false) String eventLevel,
                             @RequestParam(value = "clientIp", required = false) String clientIp,
                             @RequestParam(value = "cameraName", required = false) String cameraName,
                             @RequestParam(value = "content", required = false) String content,
                             @RequestParam(value = "eventSubject", required = false) String eventSubject,
                             @RequestParam(value = "ruleName", required = false) String ruleName,
                             @RequestParam(value = "action", required = false) String action,
                             @RequestParam(value = "id", required = false) Long id,
                             @RequestParam(value = "connectType", required = false) String connectType,
                             @RequestParam(required = false) String orderType,
                             @RequestParam(required = false) String orderValue) {
        JSONObject filters = new JSONObject();
        if (stime != null) {
            filters.put("startDate", stime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
        if (etime != null) {
            filters.put("endDate", etime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
        filters.put("eventSource", eventSource);
        filters.put("strategyName", strategyName);
        filters.put("status", status);
        filters.put("eventCategory", eventCategory);
        filters.put("eventLevel", eventLevel);
        filters.put("clientIp", clientIp);
        filters.put("cameraName", cameraName);
        filters.put("content", content);
        filters.put("eventSubject", eventSubject);
        filters.put("ruleName", ruleName);
        filters.put("action", action);
        filters.put("id", id);
        filters.put("connectType", connectType);
        if (StringUtils.isNotBlank(orderType) && StringUtils.isNotBlank(orderValue)) {
            filters.put("orderType", orderValue + " " + orderType);
        }
        String url = alarmHost+"/BusiEvent/listT?pageSize="+pageSize+"&pageNum="+pageNum;
        String result = HttpUtils.sendGet(url,filters);
        JSONObject resultObj = JSONObject.parseObject(result);
        JSONArray datas = resultObj.getJSONArray("rows");
        Integer total = resultObj.getInteger("total");
        return PageResult.success(datas,pageSize,pageNum,total);
    }
}