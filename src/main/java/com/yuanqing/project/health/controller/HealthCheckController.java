package com.yuanqing.project.health.controller;

import com.yuanqing.common.utils.StringUtils;
import com.yuanqing.project.health.queue.RunTimeMap;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author lvjingjing
 */
@RestController
@RequestMapping(value = "/api/tiansu_api")
@Api(value = "检查服务健康度接口")
@CrossOrigin
public class HealthCheckController {

    @GetMapping("/healthCheck")
    @ApiOperation(value = "", httpMethod = "GET")
    public String healthCheck() {
        Date date = RunTimeMap.get("runTime");
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateTime = sdf.format(date);
            return dateTime;
        }catch (Exception e){
            return "程序启动时间获取有误";
        }
    }




    @GetMapping("/gitInfo")
    public String getGitInfo() throws Exception {

        String originStr = IOUtils.toString(new URI("classpath:git.properties"), StandardCharsets.UTF_8);
        if(StringUtils.isEmpty(originStr)){
            return "git版本文件为空";
        }else {
            return StringUtils.decodeUnicode(originStr);
        }
    }


}
