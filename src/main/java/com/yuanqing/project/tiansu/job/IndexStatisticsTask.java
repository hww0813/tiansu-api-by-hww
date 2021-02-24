package com.yuanqing.project.tiansu.job;

import com.alibaba.fastjson.JSONObject;
import com.yuanqing.common.utils.DateUtils;
import com.yuanqing.common.utils.StringUtils;
import com.yuanqing.common.utils.ip.IpUtils;
import com.yuanqing.framework.redis.RedisCache;
import com.yuanqing.project.tiansu.domain.video.CameraStatistics;
import com.yuanqing.project.tiansu.domain.video.StatisticsSearch;
import com.yuanqing.project.tiansu.mapper.video.HomePageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @author Dong.Chao
 * @Classname IndexStatisticsTask
 * @Description 首页统计定时任务
 * @Date 2021/2/4 13:57
 * @Version V1.0
 */
@Component("indexStatisticsTask")
@Configurable
public class IndexStatisticsTask {

    @Autowired
    private RedisCache redisCache;


    @Autowired
    private HomePageMapper homePageMapper;



    /**
     * @author: dongchao
     * @create: 2021/2/4-23:31
     * @description: 查询访问最多的摄像头
     * @param: timeType:时间选择， action:操作类型
     * @return:
     */
    public void visitCamera(String timeType,Long action) {
        String cacheKey = timeType+"_"+action;
        if (action == -1){
            action = null;
        }
        //选择对应时间
        Date startTime = null , endTime = null;
        if ("DAY".equals(timeType)){
            startTime = DateUtils.getNowDateToLocal();
            endTime = DateUtils.getNextDay();
        }else if("WEEK".equals(timeType)){
            startTime = DateUtils.getNowMonday();
            endTime = DateUtils.getNowSunday();
        }else if("MONTH".equals(timeType)){
            startTime = DateUtils.getNowMonthOneDay();
            endTime = DateUtils.getNowMonthLastDay();
        }
        Date finalStartTime = startTime;
        Date finalEndTime = endTime;
        Long finalAction = action;
        //查倒序
        CompletableFuture.runAsync(() -> {
            try {
                List<CameraStatistics> cameraStatistics = homePageMapper.getCameraStatisticsByTime(finalStartTime,finalEndTime,10,finalAction,null);
                intiCameraStatistics(cameraStatistics);
                if (!CollectionUtils.isEmpty(cameraStatistics)){
                    redisCache.setCacheObject(cacheKey,cameraStatistics);
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }) ;
        //查正序  访问最少的摄像头
       CompletableFuture.runAsync(() -> {
           try {
               List<CameraStatistics> cameraStatistics = homePageMapper.getCameraStatisticsByTime(finalStartTime,finalEndTime,10,finalAction,"asc");
               intiCameraStatistics(cameraStatistics);
               if (!CollectionUtils.isEmpty(cameraStatistics)){
                   redisCache.setCacheObject(cacheKey+"_REVERSE",cameraStatistics);
               }
           }catch (Exception e){
               e.printStackTrace();
           }
        }) ;
    }


    /**
     * @author: dongchao
     * @create: 2021/2/5-14:25
     * @description: 访问最多的终端
     * @param:
     * @return:
     */
    public void visitClient(String timeType,Long action) {
        String cacheKey = "CLIENT_"+timeType+"_"+action;
        StatisticsSearch statisticsSearch = initSearch(timeType,action);
        //查倒序
        CompletableFuture.runAsync(() -> {
            try {
                List<JSONObject> clientStatistics = homePageMapper.getClinetStatisticsByTime(statisticsSearch);
                if (!CollectionUtils.isEmpty(clientStatistics)){
                    redisCache.setCacheObject(cacheKey,clientStatistics);
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }) ;
        //查正序  访问最少的摄像头
        CompletableFuture.runAsync(() -> {
            try {
                statisticsSearch.setAscFlag("asc");
                List<JSONObject> clientStatistics = homePageMapper.getClinetStatisticsByTime(statisticsSearch);
                if (!CollectionUtils.isEmpty(clientStatistics)){
                    redisCache.setCacheObject(cacheKey+"_REVERSE",clientStatistics);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }) ;
    }


    /**
     * @author: dongchao
     * @create: 2021/2/5-14:20
     * @description: 用户统计
     * @param:
     * @return:
     */
    public void userClient(String timeType,Long action) {
        String cacheKey = "USER_"+timeType+"_"+action;
        StatisticsSearch statisticsSearch = initSearch(timeType,action);
        //查倒序 访问最多的用户名
        CompletableFuture.runAsync(() -> {
            try {
                List<JSONObject> clientStatistics = homePageMapper.getUserStatisticsByTime(statisticsSearch);
                if (!CollectionUtils.isEmpty(clientStatistics)){
                    redisCache.setCacheObject(cacheKey,clientStatistics);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }) ;
        //查正序  访问最少的用户名
        CompletableFuture.runAsync(() -> {
            try {
                statisticsSearch.setAscFlag("asc");
                List<JSONObject> clientStatistics = homePageMapper.getUserStatisticsByTime(statisticsSearch);
                if (!CollectionUtils.isEmpty(clientStatistics)){
                    redisCache.setCacheObject(cacheKey+"_REVERSE",clientStatistics);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }) ;
    }



    public StatisticsSearch initSearch(String timeType, Long action){
       Date startTime = null ,endTime = null;
        if ("DAY".equals(timeType)){
            startTime = DateUtils.getNowDateToLocal();
            endTime = DateUtils.getNextDay();
        }else if("WEEK".equals(timeType)){
            startTime = DateUtils.getNowMonday();
            endTime = DateUtils.getNowSunday();
        }else if("MONTH".equals(timeType)){
            startTime = DateUtils.getNowMonthOneDay();
            endTime = DateUtils.getNowMonthLastDay();
        }
        if (action == -1){
            action = null;
        }
        return StatisticsSearch.builder().startTime(startTime).endTime(endTime).action(action).size(10L).build();

    }




    private void intiCameraStatistics(List<CameraStatistics> cameraStatistics){
        cameraStatistics = cameraStatistics.parallelStream().peek(c -> {
            if (StringUtils.isEmpty(c.getCameraName())){
                c.setCameraName(c.getCameraCode());
            }
            c.setCameraIp(IpUtils.longToIp(Long.parseLong(c.getCameraIp())));
        }).collect(Collectors.toList());
    }
}
