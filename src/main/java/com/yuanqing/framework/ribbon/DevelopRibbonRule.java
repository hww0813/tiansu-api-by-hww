package com.yuanqing.framework.ribbon;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.commons.util.InetUtilsProperties;
import org.springframework.web.servlet.support.RequestContext;

import java.util.List;


/**
 * @author xucan
 * @version 1.0
 * @Date 2021-09-01 17:57
 */
public class DevelopRibbonRule extends AbstractLoadBalancerRule {


    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {

    }

    @Override
    public Server choose(Object key) {
        ILoadBalancer lb = getLoadBalancer();
        List<Server> reachableServers = lb.getReachableServers();

        InetUtils inetUtils = new InetUtils(new InetUtilsProperties());

        InetUtils.HostInfo firstNonLoopbackHostInfo = inetUtils.findFirstNonLoopbackHostInfo();

        String local = firstNonLoopbackHostInfo.getIpAddress();



        for(Server server : reachableServers){
            if(server.getHost().equals(local)){
                System.out.println("优先使用开发环境服务,地址为:"+local);
                return server;
            }
        }

        return null;
    }

}
