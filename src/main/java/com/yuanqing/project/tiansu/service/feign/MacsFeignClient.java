package com.yuanqing.project.tiansu.service.feign;

import com.alibaba.fastjson.JSONObject;
import com.yuanqing.framework.web.domain.AjaxResult;
import com.yuanqing.project.system.domain.MacsBwList;
import com.yuanqing.project.system.domain.MacsEventRule;
import com.yuanqing.project.tiansu.domain.macs.MacsConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;

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
    String getConfigById(@RequestParam("type") String type,
                         @RequestParam("name") String name);

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

    /**
     * 黑白名单
     */
    @GetMapping(value = "/configuration/blackWhite/list")
    String getBlackWhiteList(@SpringQueryMap MacsBwList macsBwList, @RequestParam("pageSize") Integer pageSize, @RequestParam("pageNum") Integer pageNum, @RequestHeader(name="Authorization") String Authorization);

    @PostMapping(value = "/configuration/blackWhite")
    AjaxResult insertBlackWhite(@RequestBody MacsBwList macsBwList, @RequestHeader(name="Authorization") String Authorization);

    @PutMapping(value = "/configuration/blackWhite")
    AjaxResult updateBlackWhite(@RequestBody MacsBwList macsBwList, @RequestHeader(name="Authorization") String Authorization);

    @DeleteMapping("/configuration/blackWhite/{ids}")
    AjaxResult deleteMacsBwListByIds(@PathVariable Long[] ids, @RequestHeader(name="Authorization") String Authorization);

    @GetMapping(value = "/configuration/blackWhite/{id}")
    AjaxResult selectMacsBwListById(@PathVariable("id") Long id, @RequestHeader(name="Authorization") String Authorization);

    /**
     * 告警规则
     */
    @GetMapping(value = "/configuration/rule/list")
    JSONObject getEventRuleList(@SpringQueryMap MacsEventRule macsEventRule, @RequestParam("pageNum") Integer pageNum, @RequestParam("pageSize") Integer pageSize, @RequestHeader(name="Authorization") String Authorization);

    @GetMapping(value = "/configuration/rule/{id}")
    AjaxResult getEventRuleInfo(@PathVariable("id") Long id, @RequestHeader(name="Authorization") String Authorization);

    @PostMapping(value = "/configuration/rule")
    AjaxResult addEventRule(@RequestBody MacsEventRule macsEventRule, @RequestHeader(name="Authorization") String Authorization);

    @PutMapping(value = "/configuration/rule")
    AjaxResult editEventRule(@RequestBody MacsEventRule macsEventRule, @RequestHeader(name="Authorization") String Authorization);

    @DeleteMapping(value = "/configuration/rule")
    AjaxResult removeEventRule(@RequestBody MacsEventRule macsEventRule, @RequestHeader(name="Authorization") String Authorization);

    /**
     * 策略管理
     */
    @GetMapping("/configuration/config/list")
    JSONObject getMacsConfigList(@SpringQueryMap MacsConfig macsConfig, @RequestHeader(name="Authorization") String Authorization);

    @PutMapping("/configuration/config")
    AjaxResult editMacsConfig(@RequestBody MacsConfig macsConfig, @RequestHeader(name="Authorization") String Authorization);

    @PostMapping(value = "/configuration/config")
    AjaxResult addMacsConfig(@RequestBody MacsConfig macsConfig, @RequestHeader(name="Authorization") String Authorization);

    @DeleteMapping(value = "/configuration/config/{ids}")
    AjaxResult removeMacsConfig(@PathVariable Long[] ids, @RequestHeader(name="Authorization") String Authorization);

    @GetMapping(value = "/configuration/config/{id}")
    AjaxResult selectMacsConfigById(@PathVariable("id") Long id, @RequestHeader(name="Authorization") String Authorization);

    @GetMapping(value = "/maintain/MacsHealth/operate")
    AjaxResult operate(@RequestParam("instance") String instance, @RequestParam("operation") String operation,
                       @RequestParam("serverName") String serverName, @RequestHeader(name="Authorization") String Authorization);

}
