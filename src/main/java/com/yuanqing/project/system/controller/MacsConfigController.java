package com.yuanqing.project.system.controller;
import java.util.List;

import com.yuanqing.common.utils.StringUtils;
import com.yuanqing.common.utils.poi.ExcelUtil;
import com.yuanqing.framework.aspectj.lang.annotation.Log;
import com.yuanqing.framework.aspectj.lang.enums.BusinessType;
import com.yuanqing.framework.web.controller.BaseController;
import com.yuanqing.framework.web.domain.AjaxResult;
import com.yuanqing.framework.web.page.TableDataInfo;
import com.yuanqing.project.tiansu.domain.macs.MacsConfig;
import com.yuanqing.project.tiansu.service.macs.IMacsConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 系统配置表Controller
 *
 * @author xucan
 * @date 2020-10-21
 */

@Api("系统配置表")
@RestController

@RequestMapping("api/configuration/config")
public class MacsConfigController extends BaseController
{
    @Autowired
    private IMacsConfigService macsConfigService;

    /**
     * 查询系统配置表列表
     */
    @ApiOperation("查询系统配置表列表")
//    @PreAuthorize("@ss.hasPermi('configuration:config:list')")
    @GetMapping("/list")
    public TableDataInfo list(MacsConfig macsConfig)
    {
        startPage();
        List<MacsConfig> list = macsConfigService.selectMacsConfigList(macsConfig);
        return getDataTable(list);
    }

    /**
     * 导出系统配置表列表
     */
    @ApiOperation("导出系统配置表列表")
//    @PreAuthorize("@ss.hasPermi('configuration:config:export')")
    @Log(title = "系统配置表", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(MacsConfig macsConfig)
    {
        List<MacsConfig> list = macsConfigService.selectMacsConfigList(macsConfig);
        ExcelUtil<MacsConfig> util = new ExcelUtil<MacsConfig>(MacsConfig.class);
        return util.exportExcel(list, "config");
    }

    /**
     * 获取系统配置表详细信息
     */
    @ApiOperation("获取系统配置表详细信息")
//    @PreAuthorize("@ss.hasPermi('configuration:config:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(macsConfigService.selectMacsConfigById(id));
    }

    /**
     * 新增系统配置表
     */
    @ApiOperation("新增系统配置表")
//    @PreAuthorize("@ss.hasPermi('configuration:config:add')")
    @Log(title = "系统配置表", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MacsConfig macsConfig)
    {
        return toAjax(macsConfigService.insertMacsConfig(macsConfig));
    }

    /**
     * 修改系统配置表
     */
    @ApiOperation("修改系统配置表")
//    @PreAuthorize("@ss.hasPermi('configuration:config:edit')")
    @Log(title = "系统配置表", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody MacsConfig macsConfig)
    {
        return toAjax(macsConfigService.updateMacsConfig(macsConfig));
    }

    /**
     * 删除系统配置表
     */
    @ApiOperation("删除系统配置表")
//    @PreAuthorize("@ss.hasPermi('configuration:config:remove')")
    @Log(title = "系统配置表", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(macsConfigService.deleteMacsConfigByIds(ids));
    }

    /**
     * 根据类型和名称查询系统配置
     */
    @ApiOperation("根据类型和名称查询系统配置")
    @GetMapping("/selectMacsConfigByTypeAndName")
    public AjaxResult selectMacsConfigByTypeAndName(MacsConfig macsConfig)
    {
        if(macsConfig == null) {
            return AjaxResult.error("macsConfig为空");
        }
        if(StringUtils.isEmpty(macsConfig.getType())) {
            return AjaxResult.error("macsConfig中type为空");
        }
        if(StringUtils.isEmpty(macsConfig.getName())) {
            return AjaxResult.error("macsConfig中name为空");
        }
        MacsConfig macsConfigResult = macsConfigService.selectMacsConfigByTypeAndName(macsConfig.getType(), macsConfig.getName());
        if(macsConfigResult == null) {
            return AjaxResult.error("没有查询到指定配置");
        }
        return AjaxResult.success(macsConfigResult);
    }
}
