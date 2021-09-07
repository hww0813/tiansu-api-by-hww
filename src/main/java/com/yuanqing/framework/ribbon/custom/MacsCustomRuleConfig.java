package com.yuanqing.framework.ribbon.custom;

import com.yuanqing.framework.ribbon.rule.FixIpRibbonConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.annotation.Configuration;

/**
 * @author xucan
 * @version 1.0
 * @Date 2021-09-07 17:01
 */

@Configuration
@RibbonClient(name = "macs-root",configuration = FixIpRibbonConfiguration.class)
@ConditionalOnProperty(name = "spring.profiles.active", havingValue = "dev", matchIfMissing = false)
public class MacsCustomRuleConfig {
}
