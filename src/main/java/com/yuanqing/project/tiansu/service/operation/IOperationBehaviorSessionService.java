package com.yuanqing.project.tiansu.service.operation;

import com.alibaba.fastjson.JSONObject;
import com.yuanqing.framework.web.domain.PageResult;
import com.yuanqing.project.tiansu.domain.operation.OperationBehaviorSession;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * @author Lmm
 * @Classname OperationBehaviorSessionService
 * @Description
 * @Date 2021/2/25 15:24
 * @Version V1.0
 */
public interface IOperationBehaviorSessionService {


    PageResult getList(OperationBehaviorSession operationBehaviorSession) throws ExecutionException, InterruptedException;

    List<JSONObject> getAllToReport(JSONObject filters);

    /**
     * 统计操作行为分类
     * @param filters
     * @return
     */
    Map<String, Integer> getBehaviorCategory(JSONObject filters);
}
