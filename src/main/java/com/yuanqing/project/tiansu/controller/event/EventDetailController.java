package com.yuanqing.project.tiansu.controller.event;

import com.yuanqing.framework.web.domain.AjaxResult;
import com.yuanqing.project.tiansu.domain.event.Event;
import com.yuanqing.project.tiansu.service.event.EventManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

@RestController
@RequestMapping(value = "/api/eventDetail")
@CrossOrigin
@Api(value = "告警事件详细", description = "告警事件详细相关Api")
public class EventDetailController{
    @Resource
    private EventManager eventManager;

    @GetMapping(value = "/list")
    @ApiOperation(value = "获取告警事件列表", httpMethod = "GET")
    public AjaxResult getAll(@RequestParam(value = "id", required = false) Long id) {
        Event event = eventManager.findById(id);
        return AjaxResult.success(event);
    }

}
