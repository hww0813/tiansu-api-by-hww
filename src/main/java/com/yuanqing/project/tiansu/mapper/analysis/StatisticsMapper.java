package com.yuanqing.project.tiansu.mapper.analysis;

import com.alibaba.fastjson.JSONObject;
import com.yuanqing.framework.web.domain.BaseEntity;
import com.yuanqing.project.tiansu.domain.analysis.CameraVisit;
import com.yuanqing.project.tiansu.domain.analysis.TerminalVisit;
import com.yuanqing.project.tiansu.domain.analysis.VisitedRate;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author xucan
 * @version 1.0
 * @Date 2021-03-01 14:42
 */

@Repository
public interface StatisticsMapper {

    /**
     * 根据地区统计访问率
     * 根据摄像头表中的region字段分组统计
     * @param filter 过滤条件
     * @return
     */
    List<VisitedRate> visitedRate(JSONObject filter);


    /**
     * 获取终端访问列表
     * @param terminalVisit 过滤条件
     * @return
     */
    List<TerminalVisit> getTerminalVisit(TerminalVisit terminalVisit);

    /**
     * 获取摄像头访问列表
     * @param deviceCodeList 摄像头编号集合
     * @param cameraVisit 过滤条件
     * @return
     */
    List<CameraVisit> getCameraVisit(@Param("list") List<String> deviceCodeList,
                                     @Param("filter") CameraVisit cameraVisit);

}
