package com.yuanqing.project.tiansu.service.video;

import com.yuanqing.framework.web.domain.PageResult;
import com.yuanqing.project.tiansu.domain.operation.OperationBehaviorSession;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author Lmm
 * @Classname OperationBehaviorSessionService
 * @Description
 * @Date 2021/2/25 15:24
 * @Version V1.0
 */
public interface OperationBehaviorSessionService {


    PageResult queryOperationBehaviorSession(OperationBehaviorSession operationBehaviorSession) throws ExecutionException, InterruptedException;

}
