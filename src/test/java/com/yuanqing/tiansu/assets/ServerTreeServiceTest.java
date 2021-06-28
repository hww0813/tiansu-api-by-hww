package com.yuanqing.tiansu.assets;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yuanqing.common.utils.http.HttpUtils;
import com.yuanqing.project.tiansu.domain.assets.ServerTree;
import com.yuanqing.project.tiansu.mapper.operation.OperationBehaviorMapper;
import com.yuanqing.project.tiansu.service.assets.IServerTreeService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author xucan
 * @version 1.0
 * @Date 2021-02-24 17:27
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class ServerTreeServiceTest {

    @Autowired
    private IServerTreeService serverTreeService;

    @Autowired
    private OperationBehaviorMapper operationBehaviorMapper;

    /**
     * 根据sessionId 查询服务器列表 1/2
     */
    @Test
    public void getSessionServerList() {

        //过滤条件
        ServerTree serverTree = new ServerTree();

        //1.根据sessionID 查询操作行为表，得到相关操作行为到dst_ip 集合
        List<Long> dstIpList = operationBehaviorMapper.getDstIpBySessionId(630494791108530177L);

        //2.根据dst_ip集合 和serverTree 过滤条件 查询服务器表，得到sessionId 相关服务器列表
        List<ServerTree> sessionServerList = serverTreeService.getSessionServerList(serverTree, dstIpList);

        Assert.assertEquals(2, sessionServerList.size());
        System.out.println(sessionServerList);

    }
//
//    @Test
//    public void test2() {
//        String result = HttpUtils.getHttpRequest("http://localhost:8188"+"/pmc/consul/getConsulIp");
////        String result = "[\"192.168.1.30\",\"192.168.1.20\"]";
//        List<String> list = new ArrayList<>();
//        JSONObject syslogJson = JSON.parseObject(result);
//        list = (List<String>) syslogJson.get("data");
//        System.out.println(list);
//    }
}
