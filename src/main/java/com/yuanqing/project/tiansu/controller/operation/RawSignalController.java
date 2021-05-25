package com.yuanqing.project.tiansu.controller.operation;

import com.yuanqing.common.utils.StringUtils;
import com.yuanqing.common.utils.ip.IpUtils;
import com.yuanqing.framework.web.controller.BaseController;
import com.yuanqing.framework.web.domain.PageResult;
import com.yuanqing.project.tiansu.domain.operation.RawSignal;
import com.yuanqing.project.tiansu.service.operation.IRawSignalService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @author Dong.Chao
 * @Classname RawSignalController
 * @Description
 * @Date 2021/2/25 16:19
 * @Version V1.0
 */
@RestController
@RequestMapping(value = "/api/video/rawsignal")
@CrossOrigin
@Api(value = "原始信令相关接口",description = "原始信令列表相关API")
public class RawSignalController extends BaseController {

    @Autowired
    private IRawSignalService IRawSignalService;

    @GetMapping("/list")
    @ApiOperation(value = "获取原始信令列表", httpMethod = "GET")
    public PageResult getAll(@ApiParam("页码数")@RequestParam(value = "pageNum", defaultValue = "1") int num,
                             @ApiParam("行数")@RequestParam(value = "pageSize", defaultValue = "20") int size,
                             @ApiParam("连接类型")@RequestParam(value = "connectType", required = false) String connectType,
                             @ApiParam("源IP")@RequestParam(value = "srcIp", required = false) String srcIp,
                             @ApiParam("目的IP")@RequestParam(value = "dstIp", required = false) String dstIp,
                             @ApiParam("排序")@RequestParam(required = false) String orderType,
                             @ApiParam("排序对象")@RequestParam(required = false) String orderValue, @ApiParam("原始信令实体")RawSignal rawSignal) {
        rawSignal.setNum(num);
        rawSignal.setSize(size);
        rawSignal.setSrcIp(IpUtils.ipToLong(srcIp));
        rawSignal.setDstIp(IpUtils.ipToLong(dstIp));
        if (StringUtils.isNotBlank(orderValue) && StringUtils.isNotBlank(orderType)) {
            rawSignal.setOrderType(orderValue + " " + orderType);
        }
        try {
            return IRawSignalService.queryRawSignals(rawSignal);
        } catch (Exception e) {
            e.printStackTrace();
            return PageResult.error("请求原始信令列表错误");
        }

    }

    public static void main(String[] args) {
        System.out.println(IpUtils.ipToLong("11.1.0.177"));
    }

    @GetMapping("/findByCallId")
    @ApiOperation(value = "获取原始信令列表", httpMethod = "GET")
    public PageResult findByCallId(@ApiParam("页码数")@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                   @ApiParam("行数")@RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                   @ApiParam("源IP")@RequestParam(value = "srcIp", required = false) String srcIp,
                                   @ApiParam("目的IP")@RequestParam(value = "dstIp", required = false) String dstIp,RawSignal rawSignal) {


        rawSignal.setNum((pageNum-1)*pageSize);
        rawSignal.setSize(pageSize);
        rawSignal.setSrcIp(IpUtils.ipToLong(srcIp));
        rawSignal.setDstIp(IpUtils.ipToLong(dstIp));

        try {
            return IRawSignalService.queryRawSignals(rawSignal);
        } catch (Exception e) {
            e.printStackTrace();
            return PageResult.error("请求原始信令列表错误");
        }
    }
}
