package com.yuanqing.project.tiansu.mapper.operation;

import com.alibaba.fastjson.JSONObject;
import com.yuanqing.framework.web.mapper.BaseMapper;
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
public interface OperationBehaviorMapper extends BaseMapper<OperationBehavior,Long> {

     List<Long> getCameraidListBySession(Long sessionId);

     Integer quertyOperationBehaviorCount(OperationBehavior operationBehavior);

     Integer getVisitedRateRelatedOperationCount(@Param("list") List<String> cameraCodeList,
                                                 @Param("filter") OperationBehavior operationBehavior);

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

    /**
     * 根据摄像头编号和过滤条件查询操作行为
     * @param cameraCodeList 摄像头编号集合
     * @param operationBehavior 过滤条件集合
     * @return
     */
      List<OperationBehavior> getVisitedRateRelatedOperation(@Param("list") List<String> cameraCodeList,
                                                             @Param("filter") OperationBehavior operationBehavior);
}
