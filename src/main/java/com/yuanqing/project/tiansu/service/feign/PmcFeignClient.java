package com.yuanqing.project.tiansu.service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * @author lvjingjing
 * @date 2021/6/25 17:38
 */
@FeignClient(name = "pmc",url="${tiansu.pmchost}")
public interface PmcFeignClient {

    /**
     * 获得consul注册监控过的ip
     */
    @GetMapping("/pmc/consul/getConsulIp")
    String getConsulIp(@RequestHeader(name = "Authorization") String Authorization);
}
