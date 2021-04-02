package com.yuanqing.common.constant;

/**
 *
 * 大屏统计缓存key
 * @author xucan
 * @version 1.0
 * @Date 2021-04-02 16:41
 */
public class ScreenConstants {

    /**
     * 前缀
     */
    public static final String PREFIX = "screen:";

    /**
     * 1.总资产
     */
    public static final String SUMMARY = PREFIX + "summary";

    /**
     * 2.告警数
     */
    public static final String WARN = PREFIX + "warn";

    /**
     * 3.用户TOP 天/周/月
     */
    public static final String USER_DAY = PREFIX + "userTop:day";
    public static final String USER_WEEK = PREFIX + "userTop:week";
    public static final String USER_MONTH = PREFIX + "userTop:month";

    /**
     * 4.终端TOP 天/周/月
     */
    public static final String TERMINAL_DAY = PREFIX + "terminalTop:day";
    public static final String TERMINAL_WEEK = PREFIX + "terminalTop:week";
    public static final String TERMINAL_MONTH = PREFIX + "terminalTop:month";

    /**
     * 5.摄像头TOP 天/周/月
     */
    public static final String CAMERA_DAY = PREFIX + "cameraTop:day";
    public static final String CAMERA_WEEK = PREFIX + "cameraTop:week";
    public static final String CAMERA_MONTH = PREFIX + "cameraTop:month";

    /**
     * 6.操作行为类别占比（过去7小时的操作行为动作统计）
     */
    public static final String OPERATION_CATEGORY = PREFIX + "operationCategory";

    /**
     * 7.操作行为类别数量 天/周/月
     */
    public static final String OPERATION_NUM_DAY = PREFIX + "operationNum:day";
    public static final String OPERATION_NUM_WEEK = PREFIX + "operationNum:week";
    public static final String OPERATION_NUM_MONTH = PREFIX + "operationNum:month";

    /**
     * 8.摄像头区域统计（数据大屏地图）
     */
    public static final String CAMERA_MAP = PREFIX + "cameraMap";

    /**
     * 9,视频TOP
     */
    public static final String VIDEO_NUM_DAY = PREFIX + "videoNum:day";
    public static final String VIDEO_NUM_WEEK = PREFIX + "videoNum:week";
    public static final String VIDEO_NUM_MONTH = PREFIX + "videoNum:month";

    /**
     * 10.实时操作行为和告警
     */
    public static final String REAL_OPERATION_WARN = PREFIX + "realOperationWarn";

}
