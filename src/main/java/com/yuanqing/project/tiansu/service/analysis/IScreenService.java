package com.yuanqing.project.tiansu.service.analysis;

import com.alibaba.fastjson.JSONObject;

import java.util.Date;

/**
 * @author xucan
 * @version 1.0
 * @Date 2021-04-02 10:25
 */
public interface IScreenService {

    /**
     * 统计各地区摄像头总数
     *
     * @param dateType
     * @return
     */
    public String getCameraMap(String dateType);

    /**
     * 统计时间步长:一小时
     * 范围:过去七小时
     * 获取操作行为分类(分时段)
     *
     * @param dateTime 时间
     * @return
     */
    public String getOperCategory(Date dateTime);


    /**
     * 统计时间步长:30秒
     * 范围:过去15分钟
     * 获取操作行为/告警事件(分时段)
     *
     * @param dateTime 时间
     * @return
     */
    public String getOperWarn(Date dateTime);


    /**
     * 分类统计操作行为(今日)
     *
     * @param filter
     * @return
     */
    public String getOperCount(JSONObject filter);


    /**
     * 统计活跃用户TOP(今日)
     *
     * @return
     */
    public String getUserTop(JSONObject filter);


    /**
     * 统计活跃终端TOP(今日)
     *
     * @param filter
     * @return
     */
    public String getTerminalTop(JSONObject filter);

    /**
     * 统计活跃摄像头TOP(今日)
     *
     * @param filter
     * @return
     */
    public String getCameraTop(JSONObject filter);


    /**
     * 统计告警事件条数(当日/本周/本月)
     */
    public JSONObject getWarn();

    /**
     * 资产
     *
     * @return
     */
    JSONObject getSummary();

    /**
     * 统计操作行为总数(当日/前7天/前30天)
     */
    public Integer getOperNum(Date startDate, Date endDate);

    /**
     * 接口调用数(当日/前7天/前30天)
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 在该时间范围内的接口调用数
     */
    public Integer getHttpApi(Date startDate, Date endDate);

    /**
     * 获取接口错误数
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 在该时间范围内的接口错误数
     */
    public Integer getApiErrorNum(Date startDate, Date endDate);

    /**
     * 获取接口超时数
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 在该时间范围内的接口超时数
     */
    public Integer getApiOverTime(Date startDate, Date endDate);
}
