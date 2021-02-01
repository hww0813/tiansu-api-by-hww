package com.yuanqing.project.tiansu.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yuanqing.common.utils.DateUtils;
import com.yuanqing.framework.web.controller.BaseController;
import com.yuanqing.framework.web.domain.AjaxResult;
import com.yuanqing.framework.web.domain.PageResult;
import com.yuanqing.project.tiansu.domain.operation.OperationBehavior;
import com.yuanqing.project.tiansu.domain.operation.OperationBehaviorSearch;
import com.yuanqing.project.tiansu.mapper.assets.OperationBehaviorMapper;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.beans.PropertyEditorSupport;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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

    /**
     * @author: dongchao
     * @create: 2021/2/1-15:51
     * @description: 不直接查库 ，查告警中心接口
     * @param:
     * @return:
     */
    @GetMapping("/findByEventId")
    @ApiOperation(value = "根据事件id获取操作行为列表", httpMethod = "GET")
    public PageResult findByEventId(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                @RequestParam(value = "stime") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime stime,
                                @RequestParam(value = "etime") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime etime,
                                @RequestParam(value = "dstCode", required = false) String dstCode,
                                @RequestParam(value = "id", required = false) Long id,
                                @RequestParam(value = "dstDeviceIp", required = false) Long dstDeviceIp) {

        JSONObject filters = new JSONObject();

        filters.put("stime", stime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        filters.put("etime", etime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        filters.put("dstCode", dstCode);
        filters.put("id", id);
        filters.put("dstDeviceIp", dstDeviceIp);
      /*  PageInfo<OperationBehavior> pageInfo = operationBehaviorManager.findByEventId(pageNum, pageSize, filters);
        return ResultUtils.success(pageInfo);*/
      return null;
    }

    /**
     * @author: dongchao
     * @create: 2021/2/1-16:28
     * @description: 只获取了前三千条数据
     * @param:
     * @return:
     */
    @GetMapping("/realTimeList")
    @ApiOperation(value = "获取实时操作行为列表", httpMethod = "GET")
    public PageResult getRealTimeList(OperationBehaviorSearch operationBehaviorSearch) throws Exception{
        CompletableFuture<List<OperationBehavior>> operationBehaviorsFuter = CompletableFuture.supplyAsync(()-> operationBehaviorMapper.getRealTimeBehaviorList(operationBehaviorSearch));
        return PageResult.success(operationBehaviorsFuter.get(5, TimeUnit.SECONDS));
    }




    @PutMapping
    @ApiOperation(value = "操作行为更新", httpMethod = "PUT")
    public PageResult putOperationBehavior(@Valid @RequestBody OperationBehavior dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return PageResult.error(500,"更新失败");
        }
        operationBehaviorMapper.updateOperationBehavior(dto);
        return PageResult.success();
    }

}
