package com.yuanqing.project.tiansu.controller.event;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yuanqing.common.utils.StringUtils;
import com.yuanqing.common.utils.http.HttpUtils;
import com.yuanqing.framework.redis.RedisCache;
import com.yuanqing.framework.web.domain.AjaxResult;
import com.yuanqing.framework.web.domain.PageResult;
import com.yuanqing.project.tiansu.domain.event.Event;
import com.yuanqing.project.tiansu.service.event.EventManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


import static com.yuanqing.common.constant.Constants.ALARM_CAMERA_COUNTS_CACHE;

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
@Api(value = "告警事件接口", description = "告警事件相关API")
public class EventController {


    @Value("${tiansu.alarmhost}")
    private String alarmHost;

    @Autowired
    private RedisCache redisCache;

    @Resource
    private EventManager eventManager;

    @GetMapping(value = "/list")
    @ApiOperation(value = "获取告警事件列表", httpMethod = "GET")
    public PageResult getAll(@ApiParam("页码数")@RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
                             @ApiParam("行数")@RequestParam(name = "pageSize", defaultValue = "20") int pageSize,
                             @ApiParam("开始时间")@RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime stime,
                             @ApiParam("结束时间")@RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime etime,
                             @ApiParam("事件来源")@RequestParam(value = "eventSource", required = false) String eventSource,
                             @RequestParam(value = "strategyName", required = false) String strategyName,
                             @ApiParam("状态")@RequestParam(value = "status", required = false) String status,
                             @ApiParam("事件类型")@RequestParam(value = "eventCategory", required = false) String eventCategory,
                             @ApiParam("告警等级")@RequestParam(value = "eventLevel", required = false) String eventLevel,
                             @ApiParam("终端IP")@RequestParam(value = "clientIp", required = false) String clientIp,
                             @ApiParam("摄像头名称")@RequestParam(value = "cameraName", required = false) String cameraName,
                             @ApiParam("内容")@RequestParam(value = "content", required = false) String content,
                             @RequestParam(value = "eventSubject", required = false) String eventSubject,
                             @ApiParam("规则名")@RequestParam(value = "ruleName", required = false) String ruleName,
                             @ApiParam("动作类型")@RequestParam(value = "action", required = false) String action,
                             @ApiParam("ID")@RequestParam(value = "id", required = false) Long id,
                             @ApiParam("联接类型")@RequestParam(value = "connectType", required = false) String connectType,
                             @ApiParam("排序")@RequestParam(required = false) String orderType,
                             @ApiParam("排序对象")@RequestParam(required = false) String orderValue) {
        JSONObject filters = new JSONObject();
        if (stime != null) {
            filters.put("startTime", stime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
        if (etime != null) {
            filters.put("endTime", etime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
        filters.put("eventSource", eventSource);
//        filters.put("strategyName", strategyName);
        filters.put("status", status);
        filters.put("eventCategory", eventCategory);
        filters.put("eventLevel", eventLevel);
        filters.put("clientIp", clientIp);
//        filters.put("cameraName", cameraName);
//        filters.put("content", content);
//        filters.put("eventSubject", eventSubject);
//        filters.put("ruleName", ruleName);
        filters.put("action", action);
        filters.put("id", id);
//        filters.put("connectType", connectType);
        if (StringUtils.isNotBlank(orderType) && StringUtils.isNotBlank(orderValue)) {
            filters.put("orderType", orderValue + " " + orderType);
        }
        String url = alarmHost + "/BusiEvent/listT?pageSize=" + pageSize + "&pageNum=" + pageNum;
        String result = HttpUtils.sendGet(url, filters);
        JSONObject resultObj = JSONObject.parseObject(result);
        JSONArray datas = resultObj.getJSONArray("rows");
        Integer total = resultObj.getInteger("total");
        return PageResult.success(datas, pageSize, pageNum, total);
    }


    @GetMapping("/getEventDatas")
    @ApiOperation(value = "获取用户数据", httpMethod = "GET")
    public AjaxResult getUserDatas() {
        return AjaxResult.success("success", redisCache.getCacheObject(ALARM_CAMERA_COUNTS_CACHE));
    }

    @PutMapping
    @ApiOperation(value = "更新一个事件", httpMethod = "PUT")
    public AjaxResult putEvent(@ApiParam("告警事件")@Valid @RequestBody JSONObject jsonObject) {
        String str = String.valueOf(jsonObject.get("id"));
        List<Event> list = new ArrayList<Event>();
        Event event = new Event();
        event.setId(Long.valueOf(str.trim()));
        list.add(event);
        eventManager.batchChangStatus(list);
        return AjaxResult.success();
    }

    @PutMapping("/updateStatus")
    @ApiOperation(value = "批量确认告警事件", httpMethod = "PUT")
    public AjaxResult updateStatus(@ApiParam("勾选的告警事件ID")@Valid @RequestBody JSONObject jsonObject) {
        String str1 = String.valueOf(jsonObject.get("id"));
        String str = str1.substring(1, str1.length() - 1);
        List<Event> list = new ArrayList<Event>();
        String[] array = str.split(",");
        for (String i : array) {
            Event event = new Event();
            event.setId(Long.valueOf(i.trim()));
            list.add(event);
        }
        eventManager.batchChangStatus(list);
        return AjaxResult.success();
    }

}
