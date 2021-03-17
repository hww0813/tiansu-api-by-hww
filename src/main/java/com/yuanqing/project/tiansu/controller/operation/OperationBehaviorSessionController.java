package com.yuanqing.project.tiansu.controller.operation;

import com.yuanqing.common.utils.StringUtils;
import com.yuanqing.common.utils.ip.IpUtils;
import com.yuanqing.framework.web.controller.BaseController;
import com.yuanqing.framework.web.domain.PageResult;
import com.yuanqing.project.tiansu.domain.operation.OperationBehaviorSession;
import com.yuanqing.project.tiansu.service.operation.IOperationBehaviorSessionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

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
    private IOperationBehaviorSessionService operationBehaviorSessionService;


    @GetMapping("/list")
    @ApiOperation(value = "获取操作行为会话列表", httpMethod = "GET")
    public PageResult getAll(@RequestParam(value = "pageNum", defaultValue = "1") int num,
                             @RequestParam(value = "pageSize", defaultValue = "20") int size,
                             @RequestParam(value = "stime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date stime,
                             @RequestParam(value = "etime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date etime,
                             @RequestParam(value = "sessionId", required = false) Long sessionId,
                             @RequestParam(value = "srcCode", required = false) String srcCode,
                             @RequestParam(value = "srcIp", required = false) String srcIp,
                             @RequestParam(value = "username", required = false) String username,
                             @RequestParam(value = "connectType", required = false) String connectType,
                             @RequestParam(required = false) String orderType,
                             @RequestParam(required = false) String orderValue, OperationBehaviorSession operationBehaviorSession) {

        operationBehaviorSession.setNum(num -1);
        operationBehaviorSession.setSize(size);
        operationBehaviorSession.setId(sessionId);
        if(stime != null){
            operationBehaviorSession.setStime(stime);
        }
        if(etime != null){
            operationBehaviorSession.setEtime(etime);
        }
        operationBehaviorSession.setUsername(username);
        operationBehaviorSession.setSrcIp(IpUtils.ipToLong(srcIp));
        if (StringUtils.isNotBlank(orderValue) && StringUtils.isNotBlank(orderType)) {
            operationBehaviorSession.setOrderType(orderValue + " " + orderType);
        }
        try {
            PageResult pageResult = operationBehaviorSessionService.queryOperationBehaviorSession(operationBehaviorSession);
            return pageResult;
        }catch (Exception e){
            return PageResult.error("获取获取操作行为会话列表异常！");
        }
    }


}
