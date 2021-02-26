package com.yuanqing.project.tiansu.service.video.impl;

import com.yuanqing.framework.web.domain.PageResult;
import com.yuanqing.project.tiansu.domain.operation.OperationBehaviorSession;
import com.yuanqing.project.tiansu.domain.operation.RawSignal;
import com.yuanqing.project.tiansu.mapper.video.RawSignalMapper;
import com.yuanqing.project.tiansu.service.video.RawSignalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author Dong.Chao
 * @Classname RawSignalServiceImpl
 * @Description
 * @Date 2021/2/25 17:14
 * @Version V1.0
 */
@Service
public class RawSignalServiceImpl implements RawSignalService {

    @Autowired
    private RawSignalMapper rawSignalMapper;

    @Override
    public PageResult queryRawSignals(RawSignal rawSignal) throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> totalFuter = CompletableFuture.supplyAsync(() ->  rawSignalMapper.queryCount(rawSignal));
        CompletableFuture<List<RawSignal>> operationBehaviorsFuture = CompletableFuture.supplyAsync(() -> rawSignalMapper.queryListAll(rawSignal));
        return  PageResult.success(operationBehaviorsFuture.get(),rawSignal.getSize(),rawSignal.getNum(),totalFuter.get());
    }
}
