package com.yuanqing.project.tiansu.init;

import com.yuanqing.framework.redis.RedisCache;
import com.yuanqing.project.tiansu.domain.event.BusiHttpConfig;
import com.yuanqing.project.tiansu.service.event.IBusiHttpConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class HttpConfigInit {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpConfigInit.class);
    @Resource
    private RedisCache redisCache;
    @Resource
    private IBusiHttpConfigService busiHttpConfigService;

    public void run(String... args) throws Exception {
        BusiHttpConfig busiHttpConfig = new BusiHttpConfig();
        List<BusiHttpConfig> list = busiHttpConfigService.selectBusiHttpConfigList(busiHttpConfig);
        redisCache.deleteObject("HTTP_CONFIG");
        redisCache.setCacheList("HTTP_CONFIG",list);
        LOGGER.error("================ HttpConfigInit => 接口阈值初始化 ================");
    }
}
