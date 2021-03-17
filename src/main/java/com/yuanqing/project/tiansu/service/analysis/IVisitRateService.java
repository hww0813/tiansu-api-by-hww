package com.yuanqing.project.tiansu.service.analysis;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.yuanqing.framework.web.service.BaseService;
import com.yuanqing.project.tiansu.domain.assets.Camera;
import com.yuanqing.project.tiansu.domain.assets.Client;
import com.yuanqing.project.tiansu.domain.operation.OperationBehavior;
import com.yuanqing.project.tiansu.domain.report.VisitRate;

import java.util.List;

public interface IVisitRateService extends BaseService<VisitRate, Long>{

    PageInfo<VisitRate> page(int pageNum, int pageSize, JSONObject filters);

    List<VisitRate> getAllToReport(JSONObject filters);

    VisitRate getRegionRate(JSONObject filters);

    PageInfo<Camera> getCameraCntList(int pageNum, int pageSize, JSONObject filters);

    PageInfo<Camera> getVisitedCntList(int pageNum, int pageSize, JSONObject filters);

    PageInfo<OperationBehavior> getVisitCntList(int pageNum, int pageSize, JSONObject filters);

    PageInfo<Client> getClientCntList(int pageNum, int pageSize, JSONObject filters);

    List<JSONObject> getRateCameraCntToReport(JSONObject filters);

    List<JSONObject> getRateVisitedCntToReport(JSONObject filters);

    List<JSONObject> getRateVisitCntToReport(JSONObject filters);

    List<JSONObject> getRateClientCntToReport(JSONObject filters);

    void updateStatisticsTable();
}
