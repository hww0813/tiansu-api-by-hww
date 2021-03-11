package com.yuanqing.tiansu.assets;

import com.yuanqing.common.enums.SaveType;
import com.yuanqing.project.tiansu.domain.assets.ClientUser;
import com.yuanqing.project.tiansu.domain.assets.dto.ClientUserDto;
import com.yuanqing.project.tiansu.service.assets.IClientUserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * Created by xucan on 2021-02-03 23:21
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ClientUserServiceTest {

    @Autowired
    private IClientUserService clientUserService;

    /**
     * 查询用户列表（数量和对应终端数）1/2
     */
    @Test
    public void handleClientUserTerminalNum(){

        List<ClientUser> list = clientUserService.getList(new ClientUser());
        List<ClientUserDto> dtoList = clientUserService.handleClientUserTerminalNum(list);
        System.out.println(dtoList);
        Assert.assertEquals(5,dtoList.size());
    }

    /**
     * 查询活跃用户（数量和对应终端数）1/2
     */
    @Test
    public void getActiveClientUser(){
        List<ClientUser> activeClientUser = clientUserService.getActiveClientUser();
//        System.out.println(activeClientUser);
        Assert.assertEquals(3,activeClientUser.size());
    }


    /**
     * 根据条件查询用户列表(多条件待添加)
     */
    @Test
    public void getList(){
        ClientUser clientUser = new ClientUser();

        //条件
        clientUser.setUsername("001");
//        clientUser.setId(1L);
        clientUser.setStatus(0);
//        clientUser.setSource("test");
        List<ClientUser> list = clientUserService.getList(clientUser);
        System.out.println(list);
        Assert.assertEquals(1,list.size());
    }

    /**
     * 按id查找用户，包括ip  --1/2
     */
    @Test
    public void findById(){
        ClientUser clientUser = clientUserService.findById(100000000000000001L);
        System.out.println(clientUser);
    }

}
