package com.yuanqing.common.enums;

import com.yuanqing.framework.web.enums.BaseEnum;

/**
 * 设备类型
 *
 * @author hywang
 * @version 1.0
 * @since 2018/05/11
 **/
public enum SelfAuditSeverityEnum implements BaseEnum {

    COMMON("一般", 1),
    MAJOR("重要", 2),
    SERIOUS("严重", 3);

    String label;

    Integer value;

    SelfAuditSeverityEnum(String label, Integer value) {
        this.label = label;
        this.value = value;
    }
}
