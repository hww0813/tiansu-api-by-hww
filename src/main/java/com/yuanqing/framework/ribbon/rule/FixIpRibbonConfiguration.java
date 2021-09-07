package com.yuanqing.framework.ribbon.rule;

import com.netflix.loadbalancer.IRule;
import com.yuanqing.framework.ribbon.rule.FixIpRibbonRule;
import org.springframework.context.annotation.Bean;

/**
 * @author xucan
 * @version 1.0
 * @Date 2021-09-01 18:00
 */

public class FixIpRibbonConfiguration {

    @Bean
    public IRule fixedIpRule(){
        return new FixIpRibbonRule();
    }
}
