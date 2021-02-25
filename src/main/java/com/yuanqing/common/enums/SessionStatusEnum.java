package com.yuanqing.common.enums;

import com.yuanqing.framework.web.enums.BaseEnum;

/**
 * @author yipengfei
 * @date 2019/8/01 - 15:45
 */

public enum SessionStatusEnum implements BaseEnum {

    PROCESS("进行", 1),
    FINISH("结束", 2);

    String label;
    Integer value;

    SessionStatusEnum(String label, Integer value){
        this.label = label;
        this.value = value;
    }
}
