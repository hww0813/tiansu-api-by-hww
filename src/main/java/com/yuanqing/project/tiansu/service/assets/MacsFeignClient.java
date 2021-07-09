package com.yuanqing.project.tiansu.service.assets;

import com.yuanqing.project.tiansu.domain.macs.MacsConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author lvjingjing
 * @date 2021/6/25 11:46
 */
@FeignClient(name = "macs",url = "${tiansu.macshost}")
public interface MacsFeignClient {

    /**
     * 根据条件获取一条配置信息
     * */
    @GetMapping("/tripartite/config/getConfigList")
    String getConfigById(MacsConfig config);

}
