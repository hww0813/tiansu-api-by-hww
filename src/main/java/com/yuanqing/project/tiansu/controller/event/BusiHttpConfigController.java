package com.yuanqing.project.tiansu.controller.event;

import java.util.List;

import com.yuanqing.framework.web.controller.BaseController;
import com.yuanqing.framework.web.domain.AjaxResult;
import com.yuanqing.project.tiansu.domain.event.BusiHttpConfig;
import com.yuanqing.project.tiansu.service.event.IBusiHttpConfigService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 接口告警阈值配置Controller
 *
 * @author lvjingjing
 * @date 2021-05-12
 */
@RestController
@RequestMapping("/api/httpConfig")
public class BusiHttpConfigController extends BaseController {
    @Autowired
    private IBusiHttpConfigService busiHttpConfigService;

    /**
     * 查询接口告警阈值配置列表
     */
    @GetMapping("/list")
    @ApiOperation(value = "查询接口告警阈值配置列表" , httpMethod = "GET")
    public AjaxResult list(BusiHttpConfig busiHttpConfig) {
        List<BusiHttpConfig> list = busiHttpConfigService.selectBusiHttpConfigList(busiHttpConfig);
        return AjaxResult.success(list);
    }

    /**
     * 修改接口告警阈值配置
     */
    @PutMapping("/edit")
    @ApiOperation(value = "修改接口告警阈值配置" , httpMethod = "PUT")
    public AjaxResult edit(BusiHttpConfig busiHttpConfig) {
        return toAjax(busiHttpConfigService.updateBusiHttpConfig(busiHttpConfig));
    }

}
