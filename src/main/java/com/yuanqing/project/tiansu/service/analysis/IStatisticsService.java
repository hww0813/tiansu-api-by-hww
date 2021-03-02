package com.yuanqing.project.tiansu.service.analysis;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * @author xucan
 * @version 1.0
 * @Date 2021-03-01 14:40
 */
public interface IStatisticsService {

    /**
     * 获取下级地区访问率
     * @param regionId 地区代码
     * @return
     */
    List<JSONObject> getVisitedRate(String regionId);

}
