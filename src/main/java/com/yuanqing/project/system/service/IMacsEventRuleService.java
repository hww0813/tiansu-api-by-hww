package com.yuanqing.project.system.service;

import com.alibaba.fastjson.JSONObject;
import com.yuanqing.framework.web.domain.AjaxResult;
import com.yuanqing.project.system.domain.MacsEventRule;


/**
 * 告警规则Service接口
 *
 * @author gusheng
 * @date 2020-11-12
 */
public interface IMacsEventRuleService
{
    /**
     * 查询告警规则
     *
     * @param id 告警规则ID
     * @return 告警规则
     */
    public AjaxResult selectMacsEventRuleById(Long id);

    /**
     * 查询告警规则列表
     *
     * @param macsEventRule 告警规则
     * @return 告警规则集合
     */
    public JSONObject selectMacsEventRuleList(MacsEventRule macsEventRule, Integer pageNum, Integer pageSize);

    /**
     * 新增告警规则
     *
     * @param macsEventRule 告警规则
     * @return 结果
     */
    public AjaxResult insertMacsEventRule(MacsEventRule macsEventRule);

    /**
     * 修改告警规则
     *
     * @param macsEventRule 告警规则
     * @return 结果
     */
    public AjaxResult updateMacsEventRule(MacsEventRule macsEventRule);

    /**
     * 批量删除告警规则
     *
     * @param macsEventRule 需要删除的告警规则
     * @return 结果
     */
    public AjaxResult deleteMacsEventRuleByIds(MacsEventRule macsEventRule);

    /**
     * 删除告警规则信息
     *
     * @param id 告警规则ID
     * @return 结果
     */
//    public int deleteMacsEventRuleById(Long id);
}
