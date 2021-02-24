package com.yuanqing.common.utils;

import com.alibaba.fastjson.JSONObject;

public class RegionUtil {

    public static JSONObject setRegion(JSONObject filters, String[] regionList){
        if (regionList != null) {
            String region = regionList[regionList.length - 1];
            if (regionList.length == 1) {
                int count = region.length();
                if (count == 2) {
                    filters.put("provinceRegion", region);
                }
                if (count == 4) {
                    filters.put("cityRegion", region);
                }
                if (count == 6) {
                    filters.put("countryRegion", region);
                }
            }
            if (regionList.length == 2) {
                int count = region.length();
                if (count == 4) {
                    filters.put("cityRegion", region);
                }
                if (count == 6) {
                    filters.put("countryRegion", region);
                }
            }
            if (regionList.length == 3) {
                filters.put("countryRegion", region);
            }
        }
        return filters;
    }
}
