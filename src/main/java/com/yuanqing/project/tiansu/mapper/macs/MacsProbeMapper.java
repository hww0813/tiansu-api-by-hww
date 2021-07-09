package com.yuanqing.project.tiansu.mapper.macs;

import com.yuanqing.project.tiansu.domain.macs.MacsConfig;
import com.yuanqing.project.tiansu.domain.macs.MacsProbe;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author xucan
 * @version 1.0
 * @Date 2021-06-21 11:02
 */
@Repository
public interface MacsProbeMapper {

    /**
     * 获取探针列表
     * @return
     */
    List<MacsProbe> getProbeList();
}
