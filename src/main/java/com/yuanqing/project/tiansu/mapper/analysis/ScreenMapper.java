package com.yuanqing.project.tiansu.mapper.analysis;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * 大屏统计
 * @author xucan
 * @version 1.0
 * @Date 2021-04-02 10:25
 */

public interface ScreenMapper {

    JSONObject getWarn();

    List<JSONObject> getTerminalTop(JSONObject filters);
    List<JSONObject> getTerminalTopByWeekOrMonth(JSONObject filters);

    List<JSONObject> getCameraTop();
    List<JSONObject> getCameraTopByWeekOrMonth(JSONObject filters);

    List<JSONObject> getUserTop(JSONObject filters);

    List<JSONObject> getOperCountByDay(JSONObject filters);

    List<JSONObject> getOperWarnByWarn(JSONObject filters);

    List<JSONObject> getOperWarnByOper(JSONObject filters);

    /**
     * 过去七小时操作行为分类统计
     * @return
     */
    List<JSONObject> getOperCategory();

}
