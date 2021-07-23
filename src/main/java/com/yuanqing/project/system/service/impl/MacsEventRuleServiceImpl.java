package com.yuanqing.project.system.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.yuanqing.framework.web.domain.AjaxResult;
import com.yuanqing.project.system.domain.MacsEventRule;
import com.yuanqing.project.system.service.IMacsEventRuleService;
import com.yuanqing.project.tiansu.service.feign.MacsFeignClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 告警规则Service业务层处理
 *
 * @author gusheng
 * @date 2020-11-12
 */
@Service
public class MacsEventRuleServiceImpl implements IMacsEventRuleService
{

    @Resource
    private MacsFeignClient macsFeignClient;

    public static final String MACS_TOKEN = "BearereyJhbGciOiJIUzUxMiJ9.eyJsb2dpbl91c2VyX2tleSI6ImUzNjQ4YmI0LWQyMWEtNDRmZi05Mj" +
            "c0LWJjMDcxMDBjMzgzOSJ9.Ix7KKtW5Q4UZCbKgK5roz0y7xv7z5a_tb37k8alIwuAG9uiga6R6dBuCDEsx8HWqlUnXTVqNxHRaeo_6RY_e-w";

    /**
     * 查询告警规则
     *
     * @param id 告警规则ID
     * @return 告警规则
     */
    @Override
    public AjaxResult selectMacsEventRuleById(Long id)
    {

        return macsFeignClient.selectMacsBwListById(id, MACS_TOKEN);
    }

    /**
     * 查询告警规则列表
     *
     * @param macsEventRule 告警规则
     * @return 告警规则
     */
    @Override
    public JSONObject selectMacsEventRuleList(MacsEventRule macsEventRule, Integer pageNum,Integer pageSize)
    {
        JSONObject rspJson = macsFeignClient.getEventRuleList(macsEventRule, pageNum, pageSize, MACS_TOKEN);
        return rspJson;
    }

    /**
     * 新增告警规则
     *
     * @param macsEventRule 告警规则
     * @return 结果
     */
    @Override
    public AjaxResult insertMacsEventRule(MacsEventRule macsEventRule)
    {
        return macsFeignClient.addEventRule(macsEventRule, MACS_TOKEN);
    }

    /**
     * 修改告警规则
     *
     * @param macsEventRule 告警规则
     * @return 结果
     */
    @Override
    public AjaxResult updateMacsEventRule(MacsEventRule macsEventRule)
    {
        return macsFeignClient.editEventRule(macsEventRule, MACS_TOKEN);
    }

    /**
     * 批量删除告警规则
     *
     * @param macsEventRule 需要删除的告警规则
     * @return 结果
     */
    @Override
    public AjaxResult deleteMacsEventRuleByIds(MacsEventRule macsEventRule)
    {

        return macsFeignClient.removeEventRule(macsEventRule, MACS_TOKEN);
    }

    /**
     * 删除告警规则信息
     *
     * @param id 告警规则ID
     * @return 结果
     */
//    @Override
//    public int deleteMacsEventRuleById(Long id)
//    {
//        return macsEventRuleMapper.deleteMacsEventRuleById(id);
//    }
}
