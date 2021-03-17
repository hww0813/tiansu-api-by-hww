package com.yuanqing.project.tiansu.mapper.operation;

import com.yuanqing.project.tiansu.domain.operation.RawSignal;

import java.util.List;

/**
 * @author Lmm
 * @Classname RawSignalMapper
 * @Description
 * @Date 2021/2/25 16:38
 * @Version V1.0
 */
public interface RawSignalMapper {

    List<RawSignal> queryListAll(RawSignal rawSignal);

    Integer queryCount(RawSignal rawSignal);

}
