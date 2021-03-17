package com.yuanqing.project.tiansu.mapper.analysis;

import com.alibaba.fastjson.JSONObject;
import com.yuanqing.framework.web.mapper.BaseMapper;
import com.yuanqing.project.tiansu.domain.assets.Camera;
import com.yuanqing.project.tiansu.domain.assets.Client;
import com.yuanqing.project.tiansu.domain.operation.OperationBehavior;
import com.yuanqing.project.tiansu.domain.report.VisitRate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VisitRateMapper extends BaseMapper<VisitRate,Long> {

    List<VisitRate> getRateList(JSONObject filters);

    List<VisitRate> getRateLastList(JSONObject filters);

    List<Camera> getCameraCntList(JSONObject filters);

    List<Camera> getVisitedCntList(JSONObject filters);

    List<OperationBehavior> getVisitCntList(JSONObject filters);

    List<Client> getClientCntList(JSONObject filters);

    List<VisitRate> getRegionRate(JSONObject filters);

    void updateStatisticsTable();

}
