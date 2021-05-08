package com.yuanqing.project.tiansu.domain.statistics;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @author Dong.Chao
 * @Classname StatisticsSearCh
 * @Description
 * @Date 2021/2/5 14:13
 * @Version V1.0
 */
@Data
@Builder
public class StatisticsSearch implements Cloneable{

    private Date startTime;

    private Date endTime;

    private Long action;

    private String ascFlag;


    /** 默认10条*/
    private Long size;


    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
