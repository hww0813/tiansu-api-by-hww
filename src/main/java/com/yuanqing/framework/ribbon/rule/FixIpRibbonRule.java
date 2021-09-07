package com.yuanqing.framework.ribbon.rule;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.RoundRobinRule;
import com.netflix.loadbalancer.Server;
import com.yuanqing.common.utils.StringUtils;
import lombok.Synchronized;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.commons.util.InetUtilsProperties;
import org.springframework.web.servlet.support.RequestContext;

import java.util.List;


/**
 * @author xucan
 * @version 1.0
 * @Date 2021-09-01 17:57
 */
public class FixIpRibbonRule extends AbstractLoadBalancerRule {

//    @Value("${spring.cloud.consul.discovery.prefer-ip-address}")
//    private boolean preferIpAddress = false;
//
    private static final String  LOCALHOST = "127.0.0.1";


    public FixIpRibbonRule() {

    }

    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {
        System.out.println(iClientConfig.toString());
    }


    @Override
    public Server choose(Object key) {

        ILoadBalancer lb = getLoadBalancer();
        if (lb == null) {
            return null;
        }
        Server server = null;


        while (server == null) {
            if (Thread.interrupted()) {
                return null;
            }
            List<Server> upList = lb.getReachableServers();
            List<Server> allList = lb.getAllServers();

            int serverCount = allList.size();
            if (serverCount == 0) {
                /*
                 * No servers. End regardless of pass, because subsequent passes
                 * only get more restrictive.
                 */
                return null;
            }

            int index = chooseDevelopInt(upList);
            System.out.println(upList.toString()+","+Thread.currentThread().getId());

            if(index == -1){
                RoundRobinRule roundRobinRule = new RoundRobinRule();
                Server choose = roundRobinRule.choose(lb, key);
                if(null != choose){
                    System.out.println("未发现注册中心本地服务,采用轮询策略:"+ choose.getHostPort());
                }
                return choose;
            }

            server = upList.get(index);

            if (server == null) {
                /*
                 * The only time this should happen is if the server list were
                 * somehow trimmed. This is a transient condition. Retry after
                 * yielding.
                 */
                Thread.yield();
                continue;
            }

            if (server.isAlive()) {
                System.out.println("发现注册中心本地服务,优先调用:" + server.getHostPort());
                return (server);
            }

            // Shouldn't actually happen.. but must be transient or a bug.
            server = null;
            Thread.yield();
        }

        return server;




    }

    private int chooseDevelopInt(List<Server> upList) {


        InetUtils inetUtils = new InetUtils(new InetUtilsProperties());

        InetUtils.HostInfo firstNonLoopbackHostInfo = inetUtils.findFirstNonLoopbackHostInfo();

        String local = firstNonLoopbackHostInfo.getIpAddress();


        Integer position = null;
        Integer localhostPosition = null;
        for (int i = 0; i < upList.size(); i++) {
            Server server = upList.get(i);
            if (null != server && local.equals(server.getHost())) {
                position = i;
                break;
            } else if (null != server && LOCALHOST.equals(server.getHost())) {
                localhostPosition = i;
            }
        }
        if (null == position && null != localhostPosition) {
            position = localhostPosition;
        }
        if (null == position) {
            return -1;
        }
        return position;

    }


}
