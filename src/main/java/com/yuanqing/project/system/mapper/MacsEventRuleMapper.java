package com.yuanqing.project.system.mapper;


import com.yuanqing.project.system.domain.MacsEventRule;

import java.util.List;

/**
 * 告警规则Mapper接口
 *
 * @author gusheng
 * @date 2020-11-12
 */
public interface MacsEventRuleMapper
{
    /**
     * 查询告警规则
     *
     * @param id 告警规则ID
     * @return 告警规则
     */
    public MacsEventRule selectMacsEventRuleById(Long id);

    /**
     * 查询告警规则列表
     *
     * @param macsEventRule 告警规则
     * @return 告警规则集合
     */
    public List<MacsEventRule> selectMacsEventRuleList(MacsEventRule macsEventRule);

    /**
     * 新增告警规则
     *
     * @param macsEventRule 告警规则
     * @return 结果
     */
    public int insertMacsEventRule(MacsEventRule macsEventRule);

    /**
     * 修改告警规则
     *
     * @param macsEventRule 告警规则
     * @return 结果
     */
    public int updateMacsEventRule(MacsEventRule macsEventRule);

    /**
     * 删除告警规则
     *
     * @param id 告警规则ID
     * @return 结果
     */
    public int deleteMacsEventRuleById(Long id);

    /**
     * 批量删除告警规则
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteMacsEventRuleByIds(Long[] ids);
}
