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
public class ClientTerminalServiceTest {

    @Autowired
    private IClientTerminalService clientTerminalService;

    @Autowired
    private IClientService clientService;

    /**
     * 查询终端列表(数量和对应用户数) 1/2
     */
    @Test
    public void handleTerminalUserNum(){
        List<ClientTerminal> list = clientTerminalService.getList(new ClientTerminal());
        List<ClientTerminalDto> dtoList = clientTerminalService.handleTerminalUserNum(list);
//        System.out.println(dtoList);
        Assert.assertEquals(4,dtoList.size());
    }

    /**
     * 查询活跃的终端(数量和对应用户数) 1/2
     */
    @Test
    public void getActiveTerminal(){
        List<ClientTerminal> activeTerminal = clientTerminalService.getActiveTerminal();
        List<ClientTerminalDto> dtoList = clientTerminalService.handleTerminalUserNum(activeTerminal);
//        System.out.println(dtoList);
        Assert.assertEquals(1,dtoList.size());
    }

    /**
     * 永远返回1个值，有什么用途？
     */
    @Test
    public void findById(){
        ClientTerminal clientTerminal = clientTerminalService.findById(100000000000000004L);
        System.out.println("++++++++++++++++++++++");
        System.out.println(clientTerminal);
    }

    /**
     * 查询表记录总数
     */
    @Test
    public void getList(){
        ClientTerminal clientTerminal = new ClientTerminal();
        List<ClientTerminal> list = clientTerminalService.getList(clientTerminal);
        Assert.assertEquals(4,list.size());
    }

    /**
     * 按ip查数量 ip没对应上的不算--报错
     */
    @Test
    public void getTerminalByIpList(){
        Client client = new Client();
        client.setUsername("001");
        List<Client> clientList = clientService.getList(client);

        List<ClientTerminal> list = clientTerminalService.getTerminalByClientList(clientList);

        System.out.println(list.size());
//        Assert.assertEquals(4,list.size());

    }

}
