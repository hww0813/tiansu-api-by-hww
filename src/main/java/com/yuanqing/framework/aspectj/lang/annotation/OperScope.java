package com.yuanqing.framework.aspectj.lang.annotation;

import java.lang.annotation.*;

/**
 * @author xucan
 * @version 1.0
 * @Date 2021-06-18 10:48
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperScope {

    /**
     *  oper表的别名
     */
    public String operAlias() default "";


}
