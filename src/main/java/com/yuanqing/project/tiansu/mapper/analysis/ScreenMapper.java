package com.yuanqing.project.tiansu.mapper.analysis;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 大屏统计
 * @author xucan
 * @version 1.0
 * @Date 2021-04-02 10:25
 */
@Repository
public interface ScreenMapper {


    /**
     * 获取终端TOP
     * @param filters
     * @return
     */
    List<JSONObject> getTerminalTop(JSONObject filters);

    /**
     * 获取摄像头Top
     * @return
     */
    List<JSONObject> getCameraTop(JSONObject filter);

    /**
     * 获取用户Top
     * @param filters
     * @return
     */
    List<JSONObject> getUserTop(JSONObject filters);

    /**
     * 获取操作行为分类
     * @param filters
     * @return
     */
    List<JSONObject> getOperCount(JSONObject filters);


    /**
     * 获取操作行为15分钟内 每分钟的条数
     * @return
     */
    List<JSONObject> getOperWarnByOper();

    /**
     * 过去七小时操作行为分类统计
     * @return
     */
    List<JSONObject> getOperCategory();

}
