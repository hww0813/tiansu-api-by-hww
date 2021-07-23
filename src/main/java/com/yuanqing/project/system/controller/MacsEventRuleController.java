package com.yuanqing.project.system.controller;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yuanqing.framework.web.page.PageDomain;
import com.yuanqing.project.system.domain.MacsEventRule;
import com.yuanqing.project.system.service.IMacsEventRuleService;
import com.yuanqing.common.utils.poi.ExcelUtil;
import com.yuanqing.framework.aspectj.lang.annotation.Log;
import com.yuanqing.framework.aspectj.lang.enums.BusinessType;
import com.yuanqing.framework.web.controller.BaseController;
import com.yuanqing.framework.web.domain.AjaxResult;
import com.yuanqing.framework.web.page.TableDataInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 告警规则Controller
 *
 * @author gusheng
 * @date 2020-11-12
 */
@RestController
@RequestMapping("/api/configuration/rule")
@CrossOrigin
public class MacsEventRuleController extends BaseController
{
    @Autowired
    private IMacsEventRuleService macsEventRuleService;

    /**
     * 查询告警规则列表
     */
    @GetMapping("/list")
    public TableDataInfo list(MacsEventRule macsEventRule)
    {
        PageDomain pageDomain = startPage();
        JSONObject result = macsEventRuleService.selectMacsEventRuleList(macsEventRule, pageDomain.getPageNum(), pageDomain.getPageSize());
        TableDataInfo tableDataInfo = new TableDataInfo();
        tableDataInfo.setList(result.getJSONArray("rows"));
        tableDataInfo.setTotal(result.getInteger("total"));
        tableDataInfo.setMsg(result.getInteger("msg"));
        tableDataInfo.setCode(result.getInteger("code"));
        return tableDataInfo;
    }

    /**
     * 导出告警规则列表
     */
//    @PreAuthorize("@ss.hasPermi('system:rule:export')")
    @Log(title = "告警规则", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(MacsEventRule macsEventRule)
    {
        JSONObject result = macsEventRuleService.selectMacsEventRuleList(macsEventRule, null, null);
        List<MacsEventRule> list = JSONArray.parseArray(result.getString("rows"), MacsEventRule.class);
        ExcelUtil<MacsEventRule> util = new ExcelUtil<MacsEventRule>(MacsEventRule.class);
        return util.exportExcel(list, "rule");
    }

    /**
     * 获取告警规则详细信息
     */
//    @PreAuthorize("@ss.hasPermi('system:rule:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return macsEventRuleService.selectMacsEventRuleById(id);
    }

    /**
     * 新增告警规则
     */
//    @PreAuthorize("@ss.hasPermi('system:rule:add')")
    @Log(title = "告警规则", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MacsEventRule macsEventRule)
    {
        return macsEventRuleService.insertMacsEventRule(macsEventRule);
    }

    /**
     * 修改告警规则
     */
//    @PreAuthorize("@ss.hasPermi('system:rule:edit')")
    @Log(title = "告警规则", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MacsEventRule macsEventRule)
    {
        return macsEventRuleService.updateMacsEventRule(macsEventRule);
    }

    /**
     * 删除告警规则
     */
//    @PreAuthorize("@ss.hasPermi('system:rule:remove')")
    @Log(title = "告警规则", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable("id") Long id)
    {
        MacsEventRule macsEventRule = new MacsEventRule();
        return macsEventRuleService.deleteMacsEventRuleByIds(macsEventRule);
    }
}
