package com.yuanqing.project.tiansu.domain.analysis;

import lombok.Data;

/**
 * 访问率分析实体类
 * @author xucan
 * @version 1.0
 * @Date 2021-03-02 14:36
 */

@Data
public class VisitedRate {

    private Long regionId;

    private Integer visitedCamera;

    private Integer terminalCnt;

    private Integer userCnt;

    private Integer allCount;

    private Integer visitedCnt;
}
