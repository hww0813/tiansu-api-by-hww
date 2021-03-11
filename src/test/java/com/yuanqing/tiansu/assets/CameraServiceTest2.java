package com.yuanqing.tiansu.assets;

import com.yuanqing.project.tiansu.domain.assets.Camera;
import com.yuanqing.project.tiansu.service.assets.ICameraService;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

/**
 * Created by xucan on 2021-01-28 17:52
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class CameraServiceTest2 {

    @Autowired
    private ICameraService cameraService;

    /**
     * 批量确认摄像头,只能1改成0,如果有更改就为true，没有更改就为false，要改成返回更改的数量
     */
    @Test
    public void testChangStatus1() {
        String[] ids = {"20736313431"};
        boolean b = cameraService.changStatus(ids);
        Assert.assertEquals(true, b);
    }

    @Test
    public void testChangStatus2() {
        String[] ids = {"20736313432","632418120736313430"};
        boolean b = cameraService.changStatus(ids);
        Assert.assertEquals(true, b);
    }

    /**
     * 全部确认
     */
    @Test
    public void testChangAllStatus() {
        boolean flag = cameraService.changAllStatus();
        System.out.println(flag);
        Assert.assertEquals(true,flag);
    }

}
