package com.yuanqing.project.tiansu.controller.operation;

import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.yuanqing.framework.aspectj.lang.annotation.Log;
import com.yuanqing.framework.aspectj.lang.enums.BusinessType;
import com.yuanqing.project.tiansu.domain.operation.BusiHttpPerf;
import com.yuanqing.project.tiansu.service.operation.IBusiHttpPerfService;
import com.yuanqing.framework.web.controller.BaseController;
import com.yuanqing.framework.web.domain.AjaxResult;
import com.yuanqing.common.utils.poi.ExcelUtil;
import com.yuanqing.framework.web.page.TableDataInfo;

/**
 * http接口审计Controller
 *
 * @author xucan
 * @date 2021-05-17
 */
@RestController
@RequestMapping("/api/httpPerf")
public class BusiHttpPerfController extends BaseController
{
    @Autowired
    private IBusiHttpPerfService busiHttpPerfService;

    /**
     * 查询http接口审计列表
     */
    @PreAuthorize("@ss.hasPermi('api:httpPerf:list')")
    @GetMapping("/list")
    public TableDataInfo list(BusiHttpPerf busiHttpPerf)
    {
        startPage();
        List<BusiHttpPerf> list = busiHttpPerfService.selectBusiHttpPerfList(busiHttpPerf);
        return getDataTable(list);
    }

    /**
     * 导出http接口审计列表
     */
    @PreAuthorize("@ss.hasPermi('api:httpPerf:export')")
    @Log(title = "http接口审计", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(BusiHttpPerf busiHttpPerf)
    {
        List<BusiHttpPerf> list = busiHttpPerfService.selectBusiHttpPerfList(busiHttpPerf);
        ExcelUtil<BusiHttpPerf> util = new ExcelUtil<BusiHttpPerf>(BusiHttpPerf.class);
        return util.exportExcel(list, "httpPerf");
    }

    /**
     * 获取http接口审计详细信息
     */
    @PreAuthorize("@ss.hasPermi('api:httpPerf:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(busiHttpPerfService.selectBusiHttpPerfById(id));
    }

    /**
     * 新增http接口审计
     */
        @PreAuthorize("@ss.hasPermi('api:httpPerf:add')")
        @Log(title = "http接口审计", businessType = BusinessType.INSERT)
        @PostMapping
        public AjaxResult add(@RequestBody BusiHttpPerf busiHttpPerf)
        {
            return toAjax(busiHttpPerfService.insertBusiHttpPerf(busiHttpPerf));
    }

    /**
     * 修改http接口审计
     */
    @PreAuthorize("@ss.hasPermi('api:httpPerf:edit')")
    @Log(title = "http接口审计", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BusiHttpPerf busiHttpPerf)
    {
        return toAjax(busiHttpPerfService.updateBusiHttpPerf(busiHttpPerf));
    }

    /**
     * 删除http接口审计
     */
    @PreAuthorize("@ss.hasPermi('api:httpPerf:remove')")
    @Log(title = "http接口审计", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(busiHttpPerfService.deleteBusiHttpPerfByIds(ids));
    }
}
