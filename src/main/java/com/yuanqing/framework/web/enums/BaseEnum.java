package com.yuanqing.framework.web.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

/**
 * 基础枚举类型
 *
 * @author jqchu
 * @version 1.0
 * @since 2017.12.17
 **/
public interface BaseEnum {

    String DEFAULT_VALUE_NAME = "value";

    String DEFAULT_LABEL_NAME = "label";


    default String getValue() {
        Field field = ReflectionUtils.findField(this.getClass(), DEFAULT_VALUE_NAME);
        if (field == null)
            return null;
        try {
            field.setAccessible(true);
            return field.get(this).toString();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @JsonValue
    default String getLabel() {
        Field field = ReflectionUtils.findField(this.getClass(), DEFAULT_LABEL_NAME);
        if (field == null)
            return null;
        try {
            field.setAccessible(true);
            return field.get(this).toString();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    static <T extends Enum<T>> T valueOfEnum(Class<T> enumClass, String value) {
        if (value == null)
            throw new IllegalArgumentException("BaseEnum value should not be null");
        if (enumClass.isAssignableFrom(BaseEnum.class))
            throw new IllegalArgumentException("illegal BaseEnum type");
        T[] enums = enumClass.getEnumConstants();
        for (T t : enums) {
            BaseEnum baseEnum = (BaseEnum) t;
            if (baseEnum.getValue().equals(value))
                return (T) baseEnum;
        }
        throw new IllegalArgumentException("cannot parse : " + value + " to " + enumClass.getName());
    }
}
