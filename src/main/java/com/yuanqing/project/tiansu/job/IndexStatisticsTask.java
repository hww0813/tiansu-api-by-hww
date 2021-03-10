package com.yuanqing.project.tiansu.job;

import com.alibaba.fastjson.JSONObject;
import com.yuanqing.common.constant.Constants;
import com.yuanqing.common.utils.DateUtils;
import com.yuanqing.common.utils.StringUtils;
import com.yuanqing.common.utils.http.HttpUtils;
import com.yuanqing.common.utils.ip.IpUtils;
import com.yuanqing.framework.redis.RedisCache;
import com.yuanqing.project.tiansu.domain.video.CameraStatistics;
import com.yuanqing.project.tiansu.domain.video.StatisticsSearch;
import com.yuanqing.project.tiansu.mapper.video.HomePageMapper;
import com.yuanqing.project.tiansu.service.analysis.IStatisticsService;
import com.yuanqing.project.tiansu.service.assets.IClientTerminalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.yuanqing.common.constant.Constants.*;

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

    @Autowired
    private IStatisticsService iStatisticsService;



    @Value("${tiansu.alarmhost}")
    private String alarmHost;


    /**
     * @author: dongchao
     * @create: 2021/2/4-23:31
     * @description: 查询访问最多的摄像头
     * @param: timeType:时间选择， action:操作类型
     * @return:
     */
    public void visitCamera(String timeType,Long action) {
        String cacheKey = "CAMERA_"+timeType+"_"+action;
        redisCache.deleteObject(cacheKey);
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
        redisCache.deleteObject(cacheKey);
        StatisticsSearch statisticsSearch = initSearch(timeType,action);
        //查倒序
        CompletableFuture.runAsync(() -> {
            try {
                List<JSONObject> clientStatistics = homePageMapper.getClinetStatisticsByTime(statisticsSearch);
                clientStatistics.stream().forEach(j -> j.put("CLIENT_IP",IpUtils.longToIPv4(j.getLong("CLIENT_IP"))));
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
                clientStatistics.stream().forEach(j -> j.put("CLIENT_IP",IpUtils.longToIPv4(j.getLong("CLIENT_IP"))));
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
        redisCache.deleteObject(cacheKey);
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


    /**
     * @author: dongchao
     * @create: 2021/2/26-10:25
     * @description: 缓存首页数据
     * @param:
     * @return:
     */
    public void cacheIndexCounts(){
        //缓存终端、摄像头、用户数据
        Date nowDate = DateUtils.getNowDate();
        Map clientCounts = homePageMapper.queryClientStatisticsCount(nowDate);
        Map userCounts = homePageMapper.queryUserStatisticsCount(nowDate);
        Map cameraCounts = homePageMapper.queryCameraStatisticsCount(nowDate);


        redisCache.setCacheObject(Constants.INDEX_CLIENT_COUNTS_CACHE,clientCounts);
        redisCache.setCacheObject(Constants.INDEX_USER_COUNTS_CACHE,userCounts);
        redisCache.setCacheObject(Constants.INDEX_CAMERA_COUNTS_CACHE,cameraCounts);
        //缓存告警事件数据
        String url = alarmHost+"/BusiEvent/getTAllNum";
        String  alarmDiscoveryCount =  HttpUtils.sendGet(url,"status=1");
        String  alarmAllCount =  HttpUtils.sendGet(url,"status=0");
        url =   alarmHost+"/BusiEvent/getEventActive";
        String  alarmActiveCount =  HttpUtils.sendGet(url,"");
        HashMap map = new HashMap();
        map.put("discovery",Integer.parseInt(alarmDiscoveryCount));
        map.put("count",Integer.parseInt(alarmAllCount));
        map.put("active",Integer.parseInt(alarmActiveCount));
        redisCache.setCacheObject(Constants.ALARM_CAMERA_COUNTS_CACHE,cameraCounts);

    }


    /**
     * @author: dongchao
     * @create: 2021/3/4-15:01
     * @description: 缓存首页地区摄像头访问率
     * @param:
     * @return:
     */
    public void initIndexAreaCache(){
        // null默认为当月
        List<JSONObject> visitedRateDay = iStatisticsService.getVisitedRate(null,"day");
        List<JSONObject> visitedRateWeek = iStatisticsService.getVisitedRate(null,"week");
        List<JSONObject> visitedRateMonth = iStatisticsService.getVisitedRate(null,"month");

        redisCache.setCacheObject(INDEX_VISITED_RATE_CACHE_MONTH,visitedRateMonth);
        redisCache.setCacheObject(INDEX_VISITED_RATE_CACHE_DAY,visitedRateDay);
        redisCache.setCacheObject(INDEX_VISITED_RATE_CACHE_WEEK,visitedRateWeek);
    }





    private StatisticsSearch initSearch(String timeType, Long action){
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
