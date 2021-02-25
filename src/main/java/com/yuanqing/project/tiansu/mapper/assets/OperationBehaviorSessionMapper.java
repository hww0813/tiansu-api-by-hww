package com.yuanqing.project.tiansu.mapper.assets;

import com.yuanqing.project.tiansu.domain.operation.OperationBehaviorSession;

import java.util.List;

/**
 * @author Lmm
 * @Classname OperationBehaviorSessionMapper
 * @Description
 * @Date 2021/2/25 15:29
 * @Version V1.0
 */
public interface OperationBehaviorSessionMapper {

    List<OperationBehaviorSession> getAll(OperationBehaviorSession operationBehaviorSession);

    Integer getCount(OperationBehaviorSession operationBehaviorSession);
}
