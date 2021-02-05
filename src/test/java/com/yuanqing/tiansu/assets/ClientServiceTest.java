package com.yuanqing.tiansu.assets;

import com.alibaba.fastjson.JSONObject;
import com.yuanqing.project.tiansu.service.assets.IClientService;
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
        list.add(3232287076L);
        list.add(2896692008L);
        List<JSONObject> userNumByTerminal = clientService.getUserNumByTerminal(list);
        System.out.println(userNumByTerminal);

    }
}
