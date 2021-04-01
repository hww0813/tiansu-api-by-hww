package com.yuanqing.project.tiansu.controller.authorize;

import com.alibaba.fastjson.JSONObject;
import com.yuanqing.framework.web.domain.AjaxResult;
import com.yuanqing.project.tiansu.service.authorize.IAuthorizeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author xucan
 * @version 1.0
 * @Date 2021-04-01 10:03
 */

@RestController
@RequestMapping("/api/authorize")
@Api(value = "认证授权", description = "认证授权API")
@CrossOrigin
public class AuthorizeController {

    @Autowired
    private IAuthorizeService authorizeService;

    @GetMapping("/check")
    @ApiOperation(value = "获取许可数状态", httpMethod = "GET")
    public AjaxResult getCheckPermit() {
        boolean flag = authorizeService.checkPermit();
        return AjaxResult.success(flag);
    }

    @GetMapping("/getpermit")
    @ApiOperation(value = "获取许可数状态", httpMethod = "GET")
    public AjaxResult getPermit() {
        JSONObject jsonObject = authorizeService.getPermitNum();

        return AjaxResult.success(jsonObject);
    }
}
