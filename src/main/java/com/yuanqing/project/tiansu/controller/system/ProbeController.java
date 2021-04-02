package com.yuanqing.project.tiansu.controller.system;

import com.yuanqing.framework.web.domain.AjaxResult;
import com.yuanqing.project.tiansu.domain.macs.MacsConfig;
import com.yuanqing.project.tiansu.domain.macs.MacsRegion;
import com.yuanqing.project.tiansu.service.macs.IMacsConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author xucan
 * @version 1.0
 * @Date 2021-02-25 10:32
 */

@RestController
@RequestMapping(value = "/api/probe")
public class ProbeController {

    @Autowired
    private IMacsConfigService macsConfigService;

    @GetMapping("/sysname")
    public AjaxResult getSysName(){
                List<MacsConfig> macsConfigList = macsConfigService.selectMacsConfigByTypeAndName(new MacsConfig("system","title"));

                if(!CollectionUtils.isEmpty(macsConfigList)) {
                    return AjaxResult.success(macsConfigList.get(0));
                }else{
                    return AjaxResult.error("获取失败，结果为空");
        }
    }

    @GetMapping("/version")
    public AjaxResult getVersion(){
                List<MacsConfig> macsConfigList = macsConfigService.selectMacsConfigByTypeAndName(new MacsConfig("system","version"));

                if(!CollectionUtils.isEmpty(macsConfigList)) {
                    String value = macsConfigList.get(0).getValue();
                    return AjaxResult.success("请求成功",value);
                }else{
                    return AjaxResult.error("获取失败，结果为空");
        }
    }

    @GetMapping("/getRegion")
    public AjaxResult getRegion(){
        MacsRegion region = macsConfigService.getRegion(null);

        if(region != null) {
            return AjaxResult.success(region);
        }else{
            return AjaxResult.error("获取失败，结果为空");
        }
    }

    @GetMapping("/getLowerRegion")
    public AjaxResult getLowerRegion(@RequestParam("regionId") String regionId){
        List<MacsRegion> lowerRegion = macsConfigService.getLowerRegion(regionId);

        if(!CollectionUtils.isEmpty(lowerRegion)) {
            return AjaxResult.success(lowerRegion);
        }else{
            return AjaxResult.error("获取失败，结果为空");
        }
    }

}
