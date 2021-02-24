package com.yuanqing.tiansu.event;

import com.yuanqing.project.tiansu.mapper.assets.OperationBehaviorMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author xucan
 * @version 1.0
 * @Date 2021-02-24 17:28
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class OperationBehaviorServiceTest {

    @Autowired
    private OperationBehaviorMapper operationBehaviorMapper;

    @Test
    public void getDstIpBySessionId(){

        List<Long> dstIpBySessionId = operationBehaviorMapper.getDstIpBySessionId(630423123174494209L);

        System.out.println(dstIpBySessionId);
    }

}
