package com.yuanqing.common.queue;

import com.alibaba.fastjson.JSONObject;
import com.yuanqing.project.tiansu.domain.macs.MacsConfig;

import java.util.*;

/**
 * @author xucan
 * @version 1.0
 * @Date 2021-07-28 11:39
 */
public class MacsMap {

    private static final Map<String, List<MacsConfig>> MAP = new HashMap<>();

    /**
     * 获取当前队列大小
     *
     * @return 当前map大小
     */
    public static int size() {
        return MAP.size();
    }

    /**
     * put到map中
     *
     * @param id
     * @param data
     */
    public static void put(String id, List<MacsConfig> data) {
        synchronized (MAP) {
            MAP.put(id, data);
        }
    }

    /**
     * get
     */
    public static List<MacsConfig> get(String key) {
        synchronized (MAP) {
            return MAP.get(key);
        }
    }

    /**
     * 删除数据
     *
     * @param id
     */
    public static void remove(String id) {
        synchronized (MAP) {
            MAP.remove(id);
        }
    }

    /**
     *
     * 查看是否包含
     *
     * @param id
     */

    public static boolean contain(String id){
        synchronized (MAP){
            return MAP.containsKey(id);
        }
    }

    public static Collection<List<MacsConfig>> valueList(){
        synchronized (MAP){
            return new ArrayList<>(MAP.values());
        }
    }

    public static JSONObject valueJson(){
        synchronized (MAP){
            Set<String> keys = MAP.keySet();
            JSONObject jsonValue = new JSONObject();

            keys.stream().forEach(f ->{
                jsonValue.put(f,MAP.get(f));
            });
            return jsonValue;
        }
    }
}
