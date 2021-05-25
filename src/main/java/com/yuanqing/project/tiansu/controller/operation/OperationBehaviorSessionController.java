package com.yuanqing.project.tiansu.controller.operation;

import com.alibaba.fastjson.JSONObject;
import com.yuanqing.common.utils.StringUtils;
import com.yuanqing.common.utils.ip.IpUtils;
import com.yuanqing.framework.web.controller.BaseController;
import com.yuanqing.framework.web.domain.AjaxResult;
import com.yuanqing.framework.web.domain.PageResult;
import com.yuanqing.project.tiansu.domain.operation.OperationBehaviorSession;
import com.yuanqing.project.tiansu.service.operation.IOperationBehaviorSessionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

/**
 * @author Dong.Chao
 * @Classname OperationBehaviorSessionController
 * @Description 操作行为会话相关
 * @Date 2021/2/25 14:15
 * @Version V1.0
 */
@RestController
@RequestMapping(value = "/api/operation/behavior/session")
@Api(value = "操作行为会话接口", description = "操作行为会话相关Api")
@CrossOrigin
public class OperationBehaviorSessionController extends BaseController {

    @Autowired
    private IOperationBehaviorSessionService operationBehaviorSessionService;


    @GetMapping("/list")
    @ApiOperation(value = "获取操作行为会话列表", httpMethod = "GET")
    public PageResult getAll(@ApiParam("页码数")@RequestParam(value = "pageNum", defaultValue = "1") int num,
                             @ApiParam("行数")@RequestParam(value = "pageSize", defaultValue = "20") int size,
                             @ApiParam("会话ID")@RequestParam(value = "sessionId", required = false) Long sessionId,
                             @ApiParam("源设备编码")@RequestParam(value = "srcCode", required = false) String srcCode,
                             @ApiParam("IP地址")@RequestParam(value = "ipAddress", required = false) String ipAddress,
                             @ApiParam("用户名")@RequestParam(value = "username", required = false) String username,
                             @ApiParam("排序")@RequestParam(required = false) String orderType,
                             @ApiParam("排序对象")@RequestParam(required = false) String orderValue, @ApiParam("操作行为会话对象")OperationBehaviorSession operationBehaviorSession) {

        operationBehaviorSession.setNum(num - 1);
        operationBehaviorSession.setSize(size);
        operationBehaviorSession.setId(sessionId);
        operationBehaviorSession.setUsername(username);
        operationBehaviorSession.setSrcIp(IpUtils.ipToLong(ipAddress));
        if (StringUtils.isNotBlank(orderValue) && StringUtils.isNotBlank(orderType)) {
            operationBehaviorSession.setOrderType(orderValue + " " + orderType);
        }
        try {
            PageResult pageResult = operationBehaviorSessionService.getList(operationBehaviorSession);
            return pageResult;
        } catch (Exception e) {
            e.printStackTrace();
            return PageResult.error("获取获取操作行为会话列表异常！");
        }
    }

    @GetMapping("/behaviorCategory")
    @ApiOperation(value = "获取行为类别占比", httpMethod = "GET")
    public AjaxResult getBehaviorCategory(@ApiParam("开始时间")@RequestParam(value = "startDate") String startDate,
                                          @ApiParam("结束时间")@RequestParam(value = "endDate") String endDate) {
        JSONObject filters = new JSONObject();
        filters.put("startDate", startDate);
        filters.put("endDate", endDate);
        Map<String, Integer> map = operationBehaviorSessionService.getBehaviorCategory(filters);
        return AjaxResult.success(map);
    }

}
