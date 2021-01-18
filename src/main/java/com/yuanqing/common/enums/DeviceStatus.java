package com.yuanqing.common.enums;

import com.yuanqing.framework.web.enums.BaseEnum;

/**
 * 设备状态
 *
 * @author xucan
 **/
public enum DeviceStatus implements BaseEnum {

    CONFIRM("已确认", 0),
    NEW("新发现", 1),
    CHANGED("变更", 2),
    UNAUTHORIZED("未授权", 3);

    String label;

    Integer value;

    DeviceStatus(String label, Integer value) {
        this.label = label;
        this.value = value;
    }
}
