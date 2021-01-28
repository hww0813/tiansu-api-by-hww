package com.yuanqing.project.tiansu.controller;

import com.yuanqing.framework.web.controller.BaseController;
import com.yuanqing.framework.web.domain.AjaxResult;
import com.yuanqing.project.tiansu.domain.operation.OperationBehavior;
import com.yuanqing.project.tiansu.mapper.assets.OperationBehaviorMapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Dong.Chao
 * @Classname OperationBehaviorController
 * @Description 操作行为相关
 * @Date 2021/1/28 15:07
 * @Version V1.0
 */
@RestController
@RequestMapping(value = "/api/operation/behavior")
public class OperationBehaviorController extends BaseController {


    @Resource
    private OperationBehaviorMapper operationBehaviorMapper;



    @GetMapping("/list")
    @ApiOperation(value = "获取操作行为列表", httpMethod = "GET")
    public AjaxResult getAll(OperationBehavior operationBehavior) {
       List<OperationBehavior> operationBehaviors = operationBehaviorMapper.queryOperationBehaviorList(operationBehavior);
       return AjaxResult.success(operationBehaviors);
    }


}
