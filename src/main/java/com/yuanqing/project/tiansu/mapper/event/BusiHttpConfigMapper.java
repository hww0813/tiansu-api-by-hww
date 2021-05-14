package com.yuanqing.project.tiansu.mapper.event;


import com.yuanqing.project.tiansu.domain.event.BusiHttpConfig;

import java.util.List;

/**
 * 接口告警阈值配置Mapper接口
 *
 * @author lvjingjing
 * @date 2021-05-12
 */
public interface BusiHttpConfigMapper
{

    /**
     * 查询接口告警阈值配置对象列表
     *
     * @param busiHttpConfig 接口告警阈值配置对象
     * @return 接口告警阈值配置对象集合
     */
    public List<BusiHttpConfig> selectBusiHttpConfigList(BusiHttpConfig busiHttpConfig);

    /**
     * 修改接口告警阈值配置对象
     *
     * @param busiHttpConfig 接口告警阈值配置对象
     * @return 结果
     */
    public int updateBusiHttpConfig(BusiHttpConfig busiHttpConfig);

}
