package com.yuanqing.project.health.queue;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RunTimeMap {

    private static final Map<String, Date> map = new HashMap<>();

    /**
     * 获取当前队列大小
     *
     * @return 当前map大小
     */
    public static int size() {
        return map.size();
    }

    /**
     * put到map中
     *
     * @param name
     * @param time
     */
    public static void put(String name, Date time) {
        synchronized (map) {
            map.put(name, time);
        }
    }

    public static Date get(String name){
        synchronized (map){
            return map.get(name);
        }
    }


    /**
     * 删除数据
     *
     * @param name
     */
    public static void remove(String name) {
        synchronized (map) {
            map.remove(name);
        }
    }

    /**
     *
     * 查看是否包含
     *
     * @param name
     */

    public static boolean contain(String name){
        synchronized (map){
            return map.containsKey(name);
        }
    }


}
