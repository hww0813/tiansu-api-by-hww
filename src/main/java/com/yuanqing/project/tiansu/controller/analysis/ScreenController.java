package com.yuanqing.project.tiansu.controller.analysis;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yuanqing.common.constant.ScreenConstants;
import com.yuanqing.framework.redis.RedisCache;
import com.yuanqing.framework.web.domain.AjaxResult;
import com.yuanqing.project.tiansu.domain.analysis.dto.ScreenDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api")
@CrossOrigin
public class ScreenController {

    @Autowired
    private RedisCache redisCache;


    @PostMapping(value = "/assets/summary")
    public AjaxResult getSummary(){

        Object cacheObject = redisCache.getCacheObject(ScreenConstants.SUMMARY);

        return AjaxResult.success(cacheObject);
    }

    @PostMapping(value = "/warn/total")
    public AjaxResult getWarnTotal(@RequestBody ScreenDto screenDto){

        JSONObject eventList = redisCache.getCacheObject(ScreenConstants.WARN);

        int eventCnt = 0;
        if( eventList != null && eventList.size()!=0 ) {
            if(screenDto.getDateType()==1){
                eventCnt = (int) eventList.get("day");
            }else if(screenDto.getDateType()==2){
                eventCnt = (int) eventList.get("week");
            }else if(screenDto.getDateType()==3){
                eventCnt = (int) eventList.get("month");
            }
        }
        JSONObject list = new JSONObject();
        list.put("warnCount",eventCnt);
        return AjaxResult.success(list);
    }


    @PostMapping(value = "/person/top")
    public AjaxResult userTOP(@RequestBody ScreenDto screenDto){

        List<JSONObject> personList = null;
        if(screenDto.getDateType()==1){
            personList = redisCache.getCacheList(ScreenConstants.USER_DAY);
        }else if(screenDto.getDateType()==2){
            personList = redisCache.getCacheList(ScreenConstants.USER_WEEK);
        }else if(screenDto.getDateType()==3){
            personList = redisCache.getCacheList(ScreenConstants.USER_MONTH);
        }

        return AjaxResult.success(personList);
    }

    @PostMapping(value = "/camera/top")
    public AjaxResult cameraTOP(@RequestBody ScreenDto screenDto){

        List<JSONObject> cameraList = null;
        if(screenDto.getDateType()==1){
            cameraList = redisCache.getCacheList(ScreenConstants.CAMERA_DAY);
        }else if(screenDto.getDateType()==2){
            cameraList = redisCache.getCacheList(ScreenConstants.CAMERA_WEEK);
        }else if(screenDto.getDateType()==3){
            cameraList = redisCache.getCacheList(ScreenConstants.CAMERA_MONTH);
        }

        return AjaxResult.success(cameraList);
    }
    @PostMapping(value = "/terminal/top")
    public AjaxResult terminalTOP(@RequestBody ScreenDto screenDto){


        List<JSONObject> terminalList = null;
        if(screenDto.getDateType()==1){
            terminalList = redisCache.getCacheList(ScreenConstants.TERMINAL_DAY);
        }else if(screenDto.getDateType()==2){
            terminalList = redisCache.getCacheList(ScreenConstants.TERMINAL_WEEK);
        }else if(screenDto.getDateType()==3){
            terminalList = redisCache.getCacheList(ScreenConstants.TERMINAL_MONTH);
        }

        return AjaxResult.success(terminalList);
    }
    @PostMapping(value = "/behavior/trend")
    public AjaxResult behaviorTrend(){

        return AjaxResult.success(redisCache.getCacheList(ScreenConstants.OPERATION_CATEGORY));
    }

    @PostMapping(value = "/oper/add")
    public AjaxResult operAdd(){

        return AjaxResult.success(redisCache.getCacheList(ScreenConstants.REAL_OPERATION_WARN));
    }

    @PostMapping(value = "/operate/summary")
    public AjaxResult operateSummary(@RequestBody ScreenDto screenDto){

        JSONObject operateSummary = null;
        if(screenDto.getDateType()==1){
            operateSummary = redisCache.getCacheObject(ScreenConstants.OPERATION_NUM_DAY);
        }else if(screenDto.getDateType()==2){
            operateSummary = redisCache.getCacheObject(ScreenConstants.OPERATION_NUM_WEEK);
        }else if(screenDto.getDateType()==3){
            operateSummary = redisCache.getCacheObject(ScreenConstants.OPERATION_NUM_MONTH);
        }
        return AjaxResult.success(operateSummary);
    }

    @PostMapping(value = "/video/top")
    public AjaxResult videoTop(@RequestBody ScreenDto screenDto){


        List<JSONObject> videoTop = null;
        if(screenDto.getDateType()==1){
            videoTop = redisCache.getCacheList(ScreenConstants.VIDEO_NUM_DAY);
        }else if(screenDto.getDateType()==2){
            videoTop = redisCache.getCacheList(ScreenConstants.VIDEO_NUM_WEEK);
        }else if(screenDto.getDateType()==3){
            videoTop = redisCache.getCacheList(ScreenConstants.VIDEO_NUM_MONTH);
        }

        return AjaxResult.success(videoTop);
    }

    @PostMapping(value = "/map/statis")
    public AjaxResult cameraMap(){

        return AjaxResult.success(redisCache.getCacheList(ScreenConstants.CAMERA_MAP));
    }




}
