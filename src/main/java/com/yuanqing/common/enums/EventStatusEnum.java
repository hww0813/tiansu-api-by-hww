package com.yuanqing.common.enums;

import com.yuanqing.framework.web.enums.BaseEnum;

/**
 * 事件状态
 *
 * @author jqchu
 * @version 1.0
 * @since 2017/11/11
 **/
public enum EventStatusEnum implements BaseEnum {

    CONFIRM("已确认", 0),
    NEW("新发现", 1);

    String label;

    Integer value;

    EventStatusEnum(String label, Integer value) {
        this.label = label;
        this.value = value;
    }

    public static String getLabel(Long value){
        String valueStr = value + "";
        for (EventStatusEnum v : EventStatusEnum.values()) {
            if(v.getValue().equals(valueStr)){
                return v.getLabel();
            }
        }
        return null;
    }
}
