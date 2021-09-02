package com.yuanqing.framework.ribbon;

import com.netflix.loadbalancer.IRule;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author xucan
 * @version 1.0
 * @Date 2021-09-01 18:00
 */
@Configuration
@RibbonClient(name = "macs-root",configuration = MacsRibbonConfig.class)
@ConditionalOnProperty(name = "spring.profiles.active", havingValue = "dev", matchIfMissing = false)
public class MacsRibbonConfig {

    @Bean
    public IRule fixedIpRule(){
        return new DevelopRibbonRule();
    }
}
