package com.yuanqing.project.tiansu.service.analysis.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yuanqing.common.utils.http.HttpUtils;
import com.yuanqing.common.utils.ip.IpUtils;
import com.yuanqing.framework.redis.RedisCache;
import com.yuanqing.project.tiansu.mapper.analysis.ScreenMapper;
import com.yuanqing.project.tiansu.mapper.assets.CameraMapper;
import com.yuanqing.project.tiansu.mapper.assets.ClientTerminalMapper;
import com.yuanqing.project.tiansu.mapper.assets.ClientUserMapper;
import com.yuanqing.project.tiansu.mapper.assets.ServerTreeMapper;
import com.yuanqing.project.tiansu.mapper.operation.BusiHttpPerfMapper;
import com.yuanqing.project.tiansu.mapper.operation.OperationBehaviorMapper;
import com.yuanqing.project.tiansu.service.analysis.IScreenService;
import com.yuanqing.project.tiansu.service.analysis.IStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xucan
 * @version 1.0
 * @Date 2021-04-02 10:25
 */
@Service
public class ScreenServiceImpl implements IScreenService {

    @Autowired
    private ScreenMapper screenMapper;

    @Autowired
    private IStatisticsService statisticsService;

    @Autowired
    private CameraMapper cameraMapper;

    @Autowired
    private ClientTerminalMapper terminalMapper;

    @Autowired
    private ClientUserMapper clientUserMapper;

    @Resource
    private ServerTreeMapper serverTreeMapper;

    @Resource
    private OperationBehaviorMapper operationBehaviorMapper;

    @Resource
    private BusiHttpPerfMapper busiHttpPerfMapper;

    @Resource
    private RedisCache redisCache;

    @Value("${tiansu.alarmhost}")
    private String prefix;

    public static final String PREFIX = "MACS:HTTP_CONFIG:";
    public static final String OVERTIME = "请求超时";

    @Override
    public String getCameraMap(String dateType) {
        //TODO:两种情况，区域统计和街道统计 目前只有区域统计
        List<JSONObject> cameraMap = statisticsService.getVisitedRate(null, dateType);
        String value = formatCameraMap(cameraMap);
        return value;

    }

    @Override
    public String getOperCategory(Date dateTime) {

        List<JSONObject> categoryList = screenMapper.getOperCategory();

        return categoryList.toString();
    }

    @Override
    public String getOperWarn(Date dateTime) {
        long date = dateTime.getTime();
        JSONObject filter = new JSONObject();

        String now = convertTimestamp2Date(date, "yyyy-MM-dd HH:mm");
        filter.put("startDate", now);

        long lastTime = date + 1000 * 60;

        List<JSONObject> list = new ArrayList<>();
        for (int i = 1; i <= 30; i++) {
            lastTime = lastTime - 1000 * 60;
            JSONObject jsonObject = new JSONObject();
            String last = convertTimestamp2Date(lastTime, "HH:mm");
            jsonObject.put("time", last);
            jsonObject.put("warnCount", 0);
            jsonObject.put("operCount", 0);
            list.add(jsonObject);
        }
        List<JSONObject> operList = screenMapper.getOperWarnByOper();

        String url = prefix + "/analysisEvent/queryEventCntPerMinute?intervalMinutes=30";

        String result = HttpUtils.sendGet(url, "");

        List<JSONObject> warnList = JSONArray.parseArray(result, JSONObject.class);


        for (JSONObject j : list) {
            String time = (String) j.get("time");

            for (int i = 0; i < operList.size(); i++) {

                String strOperTime = (String) operList.get(i).get("minuteTime");

                if (strOperTime.equals(time)) {
                    j.put("operCount", operList.get(i).get("operCnt"));
                }

                if (i < warnList.size()) {

                    String strWarnTime = (String) warnList.get(i).get("minuteTime");

                    if (strWarnTime.equals(time)) {
                        j.put("warnCount", warnList.get(i).get("eventCnt"));
                    }
                }
            }
        }
        return list.toString();
    }

    @Override
    public String getOperCount(JSONObject filter) {
        List<JSONObject> list = screenMapper.getOperCount(filter);
        JSONObject jsonObject = formatOperCount(list);
        return jsonObject.toString();
    }


    @Override
    public String getUserTop(JSONObject filter) {
        List<JSONObject> userList = screenMapper.getUserTop(filter);
        String str = userList.toString();
        return str;
    }


    @Override
    public String getTerminalTop(JSONObject filter) {

        List<JSONObject> terminalTopList = screenMapper.getTerminalTop(filter);

        terminalTopList.stream().forEach(f -> {
            if (f.get("ip") != null) {
                f.put("ip", IpUtils.longToIp(f.getLong("ip")));
            } else {
                f.put("ip", "未知IP");
            }
        });
        return terminalTopList.toString();
    }

    @Override
    public String getCameraTop(JSONObject filter) {
        List<JSONObject> cameraTopList = screenMapper.getCameraTop(filter);
        return cameraTopList.toString();
    }


