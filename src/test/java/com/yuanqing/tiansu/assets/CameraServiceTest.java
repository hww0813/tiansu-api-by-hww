package com.yuanqing.tiansu.assets;

import com.yuanqing.project.tiansu.domain.assets.Camera;
import com.yuanqing.project.tiansu.service.assets.ICameraService;
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
public class CameraServiceTest {

    @Autowired
    private ICameraService cameraService;


    /**
     * 获取摄像头列表
     */
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


    /**
     * 查询国标、非国标摄像头数量
     * gb:国标
     * ngb:非国标
     */
    @Test
    public void getNonNationalCamera(){
       Map<String,Long> nonNationalCamera = cameraService.getNonNationalCamera();
        System.out.println(nonNationalCamera);
    }

    /**
     * 查询国标/非国标摄像头列表
     * 0:非国标
     * 1:国标
     */
    @Test
    public void getNonNationalCameraList(){
        Camera camera = new Camera();
        camera.setIsGb(0);
        List<Camera> nonNationalCameraList = cameraService.getNonNationalCameraList(camera);
        System.out.println(nonNationalCameraList);
    }

    /**
     * 查询当天摄像头状态
     *
     * 0:已确认
     * 1:新发现
     */
    @Test
    public void getCurrentStatus(){
        Map<Integer, Long> currentStatus = cameraService.getCurrentStatus();
        System.out.println(currentStatus);
    }

    /**
     * 批量确认摄像头
     */
    @Test
    public void changStatus(){
        String[] ids = {"615275032695934977","617808085842530305"};
        cameraService.changStatus(ids);
    }

    @Test
    public void changAllStatus(){
        boolean flag = cameraService.changAllStatus();

    }

    /**
     * 获取活跃摄像头列表
     */
    @Test
    public void getActiveCamera(){
        List<Camera> activeCamera = cameraService.getActiveCamera();
        System.out.println(activeCamera);

    }
}
