package com.yuanqing.project.tiansu.controller;

import com.github.pagehelper.PageHelper;
import com.yuanqing.common.utils.DateUtils;
import com.yuanqing.framework.web.controller.BaseController;
import com.yuanqing.framework.web.domain.AjaxResult;
import com.yuanqing.project.tiansu.domain.operation.OperationBehavior;
import com.yuanqing.project.tiansu.mapper.assets.OperationBehaviorMapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.beans.PropertyEditorSupport;
import java.util.Date;
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
public class OperationBehaviorController extends BaseController   {



    @Resource
    private OperationBehaviorMapper operationBehaviorMapper;



    @GetMapping("/list")
    @ApiOperation(value = "获取操作行为列表", httpMethod = "GET")

    public AjaxResult getAll(@RequestParam(value = "pageNum", defaultValue = "1") int num,
                             @RequestParam(value = "pageSize", defaultValue = "20") int size,OperationBehavior operationBehavior) {
        operationBehavior.setNum((long) num);
        operationBehavior.setSize((long) size);
        PageHelper.clearPage();
        List<OperationBehavior> operationBehaviors = operationBehaviorMapper.queryOperationBehaviorList(operationBehavior);
        return AjaxResult.success(operationBehaviors);
    }


}
