package com.yuanqing.common.utils;

import java.math.BigDecimal;

/**
 * @author xucan
 * @version 1.0
 * @Date 2021-03-02 16:46
 */
public class DoubleUtils {


    /**
     * 四舍五入
     * @param a 数值
     * @param b 保留小数
     * @return
     */
    public static Double roundOff(Double a,int b){
        BigDecimal bd = BigDecimal.valueOf(a);
        double rate = bd.setScale(b, BigDecimal.ROUND_HALF_UP).doubleValue();

        return rate;
    }
}
