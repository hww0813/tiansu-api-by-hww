package com.yuanqing.project.tiansu.service.event;



import com.yuanqing.project.tiansu.domain.event.BusiHttpConfig;

import java.util.List;


/**
 * 接口告警阈值配置Service接口
 *
 * @author lvjingjing
 * @date 2021-05-12
 */
public interface IBusiHttpConfigService
{

    /**
     * 查询接口告警阈值配置列表
     *
     * @param busiHttpConfig 接口告警阈值配置
     * @return 接口告警阈值配置集合
     */
    public List<BusiHttpConfig> selectBusiHttpConfigList(BusiHttpConfig busiHttpConfig);


    /**
     * 修改接口告警阈值配置
     *
     * @param busiHttpConfig 接口告警阈值配置
     * @return 结果
     */
    public int updateBusiHttpConfig(BusiHttpConfig busiHttpConfig);

}
