package com.yuanqing.project.system.controller;

import com.yuanqing.common.utils.poi.ExcelUtil;
import com.yuanqing.framework.aspectj.lang.annotation.Log;
import com.yuanqing.framework.aspectj.lang.enums.BusinessType;
import com.yuanqing.framework.web.controller.BaseController;
import com.yuanqing.framework.web.domain.AjaxResult;
import com.yuanqing.framework.web.page.PageDomain;
import com.yuanqing.framework.web.page.TableDataInfo;
import com.yuanqing.project.system.domain.MacsBwList;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.yuanqing.project.system.service.IMacsBwListService;

import java.util.List;


/**
 * 黑白名单Controller
 *
 * @author xucan
 * @date 2020-10-21
 */

@Api("配置黑白名单")
@RestController
@RequestMapping("/api/configuration/blackWhite")
public class MacsBwListController extends BaseController
{

    @Autowired
    private IMacsBwListService macsBwListService;

    @Value("${tiansu.macshost}")
    private String prefix;



    /**
     * 查询黑白名单列表
     */
    @ApiOperation("查询黑白名单列表")
//    @PreAuthorize("@ss.hasPermi('configuration:blackWhite:list')")
    @GetMapping("/list")
    public TableDataInfo list(MacsBwList macsBwList)
    {
        PageDomain pageDomain = startPage();
        List<MacsBwList> list = macsBwListService.selectMacsBwListList(macsBwList,pageDomain.getPageNum(),pageDomain.getPageSize());
        return getDataTable(list);
    }

    /**
     * 导出黑白名单列表
     */
    @ApiOperation("导出黑白名单列表")
//    @PreAuthorize("@ss.hasPermi('configuration:blackWhite:export')")
    @Log(title = "黑白名单", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(MacsBwList macsBwList)
    {
        List<MacsBwList> list = macsBwListService.selectMacsBwListList(macsBwList,null,null);
        ExcelUtil<MacsBwList> util = new ExcelUtil<MacsBwList>(MacsBwList.class);
        return util.exportExcel(list, "blackWhite");
    }

    /**
     * 获取黑白名单详细信息
     */
    @ApiOperation("获取黑白名单详细信息")
//    @PreAuthorize("@ss.hasPermi('configuration:blackWhite:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(macsBwListService.selectMacsBwListById(id));
    }

    /**
     * 新增黑白名单
     */
    @ApiOperation("新增黑白名单")
//    @PreAuthorize("@ss.hasPermi('configuration:blackWhite:add')")
    @Log(title = "黑白名单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MacsBwList macsBwList)
    {
        return macsBwListService.insertMacsBwList(macsBwList);
    }

    /**
     * 修改黑白名单
     */
    @ApiOperation("修改黑白名单")
//    @PreAuthorize("@ss.hasPermi('configuration:blackWhite:edit')")
    @Log(title = "黑白名单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MacsBwList macsBwList)
    {
        return macsBwListService.updateMacsBwList(macsBwList);
    }

    /**
     * 删除黑白名单
     */
    @ApiOperation("删除黑白名单")
//    @PreAuthorize("@ss.hasPermi('configuration:blackWhite:remove')")
    @Log(title = "黑白名单", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return macsBwListService.deleteMacsBwListByIds(ids);
    }
}
