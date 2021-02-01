package com.yuanqing.project.tiansu.controller;

import com.github.pagehelper.PageHelper;
import com.yuanqing.common.utils.DateUtils;
import com.yuanqing.framework.web.controller.BaseController;
import com.yuanqing.framework.web.domain.AjaxResult;
import com.yuanqing.framework.web.domain.PageResult;
import com.yuanqing.project.tiansu.domain.operation.OperationBehavior;
import com.yuanqing.project.tiansu.mapper.assets.OperationBehaviorMapper;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.beans.PropertyEditorSupport;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

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
    public PageResult getAll(@RequestParam(value = "pageNum", defaultValue = "1") int num,
                             @RequestParam(value = "pageSize", defaultValue = "20") int size,OperationBehavior operationBehavior) throws ExecutionException, InterruptedException {
            operationBehavior.setNum(num);
            operationBehavior.setSize(size);
            //
            if (StringUtils.isNotBlank(operationBehavior.getOrderType()) && StringUtils.isNotBlank(operationBehavior.getOrderValue())) {
                operationBehavior.setOrderType(operationBehavior.getOrderType()+ " " +operationBehavior.getOrderValue());
            }
            //地区码判断
            if (operationBehavior.getRegionList() != null) {
                String[] regionList = operationBehavior.getRegionList();
                String region = regionList[regionList.length - 1];
                if (regionList.length == 1) {
                    int count = region.length();
                    if (count == 2) {
                        operationBehavior.setProvinceRegion(region);
                    }
                    if (count == 4) {
                        operationBehavior.setCityRegion(region);
                    }
                    if (count == 6) {
                        operationBehavior.setCityRegion(region);
                    }
                }
                if (regionList.length == 2) {
                    int count = region.length();
                    if (count == 4) {
                        operationBehavior.setCityRegion(region);
                    }
                    if (count == 6) {
                        operationBehavior.setCountryRegion(region);
                    }
                }
                if (regionList.length == 3) {
                    operationBehavior.setCountryRegion(region);
                }
            }
           //总数据
            CompletableFuture<Integer> totalFuter = CompletableFuture.supplyAsync(() ->  operationBehaviorMapper.quertyOperationBehaviorCount());
            //操作行为列表
            CompletableFuture<List<OperationBehavior>> operationBehaviorsFuter = CompletableFuture.supplyAsync(() -> operationBehaviorMapper.queryOperationBehaviorList(operationBehavior));

            return PageResult.success(operationBehaviorsFuter.get(),num,size,totalFuter.get());
    }


}
