package com.yuanqing.project.tiansu.service.analysis.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yuanqing.common.utils.http.HttpUtils;
import com.yuanqing.common.utils.ip.IpUtils;
import com.yuanqing.project.tiansu.mapper.analysis.ScreenMapper;
import com.yuanqing.project.tiansu.mapper.assets.CameraMapper;
import com.yuanqing.project.tiansu.mapper.assets.ClientTerminalMapper;
import com.yuanqing.project.tiansu.mapper.assets.ClientUserMapper;
import com.yuanqing.project.tiansu.service.analysis.IScreenService;
import com.yuanqing.project.tiansu.service.analysis.IStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

    @Value("${tiansu.alarmhost}")
    private String prefix;


    @Override
    public String getCameraMap(String dateType){
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
    public String getOperWarn(Date dateTime){
        long date = dateTime.getTime();
        JSONObject filter = new JSONObject();

        String now = convertTimestamp2Date(date,"yyyy-MM-dd HH:mm");
        filter.put("startDate",now);

        long lastTime = date+1000*60;

        List<JSONObject> list = new ArrayList<>();
        for(int i = 1;i<=30;i++){
            lastTime = lastTime-1000*60;
            JSONObject jsonObject = new JSONObject();
            String last = convertTimestamp2Date(lastTime,"HH:mm");
            jsonObject.put("time",last);
            jsonObject.put("warnCount",0);
            jsonObject.put("operCount",0);
            list.add(jsonObject);
        }
        List<JSONObject> operList = screenMapper.getOperWarnByOper();

        String url = prefix + "/analysisEvent/queryEventCntPerMinute?intervalMinutes=30";

        String result = HttpUtils.sendGet(url,"");

        List<JSONObject> warnList = JSONArray.parseArray(result, JSONObject.class);


        for(JSONObject j : list){
            String time = (String) j.get("time");

            for(int i=0;i<operList.size();i++){

                String strOperTime = (String) operList.get(i).get("minuteTime");

                if(strOperTime.equals(time)){
                    j.put("operCount",operList.get(i).get("operCnt"));
                }

                if(i<warnList.size()){

                    String strWarnTime = (String) warnList.get(i).get("minuteTime");

                    if(strWarnTime.equals(time)){
                        j.put("warnCount",warnList.get(i).get("eventCnt"));
                    }
                }
            }
        }
        return list.toString();
    }

    @Override
    public String getOperCount(JSONObject filter){
        List<JSONObject> list = screenMapper.getOperCount(filter);
        JSONObject jsonObject = formatOperCount(list);
        return jsonObject.toString();
    }



    @Override
    public String getUserTop(JSONObject filter){
        List<JSONObject> userList = screenMapper.getUserTop(filter);
        String str = userList.toString();
        String rep1 = str.replaceAll("IDCARD","idCard");
        String result = rep1.replaceAll("COUNT","count");
        return result;
    }


    @Override
    public String getTerminalTop(JSONObject filter){

        List<JSONObject> terminalTopList = screenMapper.getTerminalTop(filter);

        terminalTopList.stream().forEach(f ->{
            if(f.get("ip") != null){
                f.put("ip",IpUtils.longToIp(f.getLong("ip")));
            }else{
                f.put("ip","未知IP");
            }
        });
        return terminalTopList.toString();
    }

    @Override
    public String getCameraTop(JSONObject filter){
        List<JSONObject> cameraTopList = screenMapper.getCameraTop();
        return cameraTopList.toString();
    }


    @Override
    public JSONObject getWarn(){
        JSONObject jsonObject = new JSONObject();
//        return jsonObject;
        String url = prefix + "/BusiEvent/getEventActive";
        String count = HttpUtils.sendGet(url, "");
        jsonObject.put("WARNCOUNT",count);
        return jsonObject;
    }

    @Override
    public JSONObject getSummary() {

        JSONObject summary = new JSONObject();

        int cameraNum = 0;
        int terminalNum = 0;
        int userNum = 0;

        cameraNum = cameraMapper.getRealTotal();

        terminalNum = clientUserMapper.getRealTotal();

        userNum = terminalMapper.getRealTotal();

        summary.put("terminal",terminalNum);
        summary.put("person",userNum);
        summary.put("camera",cameraNum);

        return summary;
    }


    private  String convertTimestamp2Date(Long timestamp, String pattern) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        return simpleDateFormat.format(new Date(timestamp));
    }

    private JSONObject formatOperCount(List<JSONObject> list){

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("login",0);
        jsonObject.put("logout",0);
        jsonObject.put("play",0);
        jsonObject.put("playback",0);
        jsonObject.put("download",0);
        jsonObject.put("control",0);
        for(JSONObject obj : list){
            int action = (int)obj.get("action");
            if(obj.get("action") != null){
                if(action==0){
                    jsonObject.put("play",obj.get("actionCnt"));
                }
                if(action==1){
                    jsonObject.put("download",obj.get("actionCnt"));
                }
                if(action==2){
                    jsonObject.put("playback",obj.get("actionCnt"));
                }
                if(action==3){
                    jsonObject.put("control",obj.get("actionCnt"));
                }
                if(action==4){
                    jsonObject.put("login",obj.get("actionCnt"));
                }
                if(action==5){
                    jsonObject.put("logout",obj.get("actionCnt"));
                }
}
        }
                return jsonObject;
                }


    private String formatCameraMap(List<JSONObject> list){
        List<JSONObject> cameraMapList = list.stream().map(f ->{
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("areaCode",f.get("cityCode"));
            jsonObject.put("areaName",f.get("cityName"));
            jsonObject.put("camera",f.get("cameraCnt"));
            return jsonObject;
        }).collect(Collectors.toList());

        return cameraMapList.toString();

    }

}