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
public class ClientUserServiceTest2 {

    @Autowired
    private IClientUserService clientUserService;

    /**
     * 1改成0,永远返回true
     */
    @Test
    public void changStatus(){
        String[] ids = {"100000000000000001","100000000000000002"};
        boolean b = clientUserService.changStatus(ids);
        Assert.assertEquals(true,b);
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
        Long i = clientUserService.save(clientUser, SaveType.INSERT);
        System.out.println(i);
    }

    /**
     * 删除用户
     */
    @Test
    public void delete(){
//        save();
        clientUserService.deleteById(1L);

        List<ClientUser> list = clientUserService.getList(new ClientUser());
        List<ClientUserDto> dtoList = clientUserService.handleClientUserTerminalNum(list);
        Assert.assertEquals(5,dtoList.size());
    }


}
