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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 *
 * 大屏统计任务
 * @author xucan
 * @version 1.0
 * @Date 2021-04-02 09:59
 */
@Component("ScreenStatisticsTask")
public class ScreenStatisticsTask {


    private static final Logger LOGGER = LoggerFactory.getLogger(ScreenStatisticsTask.class);

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private IScreenService screenService;



    @Scheduled(cron = "0/3 * * * * ?")
    public void getOperCategory(){

        String value = screenService.getOperCategory(new Date());

        JSONObject object = new JSONObject();
        object.put("value",value);
        redisCache.setCacheObject(ScreenConstants.OPERATION_CATEGORY, object);
        LOGGER.info("定时任务执行完成:统计操作行为分类(分时段)");

    }

    @Scheduled(cron = "30 0/1 * * * ?")
    public void getCameraMap(){

        String value = screenService.getCameraMap("all");
        if(value==null){
            value="[]";
        }
        JSONObject object = new JSONObject();
        object.put("value",value);
      redisCache.setCacheObject(ScreenConstants.CAMERA_MAP, object);
        LOGGER.info("定时任务执行完成:统计摄像头地图");

    }


    @Scheduled(cron = "0/30 * * * * ?")
    public void getOperWarn(){

        String value = screenService.getOperWarn(new Date());
        JSONArray jsonArray = JSONArray.parseArray(value);
        redisCache.setCacheObject(ScreenConstants.REAL_OPERATION_WARN, jsonArray);
        LOGGER.info("定时任务执行完成:统计操作行为/告警事件(分时段)");

    }

    @Scheduled(cron = "0 0/1 * * * ?")
    public void getOperCount(){

        JSONObject filter = DateUtils.getDay();
        String value = screenService.getOperCount(filter);

        JSONArray object;
        object = JSONArray.parseArray(value);
        redisCache.setCacheObject(ScreenConstants.OPERATION_NUM_DAY, object);
        LOGGER.info("定时任务执行完成:分类统计操作行为(今日)");


        filter = DateUtils.getWeek();
        String week = screenService.getOperCount(filter);

        object = JSONArray.parseArray(week);
        redisCache.setCacheObject(ScreenConstants.OPERATION_NUM_WEEK, object);
        LOGGER.info("定时任务执行完成:分类统计操作行为(本周)");

        filter = DateUtils.getMonth();
        String month = screenService.getOperCount(filter);

        object = JSONArray.parseArray(month);
        redisCache.setCacheObject(ScreenConstants.OPERATION_NUM_MONTH, object);
        LOGGER.info("定时任务执行完成:分类统计操作行为(本月)");

    }


    @Scheduled(cron = "1 0/1 * * * ?")
    public void getUserTopByDay(){

        JSONObject filter = DateUtils.getDay();
        String value = screenService.getUserTop(filter);

        JSONArray object = JSONArray.parseArray(value);
        redisCache.setCacheObject(ScreenConstants.USER_DAY, object);
        LOGGER.info("定时任务执行完成:活跃用户TOP(今日)");

        filter = DateUtils.getWeek();
        String week = screenService.getUserTop(filter);

        object = JSONArray.parseArray(week);
        redisCache.setCacheObject(ScreenConstants.USER_WEEK, object);
        LOGGER.info("定时任务执行完成:活跃用户TOP(本周)");

        filter = DateUtils.getMonth();
        String month = screenService.getUserTop(filter);

        object = JSONArray.parseArray(month);
        redisCache.setCacheObject(ScreenConstants.USER_MONTH, object);
        LOGGER.info("定时任务执行完成:活跃用户TOP(本月)");

    }

    @Scheduled(cron = "2 0/1 * * * ?")
    public void getTerminalTopByDay(){

        JSONObject filter = DateUtils.getDay();
        String value = screenService.getTerminalTop(filter);

        JSONArray object = new JSONArray();
        object = JSONArray.parseArray(value);
        redisCache.setCacheObject(ScreenConstants.TERMINAL_DAY, object);
        LOGGER.info("定时任务执行完成:活跃终端TOP(今日)");

        filter = DateUtils.getWeek();
        String week = screenService.getTerminalTop(filter);

        object = JSONArray.parseArray(week);
        redisCache.setCacheObject(ScreenConstants.TERMINAL_WEEK, object);
        LOGGER.info("定时任务执行完成:活跃终端TOP(本周)");

        filter = DateUtils.getMonth();
        String month = screenService.getTerminalTop(filter);
        object = JSONArray.parseArray(month);
        redisCache.setCacheObject(ScreenConstants.TERMINAL_MONTH, object);
        LOGGER.info("定时任务执行完成:活跃终端TOP(本月)");

    }

    @Scheduled(cron = "3 0/1 * * * ?")
    public void getCameraTop(){

        JSONObject filter = DateUtils.getDay();
        String value = screenService.getCameraTop(filter);

        JSONArray object = JSONArray.parseArray(value);
        redisCache.setCacheObject(ScreenConstants.CAMERA_DAY, object);
        LOGGER.info("定时任务执行完成:活跃摄像头TOP(今日)");


        filter = DateUtils.getWeek();
        String week = screenService.getCameraTop(filter);

        object = JSONArray.parseArray(week);
        redisCache.setCacheObject(ScreenConstants.CAMERA_WEEK, object);
        LOGGER.info("定时任务执行完成:活跃摄像头TOP(本周)");

        filter = DateUtils.getMonth();
        String month = screenService.getCameraTop(filter);

        object = JSONArray.parseArray(month);
        redisCache.setCacheObject(ScreenConstants.CAMERA_MONTH, object);
        LOGGER.info("定时任务执行完成:活跃摄像头TOP(本月)");

    }


    @Scheduled(cron = "0/5 * * * * ?")
    public void getWarn(){
        JSONObject day = screenService.getWarn();

        JSONObject week = screenService.getWarn();

        JSONObject month = screenService.getWarn();
        JSONObject warn = new JSONObject();

        warn.put("day",day.get("WARNCOUNT"));
        warn.put("week",week.get("WARNCOUNT"));
        warn.put("month",month.get("WARNCOUNT"));

        redisCache.setCacheObject(ScreenConstants.WARN, warn);
        LOGGER.info("定时任务执行完成:统计告警事件(当日/本周/本月)");

    }

    @Scheduled(cron = "0/5 * * * * ?")
    public void getSummary(){
        JSONObject object =  screenService.getSummary();

        redisCache.setCacheObject(ScreenConstants.SUMMARY, object);
    }

}
