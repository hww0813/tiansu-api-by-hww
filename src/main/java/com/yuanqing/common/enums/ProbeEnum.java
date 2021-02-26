package com.yuanqing.common.enums;

import com.yuanqing.framework.web.enums.BaseEnum;

public enum ProbeEnum implements BaseEnum {
    PROBE_CAMERA("审计", 1),
    IMPORT("导入", 2);

    String label;

    Integer value;

    ProbeEnum(String label, Integer value) {
        this.label = label;
        this.value = value;
    }
}
