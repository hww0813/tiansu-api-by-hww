package com.yuanqing.project.tiansu.controller.assets;

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
import com.yuanqing.project.tiansu.domain.assets.BusiServerRemark;
import com.yuanqing.project.tiansu.service.assets.IBusiServerRemarkService;
import com.yuanqing.framework.web.controller.BaseController;
import com.yuanqing.framework.web.domain.AjaxResult;
import com.yuanqing.common.utils.poi.ExcelUtil;
import com.yuanqing.framework.web.page.TableDataInfo;

/**
 * 服务标注Controller
 *
 * @author xucan
 * @date 2021-05-18
 */
@RestController
@RequestMapping("/serverRemark/remark")
public class BusiServerRemarkController extends BaseController
{
    @Autowired
    private IBusiServerRemarkService busiServerRemarkService;

    /**
     * 查询服务标注列表
     */
    @GetMapping("/list")
    public TableDataInfo list(BusiServerRemark busiServerRemark)
    {
        startPage();
        List<BusiServerRemark> list = busiServerRemarkService.selectBusiServerRemarkList(busiServerRemark);
        return getDataTable(list);
    }

    /**
     * 导出服务标注列表
     */
    @Log(title = "服务标注", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(BusiServerRemark busiServerRemark)
    {
        List<BusiServerRemark> list = busiServerRemarkService.selectBusiServerRemarkList(busiServerRemark);
        ExcelUtil<BusiServerRemark> util = new ExcelUtil<BusiServerRemark>(BusiServerRemark.class);
        return util.exportExcel(list, "remark");
    }

    /**
     * 获取服务标注详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(busiServerRemarkService.selectBusiServerRemarkById(id));
    }

    /**
     * 新增服务标注
     */
    @Log(title = "服务标注", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BusiServerRemark busiServerRemark)
    {
        return toAjax(busiServerRemarkService.insertBusiServerRemark(busiServerRemark));
    }

    /**
     * 修改服务标注
     */
    @Log(title = "服务标注", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BusiServerRemark busiServerRemark)
    {
        return toAjax(busiServerRemarkService.updateBusiServerRemark(busiServerRemark));
    }

    /**
     * 删除服务标注
     */
    @Log(title = "服务标注", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(busiServerRemarkService.deleteBusiServerRemarkByIds(ids));
    }
}
