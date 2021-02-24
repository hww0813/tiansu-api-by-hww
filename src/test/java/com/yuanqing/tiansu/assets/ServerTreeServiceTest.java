package com.yuanqing.tiansu.assets;

import com.yuanqing.project.tiansu.domain.assets.ServerTree;
import com.yuanqing.project.tiansu.mapper.assets.OperationBehaviorMapper;
import com.yuanqing.project.tiansu.service.assets.IServerTreeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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
     * 根据sessionId 查询服务器
     */
    @Test
    public void getSessionServerList(){

        //过滤条件
        ServerTree serverTree = new ServerTree();

        //1.根据sessionID 查询操作行为表，得到相关操作行为到dst_ip 集合
        List<Long> dstIpList = operationBehaviorMapper.getDstIpBySessionId(630423123174494209L);

        //2.根据dst_ip集合 和serverTree 过滤条件 查询服务器表，得到sessionId 相关服务器列表
        List<ServerTree> sessionServerList = serverTreeService.getSessionServerList(serverTree, dstIpList);

        System.out.println(sessionServerList);
    }
}
