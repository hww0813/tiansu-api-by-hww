package com.yuanqing.tiansu.analysis;

import com.alibaba.fastjson.JSONObject;
import com.yuanqing.project.tiansu.mapper.analysis.ScreenMapper;
import com.yuanqing.project.tiansu.service.analysis.IScreenService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

/**
 * @author xucan
 * @version 1.0
 * @Date 2021-04-02 11:37
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class ScreenServiceTest {

    @Autowired
    private ScreenMapper screenMapper;

    @Autowired
    private IScreenService screenService;

    @Test
    public void q(){
        List<JSONObject> categoryList = screenMapper.getOperCategory();

        System.out.println(categoryList);

    }

    @Test
    public void getOperCategory(){
        String operWarn = screenService.getOperWarn(new Date());
        System.out.println(operWarn);
    }

}
