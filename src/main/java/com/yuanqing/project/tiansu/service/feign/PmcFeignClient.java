package com.yuanqing.project.tiansu.service.feign;

import com.yuanqing.framework.web.domain.AjaxResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * @author lvjingjing
 * @date 2021/6/23 15:55
 */
@FeignClient(value = "pmc-root")
public interface PmcFeignClient {

    /**
     * 获得服务器详情里需要的：CPU使用率、内存使用率、运行时间，这三个数据
     */
    @GetMapping("/pmc/tiansuPmc/getSomeUtilization")
    AjaxResult getSomeUtilization(@RequestParam("instance") String instance,@RequestHeader(name = "Authorization") String Authorization);

    /**
     * 获得cpu使用率趋势图
     */
    @GetMapping("/pmc/tiansuPmc/getCpuTrend")
    AjaxResult getCpuTrend(@RequestParam("instance")String instance,@RequestHeader(name = "Authorization") String Authorization);

    /**
     * 获得内存使用率趋势图
     */
    @GetMapping("/pmc/tiansuPmc/getMemoryTrend")
    AjaxResult getMemoryTrend(@RequestParam("instance")String instance,@RequestHeader(name = "Authorization") String Authorization);

    /**
     * 获得consul注册监控过的ip
     */
    @GetMapping("/pmc/consul/getConsulIp")
    String getConsulIp(@RequestHeader(name = "Authorization") String Authorization);
}
