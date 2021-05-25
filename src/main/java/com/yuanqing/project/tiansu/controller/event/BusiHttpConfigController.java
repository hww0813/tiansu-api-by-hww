package com.yuanqing.project.tiansu.controller.event;

import java.util.List;

import com.yuanqing.framework.web.controller.BaseController;
import com.yuanqing.framework.web.domain.AjaxResult;
import com.yuanqing.project.tiansu.domain.event.BusiHttpConfig;
import com.yuanqing.project.tiansu.service.event.IBusiHttpConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 接口告警阈值配置Controller
 *
 * @author lvjingjing
 * @date 2021-05-12
 */
@RestController
@RequestMapping("/api/httpConfig")
@Api(value = "接口告警阈值配置接口", description = "接口告警阈值配置相关API")
public class BusiHttpConfigController extends BaseController {
    @Autowired
    private IBusiHttpConfigService busiHttpConfigService;

    /**
     * 查询接口告警阈值配置列表
     */
    @GetMapping("/list")
    @ApiOperation(value = "查询接口告警阈值配置列表" , httpMethod = "GET")
    public AjaxResult list(@ApiParam("接口告警") @RequestBody BusiHttpConfig busiHttpConfig) {
        List<BusiHttpConfig> list = busiHttpConfigService.selectBusiHttpConfigList(busiHttpConfig);
        return AjaxResult.success(list);
    }

    /**
     * 修改接口告警阈值配置
     */
    @PutMapping("/edit")
    @ApiOperation(value = "修改接口告警阈值配置" , httpMethod = "PUT")
    public AjaxResult edit(@ApiParam("接口告警") @RequestBody BusiHttpConfig busiHttpConfig) {
        return toAjax(busiHttpConfigService.updateBusiHttpConfig(busiHttpConfig));
    }

}
