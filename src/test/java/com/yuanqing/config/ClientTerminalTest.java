package com.yuanqing.config;


import com.yuanqing.project.tiansu.service.IClientTerminalService;
import lombok.ToString;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;



@SpringBootTest
@RunWith(SpringRunner.class)
public class ClientTerminalTest {


    @Autowired
    private IClientTerminalService clientTerminalService;


    @Test
    public void findById(){
        clientTerminalService.findById(null);
    }


}
