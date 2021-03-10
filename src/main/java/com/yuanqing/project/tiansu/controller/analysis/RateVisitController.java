package com.yuanqing.project.tiansu.controller.analysis;

import com.yuanqing.common.utils.DateUtils;
import com.yuanqing.framework.redis.RedisCache;
import com.yuanqing.framework.web.domain.AjaxResult;
import com.yuanqing.project.tiansu.domain.macs.MacsRegion;
import com.yuanqing.project.tiansu.service.macs.IMacsConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

import static com.yuanqing.common.constant.Constants.INDEX_VISITED_RATE_CACHE;

/**
 * @author Dong.Chao
 * @Classname RateVisitController
 * @Description 首页摄像头访问率分析
 * @Date 2021/3/10 9:59
 * @Version V1.0
 */
@RestController
@RequestMapping(value = "/api/analysis/visit/rate")
public class RateVisitController {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private IMacsConfigService macsConfigService;

    @GetMapping(value = "/list")
    @ApiOperation(value = "首页访问率查询", httpMethod = "GET")
    public AjaxResult rateList(@RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                               @RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
                               @RequestParam(value = "pageNum", required = false) String pageNum,
                               @RequestParam(value = "pageSize",required = false) String pageSize){
        String time = DateUtils.getTimeType(startDate,endDate);
        return AjaxResult.success("success",redisCache.getCacheObject(INDEX_VISITED_RATE_CACHE+"_"+time));
    }

    @GetMapping(value = "/region")
    @ApiOperation(value = "首页访问率查询", httpMethod = "GET")
    public AjaxResult getRegion(){
        MacsRegion region = macsConfigService.getRegion(null);
        return AjaxResult.success(region);

    }


}
