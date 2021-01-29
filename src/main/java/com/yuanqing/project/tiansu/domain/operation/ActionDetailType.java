package com.yuanqing.project.tiansu.domain.operation;

import com.yuanqing.framework.web.enums.BaseEnum;

/**
 * 操作详细类型
 *
 * @author jqchu
 * @version 1.0
 * @since 2017/11/11
 **/
public enum ActionDetailType implements BaseEnum {

    PLAY("实时播放", 0),
    PLAYBACK("回放视频", 1),
    DOWNLOAD("下载视频", 2),
    SIGN_IN("登录", 3),
    SIGN_OUT("注销", 4),
    CATALOG("查看目录", 5),
    PTZ_UP("控制向上", 6),
    PTZ_DOWN("控制向下", 7),
    PTZ_LEFT("控制向左", 8),
    PTZ_RIGHT("控制向右", 9),
    UNKNOWN("其他", 10),
    PTZ_ZOOM_IN("放大", 11),
    PTZ_ZOOM_OUT("缩小", 12),
    SUBSCRIBE_ALARM("警报订阅", 13),
    SUBSCRIBE_CATALOG("目录订阅", 14),
    NOTIFY_ALARM("警报通知", 15),
    NOTIFY_CATALOG("目录通知", 16),
    MESSAGE_ALARM("警报查询", 17),
    MESSAGE_CATALOG("目录查询", 18),
    MESSAGE_DEVICE_INFO("设备信息查询", 19),
    MESSAGE_KEEPALIVE("设备状态查询", 20),
    MESSAGE_RECORD_INFO("文件检索", 21),
    PLAY_DEFEAT("播放失败", 22),
    PLAY_STOP("停止播放", 23),
    DOWNLOAD_STOP("停止下载", 24),
    PLAYBACK_STOP("停止回放", 25),
    PTZ_UP_LEFT("控制左上",26),
    PTZ_UP_RIGHT("控制右上", 27),
    PTZ_DOWN_LEFT("控制下左", 28),
    PTZ_DOWN_RIGHT("控制下右", 29),
    PTZ_STOP("控制停止", 30);

    String label;
    Integer value;

    ActionDetailType(String label, Integer value) {
        this.label = label;
        this.value = value;
    }
}
