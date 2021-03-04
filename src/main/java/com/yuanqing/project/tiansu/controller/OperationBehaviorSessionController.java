package com.yuanqing.project.tiansu.controller;

import com.yuanqing.common.utils.StringUtils;
import com.yuanqing.common.utils.ip.IpUtils;
import com.yuanqing.framework.web.controller.BaseController;
import com.yuanqing.framework.web.domain.PageResult;
import com.yuanqing.project.tiansu.domain.operation.OperationBehaviorSession;
import com.yuanqing.project.tiansu.service.video.IOperationBehaviorSessionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Dong.Chao
 * @Classname OperationBehaviorSessionController
 * @Description 操作行为会话相关
 * @Date 2021/2/25 14:15
 * @Version V1.0
 */
@RestController
@RequestMapping(value = "/api/operation/behavior/session")
@Api(value = "操作行为会话接口")
@CrossOrigin
public class OperationBehaviorSessionController extends BaseController {

    @Autowired
    private IOperationBehaviorSessionService IOperationBehaviorSessionService;


    @GetMapping("/list")
    @ApiOperation(value = "获取操作行为会话列表", httpMethod = "GET")
    public PageResult getAll(@RequestParam(value = "pageNum", defaultValue = "1") int num,
                             @RequestParam(value = "pageSize", defaultValue = "20") int size,
                             @RequestParam(value = "sessionId", required = false) Long sessionId,
                             @RequestParam(value = "srcIp", required = false) String srcIp,
                             @RequestParam(value = "dstIp", required = false) String dstIp,
                             @RequestParam(required = false) String orderType,
                             @RequestParam(required = false) String orderValue, OperationBehaviorSession operationBehaviorSession) {

        operationBehaviorSession.setNum(num -1);
        operationBehaviorSession.setSize(size);
        operationBehaviorSession.setId(sessionId);
        operationBehaviorSession.setSrcIp(IpUtils.ipToLong(srcIp));
        operationBehaviorSession.setDstIp(IpUtils.ipToLong(dstIp));
        if (StringUtils.isNotBlank(orderValue) && StringUtils.isNotBlank(orderType)) {
            operationBehaviorSession.setOrderType(orderValue + " " + orderType);
        }
        try {
            return IOperationBehaviorSessionService.queryOperationBehaviorSession(operationBehaviorSession);
        }catch (Exception e){
            return PageResult.error("获取获取操作行为会话列表异常！");
        }
    }


}
