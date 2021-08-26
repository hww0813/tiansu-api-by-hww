package com.yuanqing.project.tiansu.service.operation;

import com.alibaba.fastjson.JSONObject;
import com.yuanqing.framework.web.domain.PageResult;
import com.yuanqing.project.tiansu.domain.operation.RawSignal;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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

    List<JSONObject> getAllToReport(RawSignal rawSignal);

    public void getListToReport(HttpServletResponse response,RawSignal condRawSignal) throws IOException;
}
