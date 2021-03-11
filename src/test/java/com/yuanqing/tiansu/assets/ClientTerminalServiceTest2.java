package com.yuanqing.tiansu.assets;

import com.yuanqing.common.enums.SaveType;
import com.yuanqing.project.tiansu.domain.assets.Client;
import com.yuanqing.project.tiansu.domain.assets.ClientTerminal;
import com.yuanqing.project.tiansu.domain.assets.dto.ClientTerminalDto;
import com.yuanqing.project.tiansu.service.assets.IClientService;
import com.yuanqing.project.tiansu.service.assets.IClientTerminalService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * Created by xucan on 2021-02-03 15:26
 */


@SpringBootTest
@RunWith(SpringRunner.class)
public class ClientTerminalServiceTest2 {

    @Autowired
    private IClientTerminalService clientTerminalService;

    @Autowired
    private IClientService clientService;



    /**
     * 批量更新终端状态 1改成0, 改为0后返回true
     */
    @Test
    public void changeStatus(){
        String[] ids = {"100000000000000001","100000000000000002"};
        boolean b = clientTerminalService.changStatus(ids);
        Assert.assertEquals(true,b);
    }


    /**
     * 新增一条记录
     */
    @Test
    public void save(){
        ClientTerminal clientTerminal = new ClientTerminal();
        clientTerminal.setId(2021L);
        clientTerminal.setIpAddress(11111L);
        clientTerminal.setDeviceCode("1111");
        clientTerminal.setDeviceName("testTerminal");
        clientTerminal.setStatus(1);
        Long i = clientTerminalService.save(clientTerminal, SaveType.INSERT);
        Assert.assertEquals("2021",i.toString());
    }


    /**
     * 根据id删除记录
     */
    @Test
    public void delete(){
        ClientTerminal clientTerminal = new ClientTerminal();
        List<ClientTerminal> list1 = clientTerminalService.getList(clientTerminal);

        clientTerminalService.deleteById(2021L);

        Assert.assertEquals(4,list1.size()-1);
    }



}
