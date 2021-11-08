package com.yuanqing.project.tiansu.job;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yuanqing.common.constant.ScreenConstants;
import com.yuanqing.common.utils.DateUtils;
import com.yuanqing.framework.redis.RedisCache;
import com.yuanqing.project.tiansu.service.analysis.IScreenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 大屏统计任务
 * @author xucan
 * @version 1.0
 * @Date 2021-04-02 09:59
 */
@Component
public class ScreenStatisticsTask {


    private static final Logger LOGGER = LoggerFactory.getLogger(ScreenStatisticsTask.class);

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private IScreenService screenService;

    private int expireTime = 1;


    public void getOperCategory() {

//        String value = screenService.getOperCategory(new Date());
//
//        JSONArray object = JSONArray.parseArray(value);
//        redisCache.setCacheObject(ScreenConstants.OPERATION_CATEGORY, object);
//        LOGGER.info("更新大屏redis缓存:统计操作行为分类(分时段)");

    }

    public void getCameraMap() {

        List<JSONObject> value = screenService.getCameraMap("all");

        JSONObject object = new JSONObject();
        object.put("value" , value);
        redisCache.setCacheObject(ScreenConstants.CAMERA_MAP, value ,expireTime,TimeUnit.MINUTES);
        LOGGER.info("更新大屏redis缓存:统计摄像头地图");

    }


    public void getOperWarn() {
        String value = screenService.getOperWarn(new Date());
        JSONArray jsonArray = JSONArray.parseArray(value);
        redisCache.setCacheObject(ScreenConstants.REAL_OPERATION_WARN, jsonArray,expireTime,TimeUnit.MINUTES);
        LOGGER.info("更新大屏redis缓存:统计操作行为/告警事件(分时段)");

    }

    public void getOperCount() {

        JSONObject filter = DateUtils.getDay();
        String value = screenService.getOperCount(filter);

        JSONObject object = JSONObject.parseObject(value);
        redisCache.setCacheObject(ScreenConstants.OPERATION_NUM_DAY, object, expireTime,TimeUnit.MINUTES);
        LOGGER.info("更新大屏redis缓存:分类统计操作行为(今日)");


        filter = DateUtils.getWeek();
        String week = screenService.getOperCount(filter);

        object = JSONObject.parseObject(week);
        redisCache.setCacheObject(ScreenConstants.OPERATION_NUM_WEEK, object, expireTime,TimeUnit.MINUTES);
        LOGGER.info("更新大屏redis缓存:分类统计操作行为(本周)");

        filter = DateUtils.getMonth();
        String month = screenService.getOperCount(filter);

        object = JSONObject.parseObject(month);
        redisCache.setCacheObject(ScreenConstants.OPERATION_NUM_MONTH, object, expireTime,TimeUnit.MINUTES);
        LOGGER.info("更新大屏redis缓存:分类统计操作行为(本月)");

    }


    public void getUserTopByDay() {

        JSONObject filter = DateUtils.getDay();
        String value = screenService.getUserTop(filter);

        JSONArray object = JSONArray.parseArray(value);
        redisCache.setCacheObject(ScreenConstants.USER_DAY, object,expireTime,TimeUnit.MINUTES);
        LOGGER.info("更新大屏redis缓存:活跃用户TOP(今日)");

        filter = DateUtils.getWeek();
        String week = screenService.getUserTop(filter);

        object = JSONArray.parseArray(week);
        redisCache.setCacheObject(ScreenConstants.USER_WEEK, object,expireTime,TimeUnit.MINUTES);
        LOGGER.info("更新大屏redis缓存:活跃用户TOP(本周)");

        filter = DateUtils.getMonth();
        String month = screenService.getUserTop(filter);

        object = JSONArray.parseArray(month);
        redisCache.setCacheObject(ScreenConstants.USER_MONTH, object,expireTime,TimeUnit.MINUTES);
        LOGGER.info("更新大屏redis缓存:活跃用户TOP(本月)");

    }

    public void getTerminalTopByDay() {

        JSONObject filter = DateUtils.getDay();
        String value = screenService.getTerminalTop(filter);

        JSONArray object = new JSONArray();
        object = JSONArray.parseArray(value);
        redisCache.setCacheObject(ScreenConstants.TERMINAL_DAY, object,expireTime,TimeUnit.MINUTES);
        LOGGER.info("更新大屏redis缓存:活跃终端TOP(今日)");

        filter = DateUtils.getWeek();
        String week = screenService.getTerminalTop(filter);

        object = JSONArray.parseArray(week);
        redisCache.setCacheObject(ScreenConstants.TERMINAL_WEEK, object,expireTime,TimeUnit.MINUTES);
        LOGGER.info("更新大屏redis缓存:活跃终端TOP(本周)");

        filter = DateUtils.getMonth();
        String month = screenService.getTerminalTop(filter);
        object = JSONArray.parseArray(month);
        redisCache.setCacheObject(ScreenConstants.TERMINAL_MONTH, object,expireTime,TimeUnit.MINUTES);
        LOGGER.info("更新大屏redis缓存:活跃终端TOP(本月)");

    }

