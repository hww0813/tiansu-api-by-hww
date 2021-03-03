package com.yuanqing.project.tiansu.service.analysis;

import com.alibaba.fastjson.JSONObject;
import com.yuanqing.project.tiansu.domain.analysis.TerminalVisit;

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

    /**
     * 获取终端访问列表
     * @param terminalVisit 过滤条件
     * @return
     */
    List<TerminalVisit> getTerminalVisit(TerminalVisit terminalVisit);
}
