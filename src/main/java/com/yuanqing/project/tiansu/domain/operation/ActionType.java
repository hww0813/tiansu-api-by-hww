package com.yuanqing.project.tiansu.domain.operation;

import com.yuanqing.framework.web.enums.BaseEnum;

/**
 * 操作类型
 *
 * @author jqchu
 * @version 1.0
 * @since 2017/11/11
 **/
public enum ActionType implements BaseEnum {

    PLAY("播放视频", 0),
    DOWNLOAD("下载视频", 1),
    PLAYBACK("回放视频", 2),
    PTZ("控制设备", 3),
    SIGN_IN("登录", 4),
    SIGN_OUT("退出", 5),
    OTHER("其他操作", 6),
    SUBSCRIBE("订阅", 7),
    NOTIFY("通知", 8),
    MESSAGE("查询", 9),
    PLAY_DEFEAT("播放失败", 10);


    String label;
    Integer value;

    ActionType(String label, Integer value) {
        this.label = label;
        this.value = value;
    }
}