    public void getCameraTop() {

        JSONObject filter = DateUtils.getDay();
        String value = screenService.getCameraTop(filter);

        JSONArray object = JSONArray.parseArray(value);
        redisCache.setCacheObject(ScreenConstants.CAMERA_DAY, object,expireTime,TimeUnit.MINUTES);
        LOGGER.info("更新大屏redis缓存:活跃摄像头TOP(今日)");


        filter = DateUtils.getWeek();
        String week = screenService.getCameraTop(filter);

        object = JSONArray.parseArray(week);
        redisCache.setCacheObject(ScreenConstants.CAMERA_WEEK, object,expireTime,TimeUnit.MINUTES);
        LOGGER.info("更新大屏redis缓存:活跃摄像头TOP(本周)");

        filter = DateUtils.getMonth();
        String month = screenService.getCameraTop(filter);

        object = JSONArray.parseArray(month);
        redisCache.setCacheObject(ScreenConstants.CAMERA_MONTH, object,expireTime,TimeUnit.MINUTES);
        LOGGER.info("更新大屏redis缓存:活跃摄像头TOP(本月)");

    }


    public void getWarn() {
        JSONObject day = screenService.getWarn();

        JSONObject week = screenService.getWarn();

        JSONObject month = screenService.getWarn();
        JSONObject warn = new JSONObject();

        warn.put("day" , day.get("WARNCOUNT"));
        warn.put("week" , week.get("WARNCOUNT"));
        warn.put("month" , month.get("WARNCOUNT"));

        redisCache.setCacheObject(ScreenConstants.WARN, warn,expireTime,TimeUnit.MINUTES);
        LOGGER.info("更新大屏redis缓存:统计告警事件(当日/本周/本月)");

    }

    public void getSummary() {
        JSONObject object = screenService.getSummary();

        redisCache.setCacheObject(ScreenConstants.SUMMARY, object,expireTime,TimeUnit.MINUTES);

        LOGGER.info("更新大屏redis缓存:大屏资产");
    }

    public void getOperNum() {
        Date endDate = new Date();
        Date startDate = DateUtils.getOfTimeType("DAY");
        Integer dayNum = screenService.getOperNum(startDate,endDate);
        redisCache.setCacheObject(ScreenConstants.OPER_NUM_DAY, dayNum);

        startDate = DateUtils.getOfTimeType("WEEK");
        Integer weekNum = screenService.getOperNum(startDate,endDate);
        redisCache.setCacheObject(ScreenConstants.OPER_NUM_WEEK, weekNum);

        startDate = DateUtils.getOfTimeType("MONTH");
        Integer monthNum = screenService.getOperNum(startDate,endDate);
        redisCache.setCacheObject(ScreenConstants.OPER_NUM_MONTH, monthNum);

        LOGGER.info("更新大屏redis缓存:操作行为总数(当日/前7天/前30天)");
    }

    public void getHttpApiMsg() {
        Date endDate = new Date();

        Date startDate = DateUtils.getOfTimeType("DAY");
        Integer dayNum = screenService.getHttpApi(startDate,endDate);
        redisCache.setCacheObject(ScreenConstants.HTTP_API_DAY, dayNum,expireTime,TimeUnit.MINUTES);
        dayNum = screenService.getApiErrorNum(startDate,endDate);
        redisCache.setCacheObject(ScreenConstants.API_ERROR_DAY, dayNum,expireTime,TimeUnit.MINUTES);
        dayNum = screenService.getApiOverTime(startDate,endDate);
        redisCache.setCacheObject(ScreenConstants.REQUEST_OVERTIME_DAY, dayNum,expireTime,TimeUnit.MINUTES);

        startDate = DateUtils.getOfTimeType("WEEK");
        Integer weekNum = screenService.getHttpApi(startDate,endDate);
        redisCache.setCacheObject(ScreenConstants.HTTP_API_WEEK, weekNum,expireTime,TimeUnit.MINUTES);
        weekNum = screenService.getApiErrorNum(startDate,endDate);
        redisCache.setCacheObject(ScreenConstants.API_ERROR_WEEK, weekNum,expireTime,TimeUnit.MINUTES);
        weekNum = screenService.getApiOverTime(startDate,endDate);
        redisCache.setCacheObject(ScreenConstants.REQUEST_OVERTIME_WEEK, weekNum,expireTime,TimeUnit.MINUTES);

        startDate = DateUtils.getOfTimeType("MONTH");
        Integer monthNum = screenService.getHttpApi(startDate,endDate);
        redisCache.setCacheObject(ScreenConstants.HTTP_API_MONTH, monthNum,expireTime,TimeUnit.MINUTES);
        monthNum = screenService.getApiErrorNum(startDate,endDate);
        redisCache.setCacheObject(ScreenConstants.API_ERROR_MONTH, monthNum,expireTime,TimeUnit.MINUTES);
        monthNum = screenService.getApiOverTime(startDate,endDate);
        redisCache.setCacheObject(ScreenConstants.REQUEST_OVERTIME_MONTH, monthNum,expireTime,TimeUnit.MINUTES);

        LOGGER.info("更新大屏redis缓存:接口调用统计信息(当日/前7天/前30天)");
    }

}
