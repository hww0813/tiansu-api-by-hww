package com.yuanqing.tiansu.assets;

import com.yuanqing.common.utils.ip.IpUtils;
import com.yuanqing.project.tiansu.domain.assets.Camera;
import com.yuanqing.project.tiansu.service.assets.ICameraService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * Created by xucan on 2021-01-28 17:52
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class CameraServiceTest {

    @Autowired
    private ICameraService cameraService;

    @Test
    public void getList(){
        Camera camera = new Camera();
        camera.setStatus(1);
        camera.setIsGb(1);
        camera.setDeviceCode("34011142001190120008");
        camera.setDeviceType("5");
        camera.setRegion(340111);

        List<Camera> getList = cameraService.getList(camera);
        System.out.println(getList);
    }

    @Test
    public void changStatus(){

        String[] ids = {"615275032695934977","617808085842530305"};
        cameraService.changStatus(ids);
    }
}
