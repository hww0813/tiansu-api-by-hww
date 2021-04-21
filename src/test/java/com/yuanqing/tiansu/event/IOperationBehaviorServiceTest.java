package com.yuanqing.tiansu.event;

import com.alibaba.fastjson.JSONObject;
import com.yuanqing.project.tiansu.domain.operation.RawNetFlow;
import com.yuanqing.project.tiansu.mapper.operation.OperationBehaviorMapper;
import com.yuanqing.project.tiansu.mapper.operation.RawNetFlowMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xucan
 * @version 1.0
 * @Date 2021-02-24 17:28
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class IOperationBehaviorServiceTest {

    @Autowired
    private OperationBehaviorMapper operationBehaviorMapper;

    @Autowired
    private RawNetFlowMapper rawNetFlowMapper;

    @Test
    public void getDstIpBySessionId(){

        List<Long> dstIpBySessionId = operationBehaviorMapper.getDstIpBySessionId(630423123174494209L);

        System.out.println(dstIpBySessionId);
    }

    @Test
    public void getTest(){

//        RawNetFlow rawNetFlow = new RawNetFlow();
//
//        rawNetFlow.setDstIp(3232235796L);
//        List<JSONObject> trendList = rawNetFlowMapper.getRawFlowTrend(rawNetFlow);
//        List<String> hourList = new ArrayList<>();
//
//
//        for(JSONObject json :trendList){
//            hourList.add(json.getString("Hour"));
//            }
//        for(int i=0;i<24;i++) {
//            if (hourList.contains(String.valueOf(i))) {
//
//            } else {
//                JSONObject jsonObject = new JSONObject();
//                jsonObject.put("Hour", i);
//                jsonObject.put("Size", "0");
//                jsonObject.put("Count", "0");
//                trendList.add(jsonObject);
//            }
//        }
//        List<JSONObject> list2 = rawNetFlowMapper.getServerFlowRelationClient(rawNetFlow);
//        System.out.println(trendList.toString());
    }

}
