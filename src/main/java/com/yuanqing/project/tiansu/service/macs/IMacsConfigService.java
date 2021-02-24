package com.yuanqing.project.tiansu.service.macs;

import com.yuanqing.framework.web.service.BaseService;
import com.yuanqing.project.tiansu.domain.macs.MacsConfig;

public interface IMacsConfigService extends BaseService<MacsConfig, Long>{

    MacsConfig selectMacsConfigByTypeAndName(String type, String name);
}
