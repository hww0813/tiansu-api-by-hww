package com.yuanqing.project.tiansu.service.feign;

import com.yuanqing.project.tiansu.domain.macs.MacsConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

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

    /**
     * 获取下级地区
     * */
    @GetMapping("/tripartite/region/getConfigById")
    String getConfigById(@RequestParam("regionId") String regionId);

    /**
     * 获取地区配置详细信息
     */
    @GetMapping(value = "/tripartite/region/regionInfo")
    String getInfo(@RequestParam("regionId") String regionId);

    /**
     * 获取地区配置详细信息
     */
    @GetMapping(value = "/tripartite/region/regionList")
    String getList();
}
