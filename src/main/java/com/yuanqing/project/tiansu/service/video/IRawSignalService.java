package com.yuanqing.project.tiansu.service.video;

import com.yuanqing.framework.web.domain.PageResult;
import com.yuanqing.project.tiansu.domain.operation.RawSignal;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author Lmm
 * @Classname RawSignalService
 * @Description
 * @Date 2021/2/25 17:14
 * @Version V1.0
 */
public interface IRawSignalService {

    PageResult queryRawSignals(RawSignal rawSignal) throws ExecutionException, InterruptedException;
}
