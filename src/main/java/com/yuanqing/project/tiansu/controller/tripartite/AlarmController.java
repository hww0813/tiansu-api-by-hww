package com.yuanqing.project.tiansu.controller.tripartite;

import com.yuanqing.framework.web.domain.AjaxResult;
import com.yuanqing.project.tiansu.domain.macs.MacsConfig;
import com.yuanqing.project.tiansu.service.assets.IClientService;
import com.yuanqing.project.tiansu.service.assets.IServerTreeService;
import com.yuanqing.project.tiansu.service.macs.IMacsConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

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

}
