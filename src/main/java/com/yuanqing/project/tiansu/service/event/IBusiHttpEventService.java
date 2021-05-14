package com.yuanqing.project.tiansu.service.event;


import com.yuanqing.project.tiansu.domain.event.BusiHttpEvent;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 接口告警Service接口
 *
 * @author lvjingjing
 * @date 2021-05-13
 */
public interface IBusiHttpEventService {
    /**
     * 查询接口告警列表
     *
     * @param busiHttpEvent 接口告警
     * @return 接口告警集合
     */
    public List<BusiHttpEvent> selectBusiHttpEventList(BusiHttpEvent busiHttpEvent, Date startDate,Date endDate);

    /**
     * 勾选部分告警，确认状态
     *
     * @param ids 需要确认状态的接口告警id
     * @return 结果
     */
    public int updateBusiHttpEvent(Long[] ids);

    /**
     * 所有告警都确认状态
     *
     * @return
     */
    public int updateAllBusiHttpEvent();

    /**
     * 数据来源统计
     * @return
     */
    public List<Map<String,String>> statisticsOfDataSources(Date startDate, Date endDate);

    /**
     * 告警等级统计
     * @return
     */
    public List<Map<String,String>> alarmLevelStatistics(Date startDate,Date endDate);
}
