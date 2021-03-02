package com.yuanqing.project.tiansu.mapper.analysis;

import com.yuanqing.project.tiansu.domain.analysis.VisitedRate;
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
     * @return
     */
    List<VisitedRate> visitedRate();

}
