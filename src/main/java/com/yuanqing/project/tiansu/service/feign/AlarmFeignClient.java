package com.yuanqing.project.tiansu.service.feign;

import com.yuanqing.framework.web.domain.AjaxResult;
import com.yuanqing.project.tiansu.domain.event.Event;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;


/**
 * @author lvjingjing
 * @date 2021/6/25 10:13
 */
@FeignClient(value = "alarm-root")
public interface AlarmFeignClient {

    /**
     * 根据告警事件的eventId查询cameraId
     */
    @GetMapping("/alarm/BusiEvent/getCameraId")
    String getCameraId(@RequestParam("event_id") Long event_id);

    /**
     * 获取活跃告警事件次数
     */
    @GetMapping("/alarm/BusiEvent/getEventActive")
    String getEventActive();

    /**
     * 查询最近一段时间内每分钟的的告警数量
     * @param intervalMinutes 最近分钟数
     */
    @GetMapping("/alarm/analysisEvent/queryEventCntPerMinute")
    String queryEventCntPerMinute(@RequestParam("intervalMinutes") Integer intervalMinutes);

    /**
     * 获取告警事件详细信息和uuid
     */
    @GetMapping(value = "/alarm/BusiEvent/getTInfo")
    String detailAndTOperUuid(@RequestParam("event_id") Long event_id) ;

    /**
     * 批量确认告警事件
     */
    @PutMapping("/alarm/BusiEvent/{ids}")
    AjaxResult confirm(@PathVariable("ids") Long[] ids);

    /**
     * 查询告警事件列表
     */
    @GetMapping("/alarm/BusiEvent/listT")
    String listT(@RequestBody Event event,@RequestParam(value = "pageSize") Integer pageSize,@RequestParam(value = "pageNum") Integer pageNum);

    @GetMapping("/alarm/BusiEvent/getUuidListByEventId")
    AjaxResult getUuidListByEventId(@RequestParam(value = "eventId") long eventId, @RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
                                    @RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate);
}
