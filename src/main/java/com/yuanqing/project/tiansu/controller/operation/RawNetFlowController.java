package com.yuanqing.project.tiansu.controller.operation;


import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yuanqing.common.constant.HttpStatus;
import com.yuanqing.common.utils.StringUtils;
import com.yuanqing.common.utils.ip.IpUtils;
import com.yuanqing.common.utils.poi.ExcelUtil;
import com.yuanqing.common.utils.sql.SqlUtil;
import com.yuanqing.framework.aspectj.lang.annotation.Log;
import com.yuanqing.framework.aspectj.lang.enums.BusinessType;
import com.yuanqing.framework.web.controller.BaseController;
import com.yuanqing.framework.web.domain.AjaxResult;
import com.yuanqing.framework.web.domain.PageResult;
import com.yuanqing.framework.web.page.PageDomain;
import com.yuanqing.framework.web.page.TableDataInfo;
import com.yuanqing.framework.web.page.TableSupport;
import com.yuanqing.project.tiansu.domain.assets.Camera;
import com.yuanqing.project.tiansu.domain.operation.RawNetFlow;
import com.yuanqing.project.tiansu.service.operation.IRawNetFlowService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping(value = "/api/rawNetFlow")
public class RawNetFlowController extends BaseController {

    @Autowired
    private IRawNetFlowService busiRawNetFlowService;

    /**
     * 查询原始流量列表
     */
    @GetMapping("/list")
    @ApiOperation(value = "获取原始信令列表", httpMethod = "GET")
    public AjaxResult getAll(@RequestParam(value = "srcIp", required = false) String srcIp,
                             @RequestParam(value = "dstIp", required = false) String dstIp,
                             @RequestParam(value = "stamp", required = false) LocalDateTime stamp,
                             @RequestParam(required = false) String orderType,
                             @RequestParam(required = false) String orderValue) {
        RawNetFlow rawNetFlow = new RawNetFlow();
        rawNetFlow.setSrcIp(IpUtils.ipToLong(srcIp));
        rawNetFlow.setDstIp(IpUtils.ipToLong(dstIp));
        rawNetFlow.setStamp(stamp);
        startPage();
//        if (StringUtils.isNotBlank(orderValue) && StringUtils.isNotBlank(orderType)) {
//            rawNetFlow.setOrderType(orderValue + " " + orderType);
//        }
        List<RawNetFlow> rawNetFlowList = busiRawNetFlowService.selectBusiRawNetFlowList(rawNetFlow);

        return AjaxResult.success(getDataTable(rawNetFlowList));
    }
    /**
     * 导出原始流量列表
     */
    @PreAuthorize("@ss.hasPermi('system:flow:export')")
    @Log(title = "原始流量", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(RawNetFlow busiRawNetFlow)
    {
        List<RawNetFlow> list = busiRawNetFlowService.selectBusiRawNetFlowList(busiRawNetFlow);
        ExcelUtil<RawNetFlow> util = new ExcelUtil<RawNetFlow>(RawNetFlow.class);
        return util.exportExcel(list, "flow");
    }


    /**
     * 获取服务器流量趋势统计
     */
    @GetMapping("/ServerFlowTrend")
    @ApiOperation(value = "获取原始信令列表", httpMethod = "GET")
    public AjaxResult getServerFlowTrend(@RequestParam(value = "dstIp", required = false) String dstIp,
                             @RequestParam(required = false) String orderType,
                             @RequestParam(required = false) String orderValue) {
        RawNetFlow rawNetFlow = new RawNetFlow();
        rawNetFlow.setDstIp(IpUtils.ipToLong(dstIp));
        startPage();
        List<JSONObject> rawNetFlowList = busiRawNetFlowService.getServerFlowTrend(rawNetFlow);
        return AjaxResult.success(rawNetFlowList);
    }

    /**
     * 获取服务器流量相关终端
     */
    @GetMapping("/ServerFlowRelationClient")
    @ApiOperation(value = "获取原始信令列表", httpMethod = "GET")
    public AjaxResult getServerFlowRelationClient(@RequestParam(value = "dstIp", required = false) String dstIp,
                                                  @RequestParam(value = "stamp", required = false) String stamp,
                                            @RequestParam(required = false) String orderType,
                                            @RequestParam(required = false) String orderValue) {
        RawNetFlow rawNetFlow = new RawNetFlow();
        rawNetFlow.setDstIp(IpUtils.ipToLong(dstIp));
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse(stamp,df);
        rawNetFlow.setStamp(localDateTime);
        startPage();
        List<JSONObject> rawNetFlowList = busiRawNetFlowService.getServerFlowRelationClient(rawNetFlow);
        return AjaxResult.success(rawNetFlowList);
    }



//    /**
//     * 设置请求分页数据
//     */
//    protected void startPage()
//    {
//        PageDomain pageDomain = TableSupport.buildPageRequest();
//        Integer pageNum = pageDomain.getPageNum();
//        Integer pageSize = pageDomain.getPageSize();
//        if (StringUtils.isNotNull(pageNum) && StringUtils.isNotNull(pageSize))
//        {
//            String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
//            PageHelper.startPage(pageNum, pageSize, orderBy);
//        }
//    }

//    /**
//     * 响应请求分页数据
//     */
//    @SuppressWarnings({ "rawtypes", "unchecked" })
//    protected TableDataInfo getDataTable(List<?> list)
//    {
//
//
//        TableDataInfo rspData = new TableDataInfo();
//
//        if(CollectionUtils.isEmpty(list)){
//            rspData.setCode(HttpStatus.SUCCESS);
//            rspData.setList(null);
//            rspData.setTotal(0);
//            return rspData;
//        }else{
//            rspData.setCode(HttpStatus.SUCCESS);
//            rspData.setList(list);
//            rspData.setTotal(new PageInfo(list).getTotal());
//            return rspData;
//        }
//    }

//    /**
//     * 获取原始流量详细信息
//     */
//    @PreAuthorize("@ss.hasPermi('system:flow:query')")
//    @GetMapping(value = "/{id}")
//    public AjaxResult getInfo(@PathVariable("id") Long id)
//    {
//        return AjaxResult.success(busiRawNetFlowService.selectBusiRawNetFlowById(id));
//    }
//
//    /**
//     * 新增原始流量
//     */
//    @PreAuthorize("@ss.hasPermi('system:flow:add')")
//    @Log(title = "原始流量", businessType = BusinessType.INSERT)
//    @PostMapping
//    public AjaxResult add(@RequestBody RawNetFlow busiRawNetFlow)
//    {
//        return toAjax(busiRawNetFlowService.insertBusiRawNetFlow(busiRawNetFlow));
//    }
//
//    /**
//     * 修改原始流量
//     */
//    @PreAuthorize("@ss.hasPermi('system:flow:edit')")
//    @Log(title = "原始流量", businessType = BusinessType.UPDATE)
//    @PutMapping
//    public AjaxResult edit(@RequestBody RawNetFlow busiRawNetFlow)
//    {
//        return toAjax(busiRawNetFlowService.updateBusiRawNetFlow(busiRawNetFlow));
//    }
//
//    /**
//     * 删除原始流量
//     */
//    @PreAuthorize("@ss.hasPermi('system:flow:remove')")
//    @Log(title = "原始流量", businessType = BusinessType.DELETE)
//    @DeleteMapping("/{ids}")
//    public AjaxResult remove(@PathVariable Long[] ids)
//    {
//        return toAjax(busiRawNetFlowService.deleteBusiRawNetFlowByIds(ids));
//    }
}
