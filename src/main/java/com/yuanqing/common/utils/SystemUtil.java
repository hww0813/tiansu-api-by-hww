package com.yuanqing.common.utils;

public class SystemUtil {

    public static boolean currSystem(){
        String system = System.getProperty("os.name");
        if ("Linux".equals(system)) return true;
        return false;
    }
    public static boolean macSystem(){
        String system = System.getProperty("os.name");
        if (system.contains("Mac")) return true;
        return false;
    }
}
