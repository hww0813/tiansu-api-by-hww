package com.yuanqing.tiansu.assets;

import com.yuanqing.common.enums.SaveType;
import com.yuanqing.project.tiansu.domain.assets.Client;
import com.yuanqing.project.tiansu.domain.assets.ClientTerminal;
import com.yuanqing.project.tiansu.domain.assets.dto.ClientTerminalDto;
import com.yuanqing.project.tiansu.service.assets.IClientService;
import com.yuanqing.project.tiansu.service.assets.IClientTerminalService;
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
     * 批量更新终端状态
     */
    @Test
    public void changeStatus(){
        String[] ids = {"615998687822352385", "617808842226536449"};
        boolean b = clientTerminalService.changStatus(ids);
        System.out.println(b);
    }

    /**
     * 查询终端列表（包含用户数）
     */
    @Test
    public void handleTerminalUserNum(){
        List<ClientTerminal> list = clientTerminalService.getList(new ClientTerminal());
        List<ClientTerminalDto> dtoList = clientTerminalService.handleTerminalUserNum(list);
        System.out.println(dtoList);
    }

    @Test
    public void getActiveTerminal(){
        List<ClientTerminal> activeTerminal = clientTerminalService.getActiveTerminal();

        List<ClientTerminalDto> dtoList = clientTerminalService.handleTerminalUserNum(activeTerminal);
        System.out.println(dtoList);
    }

    @Test
    public void findById(){
        ClientTerminal clientTerminal = clientTerminalService.findById(1L);
        System.out.println(clientTerminal);
    }

    @Test
    public void getList(){
        ClientTerminal clientTerminal = new ClientTerminal();
        List<ClientTerminal> list = clientTerminalService.getList(clientTerminal);
        System.out.println(list);
    }

    @Test
    public void delete(){
        clientTerminalService.deleteById(1L);
    }

    @Test
    public void save(){
        ClientTerminal clientTerminal = new ClientTerminal();
        clientTerminal.setId(1L);
        clientTerminal.setIpAddress(11111L);
        clientTerminal.setDeviceCode("1111");
        clientTerminal.setDeviceName("testTerminal");
        clientTerminal.setStatus(1);
        Long i = clientTerminalService.save(clientTerminal, SaveType.INSERT);
        System.out.println(i);
    }

    @Test
    public void getTerminalByIpList(){
        Client client = new Client();
        client.setUsername("admin");
        List<Client> clientList = clientService.getList(client);

        List<ClientTerminal> list = clientTerminalService.getTerminalByIpList(clientList);

        System.out.println(list);

    }


}
