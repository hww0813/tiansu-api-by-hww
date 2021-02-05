package com.yuanqing.tiansu.assets;

import com.yuanqing.project.tiansu.domain.assets.ClientUser;
import com.yuanqing.project.tiansu.domain.assets.dto.ClientUserDto;
import com.yuanqing.project.tiansu.service.assets.IClientUserService;
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

    @Test
    public void handleClientUserTerminalNum(){

        List<ClientUser> list = clientUserService.getList(new ClientUser());
        List<ClientUserDto> dtoList = clientUserService.handleClientUserTerminalNum(list);

        System.out.println(dtoList);
    }

    @Test
    public void getActiveClientUser(){
        List<ClientUser> activeClientUser = clientUserService.getActiveClientUser();
        System.out.println(activeClientUser);
    }

    @Test
    public void changStatus(){
        String[] ids = {"1","623239475283234817"};
        boolean b = clientUserService.changStatus(ids);
        System.out.println(b);
    }

    @Test
    public void getList(){
        ClientUser clientUser = new ClientUser();

        //条件
//        clientUser.setUsername("test");
//        clientUser.setId(1L);
//        clientUser.setStatus(1);
//        clientUser.setSource("test");
        List<ClientUser> list = clientUserService.getList(clientUser);
        System.out.println(list);
    }

    @Test
    public void findById(){
        ClientUser clientUser = clientUserService.findById(623262576440119298L);
        System.out.println(clientUser);
    }

    @Test
    public void delete(){
        save();
        clientUserService.deleteById(1L);
    }

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
