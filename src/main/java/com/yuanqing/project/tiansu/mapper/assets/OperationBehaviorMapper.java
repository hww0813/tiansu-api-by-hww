package com.yuanqing.project.tiansu.mapper.assets;

import com.alibaba.fastjson.JSONObject;
import com.yuanqing.framework.web.mapper.BaseMapper;
import com.yuanqing.project.tiansu.domain.assets.SessionClient;
import com.yuanqing.project.tiansu.domain.operation.OperationBehavior;
import com.yuanqing.project.tiansu.domain.operation.OperationBehaviorSearch;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

/**
 * 操作行为
 *
 * @author jqchu
 * @version 1.0
 * @since 2017.12.17
 */
@Repository
public interface OperationBehaviorMapper  {

     List<OperationBehavior> queryOperationBehaviorList(OperationBehavior operationBehavior);

     List<Long> getCameraidListBySession(Long sessionId);

     Integer quertyOperationBehaviorCount(OperationBehavior operationBehavior);

     List<OperationBehavior> getRealTimeBehaviorList(OperationBehaviorSearch operationBehaviorSearch);

     int updateOperationBehavior(OperationBehavior operationBehavior);

     List<HashMap> cameraAnalysisDetail(JSONObject filters);

     Integer cameraAnalysisCount(JSONObject filters);

     Integer queryRawCount(Object object);

        /**
         * 根据sessionID 查询dstIP
         * @param sessionId
         * @return
                 */
      List<Long> getDstIpBySessionId(Long sessionId);
}
