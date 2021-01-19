package com.yuanqing.common.enums;

import com.yuanqing.framework.web.enums.BaseEnum;

/**
 * @author dingyong
 * @date 2019/7/10 - 17:30
 */
public enum ConnectTypeEnum implements BaseEnum {

    NOTDIRECT("非直联", 1),
    DIRECT("直联", 2);

    String label;
    Integer value;

    ConnectTypeEnum(String label, Integer value){
        this.label = label;
        this.value = value;
    }
}
