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

import java.util.ArrayList;
import java.util.Arrays;
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
    @Ignore
    public void testGetList0() {
        Camera camera = new Camera();
//        camera.setStatus(0);
//        camera.setIsGb(0);
        camera.setDeviceCode("11111111111111111111");
//        camera.setDeviceType("5");
//        camera.setRegion(340111);

        List<Camera> getList = cameraService.getList(camera);
        System.out.println(getList);
    }

    @Test
    public void testGetList1() {
        //条件：deviceCode
        Camera camera = new Camera();
        camera.setDeviceCode("22222222222222222222");
        List<Camera> getList = cameraService.getList(camera);
        Assert.assertEquals(1, getList.size());
    }

    @Test
    public void testGetList2() {
        //条件：region
        Camera camera = new Camera();
        camera.setDeviceCode("11111111111111111111");
        camera.setRegion(340002);
        List<Camera> getList = cameraService.getList(camera);
        Assert.assertEquals(1, getList.size());
    }

    @Test
    public void testGetList3() {
        //条件：status
        Camera camera = new Camera();
        camera.setDeviceCode("11111111111111111111");
        camera.setStatus(0);
        List<Camera> getList = cameraService.getList(camera);
        System.out.println(getList);
        Assert.assertEquals(1, getList.size());
    }

    @Test
    public void testGetList4() {
        //条件：status
        Camera camera = new Camera();
        camera.setDeviceCode("11111111111111111111");
        camera.setStatus(1);
        List<Camera> getList = cameraService.getList(camera);
        Assert.assertEquals(4, getList.size());
    }

    @Test
    public void testGetList5() {
        //条件：status
        Camera camera = new Camera();
        camera.setDeviceCode("11111111111111111111");
        camera.setStatus(2);
        List<Camera> getList = cameraService.getList(camera);
        System.out.println(getList);
        Assert.assertEquals(1, getList.size());
    }

    @Test
    public void testGetList6() {
        //条件：status
        Camera camera = new Camera();
        camera.setDeviceCode("11111111111111111111");
        camera.setStatus(3);
        List<Camera> getList = cameraService.getList(camera);
        Assert.assertEquals(1, getList.size());
    }

    @Test
    public void testGetList7() {
        //条件：is_gb
        Camera camera = new Camera();
        camera.setDeviceCode("11111111111111111111");
        camera.setIsGb(0);
        List<Camera> getList = cameraService.getList(camera);
        System.out.println(getList);
        Assert.assertEquals(1, getList.size());
    }

    @Test
    public void testGetList8() {
        //条件：is_gb
        Camera camera = new Camera();
        camera.setDeviceCode("11111111111111111111");
        camera.setIsGb(1);
        List<Camera> getList = cameraService.getList(camera);
        Assert.assertEquals(6, getList.size());
    }

    @Test
    public void testGetList9() {
        //条件：is_probe
        Camera camera = new Camera();
        camera.setDeviceCode("11111111111111111111");
        camera.setIsProbe("1");
        List<Camera> getList = cameraService.getList(camera);
        Assert.assertEquals(6, getList.size());
    }

    @Test
    public void testGetList10() {
        //条件：is_probe
        Camera camera = new Camera();
        camera.setDeviceCode("11111111111111111111");
        camera.setIsProbe("0");
        List<Camera> getList = cameraService.getList(camera);
        Assert.assertEquals(1, getList.size());
    }


    /**
     * 查询国标、非国标摄像头数量
     * gb:国标
     * ngb:非国标
     */
    @Test
    public void testGetNonNationalCamera() {
        Map<String, Long> nonNationalCamera = cameraService.getNonNationalCamera();
//        System.out.println(nonNationalCamera);
        for(String key : nonNationalCamera.keySet()){
            Long v_ngb = nonNationalCamera.get("ngb");
            Long v_gb = nonNationalCamera.get("gb");
            Assert.assertEquals("5", v_ngb.toString()); //非国标摄像头数
            Assert.assertEquals("200", v_gb.toString()); //国标摄像头数
        }
    }

    /**
     * 查询国标/非国标摄像头列表
     * 0:非国标
     * 1:国标
     */
    @Test
    public void testGetNonNationalCameraList() {
        Camera camera = new Camera();
        camera.setIsGb(0);
        List<Camera> nonNationalCameraList = cameraService.getNonNationalCameraList(camera);
        System.out.println(nonNationalCameraList); //跟getList有啥区别？
    }

    /**
     * 查询当天摄像头状态 对应数量
     * <p>
     * 0:已确认
     * 1:新发现
     */
    @Test
    public void testGetCurrentStatus() {
        Map<Integer, Long> currentStatus = cameraService.getCurrentStatus();
        System.out.println(currentStatus);
        if (currentStatus.size()==0){
            System.out.println("当天没有摄像头数据");
        } else{
            Assert.assertEquals(currentStatus.size(),1);
        }
    }

    /**
     * 批量确认摄像头,只能1改成0,如果有更改就为true，没有更改就为false，要改成返回更改的数量
     */
    @Test
    public void testChangStatus() {
        String[] ids = {"600000000000000001", "600000000000000002"};
        boolean id = cameraService.changStatus(ids);
        System.out.println(id);
        Assert.assertEquals(id,true);
    }

    /**
     * 同上
     */
    @Test
    public void testChangAllStatus() {
        boolean flag = cameraService.changAllStatus();
        System.out.println(flag);
    }

    /**
     * 获取活跃摄像头列表
     */
    @Test
    public void testGetActiveCamera() {
        List<Camera> activeCamera = cameraService.getActiveCamera();
        System.out.println(activeCamera);
        Assert.assertEquals(3,activeCamera); //如果有非测试数据的今日数据，就不准了

    }
}