    @Override
    public JSONObject getWarn() {
        JSONObject jsonObject = new JSONObject();
//        return jsonObject;
        String url = prefix + "/BusiEvent/getEventActive";
        String count = HttpUtils.sendGet(url, "");
        jsonObject.put("WARNCOUNT", count);
        return jsonObject;
    }

    @Override
    public JSONObject getSummary() {

        JSONObject summary = new JSONObject();

        int cameraNum = 0;
        int terminalNum = 0;
        int userNum = 0;
        int serverNum = 0;

        cameraNum = cameraMapper.getRealTotal();

        userNum = clientUserMapper.getRealTotal();

        terminalNum = terminalMapper.getRealTotal();

        serverNum = serverTreeMapper.getRealTotal();

        summary.put("terminal", terminalNum);
        summary.put("person", userNum);
        summary.put("camera", cameraNum);
        summary.put("server", serverNum);

        return summary;
    }

    @Override
    public Integer getOperNum(Date startDate, Date endDate) {
        return operationBehaviorMapper.getOperNum(startDate, endDate);
    }

    @Override
    public Integer getHttpApi(Date startDate, Date endDate) {
        return busiHttpPerfMapper.getHttpApiNum(startDate, endDate);
    }

    @Override
    public Integer getApiErrorNum(Date startDate, Date endDate) {
        return busiHttpPerfMapper.getApiErrorNum(startDate, endDate);
    }

    @Override
    public Integer getApiOverTime(Date startDate, Date endDate) {
        //获取配置，再去查询
        JSONObject overConfig = redisCache.getCacheObject(PREFIX+OVERTIME);
        Double time = overConfig.getDouble("configvalue");
        return busiHttpPerfMapper.getApiOverTime(startDate, endDate, time);
    }


    private String convertTimestamp2Date(Long timestamp, String pattern) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        return simpleDateFormat.format(new Date(timestamp));
    }

    private JSONObject formatOperCount(List<JSONObject> list) {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("login", 0);
        jsonObject.put("logout", 0);
        jsonObject.put("play", 0);
        jsonObject.put("playback", 0);
        jsonObject.put("download", 0);
        jsonObject.put("control", 0);
        for (JSONObject obj : list) {
            int action = (int) obj.get("action");
            if (obj.get("action") != null) {
                if (action == 0) {
                    jsonObject.put("play", obj.get("actionCnt"));
                }
                if (action == 1) {
                    jsonObject.put("download", obj.get("actionCnt"));
                }
                if (action == 2) {
                    jsonObject.put("playback", obj.get("actionCnt"));
                }
                if (action == 3) {
                    jsonObject.put("control", obj.get("actionCnt"));
                }
                if (action == 4) {
                    jsonObject.put("login", obj.get("actionCnt"));
                }
                if (action == 5) {
                    jsonObject.put("logout", obj.get("actionCnt"));
                }
            }
        }
        return jsonObject;
    }

//    private String a(List<JSONObject> categoryList){
//
//
//        long lastTime = date+1000*60*60;
//
//        List<JSONObject> list = new ArrayList<>();
//        JSONObject jsonObject = null;
//        for(int i = 1;i<=7;i++){
//            lastTime = lastTime-1000*60*60;
//            jsonObject = new JSONObject();
//            String last = convertTimestamp2Date(lastTime,"yyyy-MM-dd HH:mm:ss");
//            jsonObject.put("time",last);
//            jsonObject.put("play",0);
//            jsonObject.put("playback",0);
//            jsonObject.put("download",0);
//            jsonObject.put("control",0);
//            list.add(jsonObject);
//        }
//
//        for(JSONObject j : list){
//            String time = (String) j.get("time");
//            for(JSONObject js : categoryList){
//
//                Timestamp cTime = (Timestamp)js.get("DT");
//                Long categoryTime = cTime.getTime();
//                String strCategoryTime = convertTimestamp2Date(categoryTime,"yyyy-MM-dd HH:mm:ss");
//
//
//                if(strCategoryTime.equals(time)){
//                    BigDecimal action = (BigDecimal)js.get("ACTION");
//                    if(action.intValue()==0){
//                        j.put("play",js.get("DTCNT"));
//                    }
//                    if(action.intValue()==1){
//                        j.put("download",js.get("DTCNT"));
//                    }
//                    if(action.intValue()==2){
//                        j.put("playback",js.get("DTCNT"));
//                    }
//                    if(action.intValue()==3){
//                        j.put("control",js.get("DTCNT"));
//                    }
//                }
//            }
//        }
//        return list.toString();
//    }

    private String formatCameraMap(List<JSONObject> list) {
        List<JSONObject> cameraMapList = list.stream().map(f -> {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("areaCode", f.get("cityCode"));
            jsonObject.put("areaName", f.get("cityName"));
            jsonObject.put("camera", f.get("cameraCnt"));
            return jsonObject;
        }).collect(Collectors.toList());

        return cameraMapList.toString();

    }

}
