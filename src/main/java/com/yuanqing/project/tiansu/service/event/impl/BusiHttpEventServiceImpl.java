package com.yuanqing.project.tiansu.service.event.impl;

import com.yuanqing.project.tiansu.domain.event.BusiHttpEvent;
import com.yuanqing.project.tiansu.mapper.event.BusiHttpEventMapper;
import com.yuanqing.project.tiansu.service.event.IBusiHttpEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 接口告警Service业务层处理
 *
 * @author lvjingjing
 * @date 2021-05-13
 */
@Service
public class BusiHttpEventServiceImpl implements IBusiHttpEventService {
    @Autowired
    private BusiHttpEventMapper busiHttpEventMapper;


    /**
     * 查询接口告警列表
     *
     * @param busiHttpEvent 接口告警
     * @return 接口告警
     */
    @Override
    public List<BusiHttpEvent> selectBusiHttpEventList(BusiHttpEvent busiHttpEvent, Date startDate, Date endDate) {
        return busiHttpEventMapper.selectBusiHttpEventList(busiHttpEvent, startDate, endDate);
    }

    /**
     * 修改接口告警
     *
     * @param ids 需要确认状态的接口告警id
     * @return 结果
     */
    @Override
    public int updateBusiHttpEvent(Long[] ids) {
        return busiHttpEventMapper.updateBusiHttpEvent(ids);
    }

    /**
     * 所有告警都确认状态
     *
     * @return
     */
    @Override
    public int updateAllBusiHttpEvent() {
        return busiHttpEventMapper.updateAllBusiHttpEvent();
    }

    /**
     * 数据来源统计
     *
     * @return
     */
    @Override
    public List<Map<String, String>> statisticsOfDataSources(Date startDate, Date endDate) {
        return busiHttpEventMapper.statisticsOfDataSources(startDate, endDate);
    }

    /**
     * 告警等级统计
     * @return
     */
    @Override
    public List<Map<String, String>> alarmLevelStatistics(Date startDate, Date endDate) {
        return busiHttpEventMapper.alarmLevelStatistics(startDate,endDate);
    }

}
