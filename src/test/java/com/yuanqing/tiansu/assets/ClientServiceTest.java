package com.yuanqing.tiansu.assets;

import com.alibaba.fastjson.JSONObject;
import com.yuanqing.project.tiansu.service.assets.IClientService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xucan on 2021-02-03 21:02
 */


@SpringBootTest
@RunWith(SpringRunner.class)
public class ClientServiceTest {

    @Autowired
    private IClientService clientService;


    /**
     * 根据终端ip 获取相应用户数
     */
    @Test
    public void getUserNumByTerminal(){

        List<Long> list = new ArrayList<>();
        list.add(1111111001L);
        list.add(1111111003L);
        List<JSONObject> userNumByTerminal = clientService.getUserNumByTerminal(list);
        String[] v_userCnt = new String[2];
        for (int i = 0; i < userNumByTerminal.size(); i++) {
            JSONObject jsonObject = userNumByTerminal.get(i);
            v_userCnt[i] = jsonObject.getString("userCnt");
        }
        Assert.assertEquals("1",v_userCnt[0]); //检验第1个ip的用户数为1
        Assert.assertEquals("3",v_userCnt[1]); //检验第2个ip的用户数为3
    }
}
