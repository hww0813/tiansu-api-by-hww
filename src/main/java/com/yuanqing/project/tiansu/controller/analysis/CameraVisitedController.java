package com.yuanqing.project.tiansu.controller.analysis;

import com.yuanqing.framework.web.controller.BaseController;
import com.yuanqing.framework.web.domain.AjaxResult;
import com.yuanqing.project.tiansu.domain.analysis.CameraVisit;
import com.yuanqing.project.tiansu.domain.assets.Camera;
import com.yuanqing.project.tiansu.service.analysis.IStatisticsService;
import com.yuanqing.project.tiansu.service.assets.ICameraService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author xucan
 * @version 1.0
 * @Date 2021-03-08 15:19
 */

@RestController
@RequestMapping(value = "/api/analysis/camera/visited")
@Api(value = "摄像头被访问接口", description = "摄像头被访问相关Api")
public class CameraVisitedController extends BaseController {


    @Autowired
    private IStatisticsService statisticsService;

    @Autowired
    private ICameraService cameraService;


    @GetMapping("/list")
    @ApiOperation(value = "获取摄像头被访问列表", httpMethod = "GET")
    public AjaxResult getCameraVisitedList(Camera camera) {




        CameraVisit cameraVisit = new CameraVisit();

        cameraVisit.setstartDate(camera.getstartDate());
        cameraVisit.setendDate(camera.getendDate());

        camera.setstartDate(null);
        camera.setendDate(null);

        startPage();
        List<Camera> cameraList = cameraService.getList(camera);

        List<CameraVisit> cameraVisitList = statisticsService.getCameraVisit(cameraList,cameraVisit);



        return AjaxResult.success(getDataTable(cameraVisitList));


    }

}
