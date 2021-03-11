package com.yuanqing.tiansu.assets;

import com.yuanqing.project.tiansu.domain.assets.Camera;
import com.yuanqing.project.tiansu.service.assets.ICameraService;
import org.junit.After;
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
 * 测试库 192.168.1.20：test
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
        camera.setStatus(1);
        camera.setIsGb(1);
        camera.setDeviceCode("34011141321190120160");
        camera.setDeviceType("1");
        camera.setRegion(340111);

        List<Camera> getList = cameraService.getList(camera);
        System.out.println(getList);
    }

    /**
     * 按条件获取摄像头列表 1/2
     */
    @Test
    public void testGetList1() {
        //条件：deviceCode
        Camera camera = new Camera();
        camera.setDeviceCode("34011141321190120160");
        List<Camera> getList = cameraService.getList(camera);

        Assert.assertEquals(1, getList.size());
        System.out.println(getList);
    }

    @Test
    public void testGetList2() {
        //条件：region
        Camera camera = new Camera();
        camera.setRegion(340112);
        List<Camera> getList = cameraService.getList(camera);
        Assert.assertEquals(4, getList.size());
    }

    @Test
    public void testGetList3() {
        //条件：status
        Camera camera = new Camera();
        camera.setStatus(0);
        List<Camera> getList = cameraService.getList(camera);
        System.out.println(getList);
        Assert.assertEquals(1, getList.size());
    }

    @Test
    public void testGetList4() {
        //条件：status
        Camera camera = new Camera();
        camera.setStatus(1);
        List<Camera> getList = cameraService.getList(camera);
        Assert.assertEquals(1, getList.size());
    }

    @Test
    public void testGetList5() {
        //条件：status
        Camera camera = new Camera();
        camera.setStatus(2);
        List<Camera> getList = cameraService.getList(camera);
        System.out.println(getList);
        Assert.assertEquals(1, getList.size());
    }

    @Test
    public void testGetList6() {
        //条件：status
        Camera camera = new Camera();
        camera.setStatus(3);
        List<Camera> getList = cameraService.getList(camera);
        Assert.assertEquals(2, getList.size());
    }

    @Test
    public void testGetList7() {
        //条件：is_gb
        Camera camera = new Camera();
        camera.setIsGb(0);
        List<Camera> getList = cameraService.getList(camera);
        System.out.println(getList);
        Assert.assertEquals(2, getList.size());
    }

    @Test
    public void testGetList8() {
        //条件：is_gb
        Camera camera = new Camera();
        camera.setIsGb(1);
        List<Camera> getList = cameraService.getList(camera);
        Assert.assertEquals(3, getList.size());
    }

    @Test
    public void testGetList9() {
        //条件：is_probe
        Camera camera = new Camera();
        camera.setIsProbe("0");
        List<Camera> getList = cameraService.getList(camera);
        Assert.assertEquals(2, getList.size());
    }

    @Test
    public void testGetList10() {
        //条件：is_probe
        Camera camera = new Camera();
        camera.setIsProbe("1");
        List<Camera> getList = cameraService.getList(camera);
        Assert.assertEquals(3, getList.size());
    }


    /**
     * 查询国标、非国标摄像头数量
     * gb:国标
     * ngb:非国标
     */
    @Test
    public void testGetNonNationalCamera() {
        Map<String, Long> nonNationalCamera = cameraService.getNonNationalCamera();
        System.out.println(nonNationalCamera);
        for (String key : nonNationalCamera.keySet()) {
            Long v_ngb = nonNationalCamera.get("ngb");
            Long v_gb = nonNationalCamera.get("gb");
            Assert.assertEquals("2", v_ngb.toString()); //非国标摄像头数
            Assert.assertEquals("3", v_gb.toString()); //国标摄像头数
        }
    }

    /**
     * 查询国标/非国标摄像头列表
     * 0:非国标
     * 1:国标
     */
    @Test
    public void testGetNonNationalCameraList1() {
        Camera camera = new Camera();
        camera.setIsGb(0);
        List<Camera> nonNationalCameraList = cameraService.getNonNationalCameraList(camera);
        System.out.println(nonNationalCameraList); //跟getList有啥区别？
        Assert.assertEquals(2, nonNationalCameraList.size());
    }

    @Test
    public void testGetNonNationalCameraList2() {
        Camera camera = new Camera();
        camera.setIsGb(1);
        List<Camera> nonNationalCameraList = cameraService.getNonNationalCameraList(camera);
        System.out.println(nonNationalCameraList);
        Assert.assertEquals(3, nonNationalCameraList.size());
    }

    /**
     * 获取活跃摄像头列表
     */
    @Test
    public void testGetActiveCamera() {
        List<Camera> activeCamera = cameraService.getActiveCamera();
        Assert.assertEquals(2, activeCamera.size()); //如果有非测试数据的今日数据，就不准了
        System.out.println(activeCamera);
    }


    /**
     * 查询【当天】的摄像头状态有几种，对应几条数据 1/2
     * <p>
     * 0:已确认
     * 1:新发现
     */
    @Test
    public void testGetCurrentStatus() {
        Map<Integer, Long> currentStatus = cameraService.getCurrentStatus();
        System.out.println("=======================");
        System.out.println(currentStatus);
        Assert.assertEquals(2, currentStatus.size());
    }
}
