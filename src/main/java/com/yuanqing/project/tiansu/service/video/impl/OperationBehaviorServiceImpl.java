package com.yuanqing.project.tiansu.service.video.impl;

import com.yuanqing.framework.redis.RedisCache;
import com.yuanqing.framework.web.domain.AjaxResult;
import com.yuanqing.framework.web.domain.PageResult;
import com.yuanqing.project.tiansu.domain.operation.OperationBehavior;
import com.yuanqing.project.tiansu.mapper.assets.OperationBehaviorMapper;
import com.yuanqing.project.tiansu.service.video.IOperationBehaviorService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author Dong.Chao
 * @Classname OperationBehaviorServiceImpl
 * @Description
 * @Date 2021/2/25 11:37
 * @Version V1.0
 */
@Service
public class OperationBehaviorServiceImpl implements IOperationBehaviorService {

    @Resource
    private OperationBehaviorMapper operationBehaviorMapper;

    @Autowired
    private RedisCache redisCache;

    @Override
    public PageResult queryOperationList(OperationBehavior operationBehavior) throws Exception {

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
        CompletableFuture<Integer> totalFuter = CompletableFuture.supplyAsync(() ->  operationBehaviorMapper.quertyOperationBehaviorCount(operationBehavior));
        //操作行为列表
        CompletableFuture<List<OperationBehavior>> operationBehaviorsFuture = CompletableFuture.supplyAsync(() -> operationBehaviorMapper.getList(operationBehavior));

        return PageResult.success(operationBehaviorsFuture.get(),operationBehavior.getNum(),operationBehavior.getSize(),totalFuter.get());
    }

    @Override
    public AjaxResult getCharts(LocalDate startDate, LocalDate endDate, String action, String sort, String type) {
        String timeType = getTimeType(startDate,endDate);
        Long actionType = getActionType(action);
        //拼 redis Key
        if (StringUtils.isNotBlank(type)){
            type += "_";
        }else{
            type = "";
        }
        String cacheKey = type+timeType+"_"+actionType;
        if (!"desc".equals(sort)){
            cacheKey = cacheKey + "_REVERSE";
        }
       return AjaxResult.success("success",redisCache.getCacheObject(cacheKey));
    }


    private String getTimeType(LocalDate startDate,LocalDate endDate){
        int dayCount =  (int) startDate.until(endDate, ChronoUnit.DAYS);
        //对应事件
        String timeType;
        switch (dayCount){
            case 1|0 : timeType = "DAY"; break;
            case 6 : timeType = "WEEK"; break;
            default: timeType = "MONTH"; break;
        }
        return timeType;
    };

    public Long getActionType(String action){
        Long actionType = null;
        if (StringUtils.isBlank(action)){
            actionType = -1L;
        }else{
            actionType = Long.parseLong(action);
        }
        return actionType;
    }
}
