package com.yuanqing.project.tiansu.controller.operation;


import com.alibaba.fastjson.JSONObject;
import com.yuanqing.common.utils.DateUtils;
import com.yuanqing.common.utils.StringUtils;
import com.yuanqing.common.utils.ip.IpUtils;
import com.yuanqing.common.utils.poi.ExcelUtil;
import com.yuanqing.framework.aspectj.lang.annotation.Log;
import com.yuanqing.framework.aspectj.lang.enums.BusinessType;
import com.yuanqing.framework.web.controller.BaseController;
import com.yuanqing.framework.web.domain.AjaxResult;
import com.yuanqing.framework.web.page.TableDataInfo;
import com.yuanqing.project.tiansu.domain.operation.RawNetFlow;
import com.yuanqing.project.tiansu.service.operation.IRawNetFlowService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

/**
 * @author: lvjingjing
 * @date: 2021/4/27 17:25
 * @introduce: 原始流量列表接口
 */
@RestController
@RequestMapping(value = "/api/rawNetFlow")
@Api(value = "原始流量列表接口", description = "原始流量列表相关API")
public class RawNetFlowController extends BaseController {

    @Autowired
    private IRawNetFlowService busiRawNetFlowService;

    /**
     * 查询原始流量列表
     */
    @GetMapping("/list")
    @ApiOperation(value = "获取原始信令列表", httpMethod = "GET")
    public AjaxResult getAll(@ApiParam("源IP")@RequestParam(value = "srcIp", required = false) String srcIp,
                             @ApiParam("目的IP")@RequestParam(value = "dstIp", required = false) String dstIp,
                             @ApiParam("开始时间")@RequestParam(value = "startDate", required = false) String startDate,
                             @ApiParam("结束时间")@RequestParam(value = "endDate", required = false) String endDate,
                             @ApiParam("排序")@RequestParam(required = false) String orderType,
                             @ApiParam("排序对象")@RequestParam(required = false) String orderValue) {
        RawNetFlow rawNetFlow = new RawNetFlow();
        rawNetFlow.setSrcIp(IpUtils.ipToLong(srcIp));
        rawNetFlow.setDstIp(IpUtils.ipToLong(dstIp));
        rawNetFlow.setstartDate(startDate);
        rawNetFlow.setendDate(endDate);
        startPage();
        if (StringUtils.isNotBlank(orderValue) && StringUtils.isNotBlank(orderType)) {
            rawNetFlow.setOrderType(orderValue + " " + orderType);
        }
        List<RawNetFlow> rawNetFlowList = busiRawNetFlowService.selectBusiRawNetFlowList(rawNetFlow);

        return AjaxResult.success(getDataTable(rawNetFlowList));
    }

    /**
     * 导出原始流量列表
     */
    @PreAuthorize("@ss.hasPermi('system:flow:export')")
    @Log(title = "原始流量", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(@ApiParam("原始流量") RawNetFlow busiRawNetFlow) {
        List<RawNetFlow> list = busiRawNetFlowService.selectBusiRawNetFlowList(busiRawNetFlow);
        ExcelUtil<RawNetFlow> util = new ExcelUtil<RawNetFlow>(RawNetFlow.class);
        return util.exportExcel(list, "flow");
    }


    /**
     * 获取服务器流量趋势统计
     */
    @GetMapping("/ServerFlowTrend")
    @ApiOperation(value = "获取服务器流量趋势统计", httpMethod = "GET")
    public AjaxResult getServerFlowTrend(@ApiParam("目的IP")@RequestParam(value = "dstIp", required = false) String dstIp) throws ParseException {
        //ip地址转换
        Long ip = IpUtils.ipToLong(dstIp);
        //获得开始结束时间时间段
        Date endTime = new Date();
        Date startTime = DateUtils.getStartDate(endTime);

        List<JSONObject> rawNetFlowList = busiRawNetFlowService.getServerFlowTrend(ip, startTime, endTime);
        return AjaxResult.success(rawNetFlowList);
    }

    /**
     * 获取终端流量趋势统计
     */
    @GetMapping("/getClientRawFlowTrend")
    @ApiOperation(value = "获取终端流量趋势统计", httpMethod = "GET")
    public AjaxResult getClientRawFlowTrend(@ApiParam("源IP")@RequestParam(value = "srcIp", required = false) String srcIp) throws ParseException {
        //ip地址转换
        Long ip = IpUtils.ipToLong(srcIp);
        //获得开始结束时间时间段
        Date endTime = new Date();
        Date startTime = DateUtils.getStartDate(endTime);

        List<JSONObject> rawNetFlowList = busiRawNetFlowService.getClientRawFlowTrend(ip, startTime, endTime);
        return AjaxResult.success(rawNetFlowList);
    }


    /**
     * 获取服务器流量相关终端
     */
    @GetMapping("/ServerFlowRelationClient")
    @ApiOperation(value = "获取原始信令列表", httpMethod = "GET")
    public AjaxResult getServerFlowRelationClient(@ApiParam("目的IP")@RequestParam(value = "dstIp", required = false) String dstIp,
                                                  @ApiParam("时间")@RequestParam(value = "stamp", required = false) String stamp,
                                                  @ApiParam("排序")@RequestParam(required = false) String orderType,
                                                  @ApiParam("排序对象")@RequestParam(required = false) String orderValue) {
        RawNetFlow rawNetFlow = new RawNetFlow();
        rawNetFlow.setDstIp(IpUtils.ipToLong(dstIp));
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse(stamp, df);
        rawNetFlow.setStamp(localDateTime);
        startPage();
        List<JSONObject> rawNetFlowList = busiRawNetFlowService.getServerFlowRelationClient(rawNetFlow);
        return AjaxResult.success(rawNetFlowList);
    }

    /**
     * 获取流量列表终端排行
     *
     * @param stamp     时间范围：当日/当周/当月
     * @param orderType 排序： Size:流量大小排序/Count:包数量排序
     * @return
     */
    @GetMapping("/getRawClientRank")
    @ApiOperation(value = "获取流量列表终端排行", httpMethod = "GET")
    public TableDataInfo getRawClientRank(@ApiParam("时间")@RequestParam(value = "stamp", required = false) String stamp,
                                          @ApiParam("排序")@RequestParam(value = "orderType", required = false) String orderType) {
        Date startTime = new Date();
        Date endTime = new Date();
        if ("day".equals(stamp)) {
            startTime = DateUtils.getStartToday();
        } else if ("week".equals(stamp)) {
            startTime = DateUtils.getSevenDaysAgo();
        } else if ("month".equals(stamp)) {
            startTime = DateUtils.getThirtyDaysAgo();
        }
        startPage();
        List<JSONObject> list = busiRawNetFlowService.getRawClientRank(startTime, endTime, orderType);
        return getDataTable(list);
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
