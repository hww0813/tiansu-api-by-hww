package com.yuanqing.project.tiansu.service.macs;

import com.yuanqing.framework.web.service.BaseService;
import com.yuanqing.project.tiansu.domain.macs.MacsConfig;
import com.yuanqing.project.tiansu.domain.macs.MacsRegion;

public interface IMacsConfigService extends BaseService<MacsConfig, Long>{

    /**
     * 根据类型和名称查询系统配置
     * @param type
     * @param name
     * @return
     */
    MacsConfig selectMacsConfigByTypeAndName(String type, String name);

    /**
     * 根据ID获取区域信息
     * @param id
     * @return
     */
    MacsRegion selectMacsRegionById(String id);

    String getRegion(String cityCode);
}
