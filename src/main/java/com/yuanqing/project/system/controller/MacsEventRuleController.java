package com.yuanqing.project.system.controller;

import java.util.List;

import com.yuanqing.project.system.domain.MacsEventRule;
import com.yuanqing.project.system.service.IMacsEventRuleService;
import com.yuanqing.common.utils.poi.ExcelUtil;
import com.yuanqing.framework.aspectj.lang.annotation.Log;
import com.yuanqing.framework.aspectj.lang.enums.BusinessType;
import com.yuanqing.framework.web.controller.BaseController;
import com.yuanqing.framework.web.domain.AjaxResult;
import com.yuanqing.framework.web.page.TableDataInfo;
import io.swagger.annotations.Api;
import org.springframework.security.access.prepost.PreAuthorize;
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
        startPage();
        List<MacsEventRule> list = macsEventRuleService.selectMacsEventRuleList(macsEventRule);
        return getDataTable(list);
    }

    /**
     * 导出告警规则列表
     */
//    @PreAuthorize("@ss.hasPermi('system:rule:export')")
    @Log(title = "告警规则", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(MacsEventRule macsEventRule)
    {
        List<MacsEventRule> list = macsEventRuleService.selectMacsEventRuleList(macsEventRule);
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
        return AjaxResult.success(macsEventRuleService.selectMacsEventRuleById(id));
    }

    /**
     * 新增告警规则
     */
//    @PreAuthorize("@ss.hasPermi('system:rule:add')")
    @Log(title = "告警规则", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MacsEventRule macsEventRule)
    {
        if (macsEventRuleService.insertMacsEventRule(macsEventRule) == 0) {
            return AjaxResult.error("规则名重复，新增失败");
        }else {
            return AjaxResult.success("新增成功");
        }
    }

    /**
     * 修改告警规则
     */
//    @PreAuthorize("@ss.hasPermi('system:rule:edit')")
    @Log(title = "告警规则", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MacsEventRule macsEventRule)
    {
        if (macsEventRuleService.updateMacsEventRule(macsEventRule) == 0) {
            return AjaxResult.error("规则名重复，修改失败");
        }else {
            return AjaxResult.success("修改成功");
        }
    }

    /**
     * 删除告警规则
     */
//    @PreAuthorize("@ss.hasPermi('system:rule:remove')")
    @Log(title = "告警规则", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable("id") Long id)
    {
        Long []ids = {id};
        return toAjax(macsEventRuleService.deleteMacsEventRuleByIds(ids));
    }
}
