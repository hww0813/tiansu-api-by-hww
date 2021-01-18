package com.yuanqing.common.enums;

import com.yuanqing.framework.web.enums.BaseEnum;

/**
 * 国标枚举
 * @author xucan
 **/
public enum GBEnum implements BaseEnum {

    GB("国标", 1),
    NGB("非国标", 0);

    String label;

    Integer value;

    GBEnum(String label, Integer value) {
        this.label = label;
        this.value = value;
    }
}
