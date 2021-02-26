package com.yuanqing.tiansu.assets;

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
     * 查询用户列表（包含终端数）1/2
     */
    @Test
    public void handleClientUserTerminalNum(){

        List<ClientUser> list = clientUserService.getList(new ClientUser());
        List<ClientUserDto> dtoList = clientUserService.handleClientUserTerminalNum(list);
        System.out.println(dtoList);
        Assert.assertEquals(32,dtoList.size());
    }

    /**
     * 活跃用户？？
     */
    @Test
    public void getActiveClientUser(){
        List<ClientUser> activeClientUser = clientUserService.getActiveClientUser();
        System.out.println(activeClientUser);
    }

    /**
     * 1改成0
     */
    @Test
    public void changStatus(){
        String[] ids = {"1","600000000000000001"};
        boolean b = clientUserService.changStatus(ids);
        Assert.assertEquals(true,b);
    }

    /**
     * 条件查询用户列表？？？
     */
    @Test
    public void getList(){
        ClientUser clientUser = new ClientUser();

        //条件
        clientUser.setUsername("admin");
//        clientUser.setId(1L);
//        clientUser.setStatus(1);
//        clientUser.setSource("test");
        List<ClientUser> list = clientUserService.getList(clientUser);
        System.out.println(list);
    }

    /**
     * 按id查找用户，包括ip
     */
    @Test
    public void findById(){
        ClientUser clientUser = clientUserService.findById(600000000000000001L);
        System.out.println(clientUser);
    }

    /**
     * 删除用户
     */
    @Test
    public void delete(){
        save();
        clientUserService.deleteById(1L);
    }

    /**
     * 新增用户
     */
    @Test
    public void save(){
        ClientUser clientUser = new ClientUser();
        clientUser.setUsername("test");
        clientUser.setSource("test");
        clientUser.setStatus(1);
        clientUser.setId(1L);
        Long i = clientUserService.save(clientUser);
        System.out.println(i);
    }

}
