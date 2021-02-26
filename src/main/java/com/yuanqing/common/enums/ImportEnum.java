package com.yuanqing.common.enums;

import com.yuanqing.framework.web.enums.BaseEnum;

public enum ImportEnum implements BaseEnum {
    IMPORT_CAMERA("导入", 1),
    N_IMPORT_CAMERA("非导入", 0);

    String label;

    Integer value;

    ImportEnum(String label, Integer value) {
        this.label = label;
        this.value = value;
    }
}
