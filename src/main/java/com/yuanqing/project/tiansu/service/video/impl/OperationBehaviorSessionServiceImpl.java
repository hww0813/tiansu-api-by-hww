package com.yuanqing.project.tiansu.service.video.impl;

import com.yuanqing.framework.web.domain.PageResult;
import com.yuanqing.project.tiansu.domain.operation.OperationBehavior;
import com.yuanqing.project.tiansu.domain.operation.OperationBehaviorSession;
import com.yuanqing.project.tiansu.mapper.assets.OperationBehaviorMapper;
import com.yuanqing.project.tiansu.mapper.assets.OperationBehaviorSessionMapper;
import com.yuanqing.project.tiansu.service.video.OperationBehaviorSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author Dong.Chao
 * @Classname OperationBehaviorSessionImpl
 * @Description
 * @Date 2021/2/25 15:25
 * @Version V1.0
 */
@Service
public class OperationBehaviorSessionServiceImpl implements OperationBehaviorSessionService {

    @Resource
    private OperationBehaviorSessionMapper operationBehaviorSessionMapper;


    @Override
    public PageResult queryOperationBehaviorSession(OperationBehaviorSession operationBehaviorSession) throws ExecutionException, InterruptedException {

        //总数据
        CompletableFuture<Integer> totalFuter = CompletableFuture.supplyAsync(() ->  operationBehaviorSessionMapper.getCount(operationBehaviorSession));
        //操作行为列表
        CompletableFuture<List<OperationBehaviorSession>> operationBehaviorsFuture = CompletableFuture.supplyAsync(() -> operationBehaviorSessionMapper.getAll(operationBehaviorSession));

        return  PageResult.success(operationBehaviorsFuture.get(),operationBehaviorSession.getSize(),operationBehaviorSession.getNum(),totalFuter.get());

    }
}
