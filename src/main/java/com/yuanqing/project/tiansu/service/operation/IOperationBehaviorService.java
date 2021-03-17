package com.yuanqing.project.tiansu.service.operation;

import com.alibaba.fastjson.JSONObject;
import com.yuanqing.framework.web.domain.AjaxResult;
import com.yuanqing.framework.web.domain.PageResult;
import com.yuanqing.project.tiansu.domain.operation.OperationBehavior;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Dong.Chao
 * @Classname OperationBehaviorService
 * @Description
 * @Date 2021/2/25 11:37
 * @Version V1.0
 */
public interface IOperationBehaviorService {

    PageResult queryOperationList(OperationBehavior operationBehavior) throws Exception;

    AjaxResult getCharts(LocalDate startDate,LocalDate endDate,String action,String sort,String type);

    List<JSONObject> getAllToReport(JSONObject filters);
}
