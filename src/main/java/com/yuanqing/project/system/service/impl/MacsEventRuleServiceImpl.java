package com.yuanqing.project.system.service.impl;

import java.util.List;

import com.yuanqing.common.utils.DateUtils;
import com.yuanqing.project.system.domain.MacsEventRule;
import com.yuanqing.project.system.mapper.MacsEventRuleMapper;
import com.yuanqing.project.system.service.IMacsEventRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 告警规则Service业务层处理
 *
 * @author gusheng
 * @date 2020-11-12
 */
@Service
public class MacsEventRuleServiceImpl implements IMacsEventRuleService
{
    @Autowired
    private MacsEventRuleMapper macsEventRuleMapper;

    /**
     * 查询告警规则
     *
     * @param id 告警规则ID
     * @return 告警规则
     */
    @Override
    public MacsEventRule selectMacsEventRuleById(Long id)
    {
        return macsEventRuleMapper.selectMacsEventRuleById(id);
    }

    /**
     * 查询告警规则列表
     *
     * @param macsEventRule 告警规则
     * @return 告警规则
     */
    @Override
    public List<MacsEventRule> selectMacsEventRuleList(MacsEventRule macsEventRule)
    {
        return macsEventRuleMapper.selectMacsEventRuleList(macsEventRule);
    }

    /**
     * 新增告警规则
     *
     * @param macsEventRule 告警规则
     * @return 结果
     */
    @Override
    public int insertMacsEventRule(MacsEventRule macsEventRule)
    {
        macsEventRule.setCreateTime(DateUtils.getNowDate());
        MacsEventRule rule = new MacsEventRule();
        rule.setName(macsEventRule.getName());
        List<MacsEventRule> rules = macsEventRuleMapper.selectMacsEventRuleList(rule);
        if(rules.size() > 0) {
            return 0;
        }
        return macsEventRuleMapper.insertMacsEventRule(macsEventRule);
    }

    /**
     * 修改告警规则
     *
     * @param macsEventRule 告警规则
     * @return 结果
     */
    @Override
    public int updateMacsEventRule(MacsEventRule macsEventRule)
    {
        macsEventRule.setUpdateTime(DateUtils.getNowDate());
        MacsEventRule rule = new MacsEventRule();
        Long srcId = macsEventRule.getId();
        rule.setName(macsEventRule.getName());
        List<MacsEventRule> rules = macsEventRuleMapper.selectMacsEventRuleList(rule);
        if(rules.size() > 1) {
            return 0;
        }else if (rules.size() == 1 && !rules.get(0).getId().equals(srcId)) {
            return 0;
        }
        return macsEventRuleMapper.updateMacsEventRule(macsEventRule);
    }

    /**
     * 批量删除告警规则
     *
     * @param ids 需要删除的告警规则ID
     * @return 结果
     */
    @Override
    public int deleteMacsEventRuleByIds(Long[] ids)
    {
        return macsEventRuleMapper.deleteMacsEventRuleByIds(ids);
    }

    /**
     * 删除告警规则信息
     *
     * @param id 告警规则ID
     * @return 结果
     */
    @Override
    public int deleteMacsEventRuleById(Long id)
    {
        return macsEventRuleMapper.deleteMacsEventRuleById(id);
    }
}
