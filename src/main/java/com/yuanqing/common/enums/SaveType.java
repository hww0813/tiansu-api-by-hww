package com.yuanqing.common.enums;

/**
 *
 * 保存方法
 * @author xucan
 * @version 1.0
 * @Date 2021-02-05 10:49
 */
public enum SaveType {

    UPDATE(0, "更新"), INSERT(1, "插入");

    private final Integer code;
    private final String info;

    SaveType(Integer code, String info)
    {
        this.code = code;
        this.info = info;
    }

    public Integer getCode()
    {
        return code;
    }

    public String getInfo()
    {
        return info;
    }
}
