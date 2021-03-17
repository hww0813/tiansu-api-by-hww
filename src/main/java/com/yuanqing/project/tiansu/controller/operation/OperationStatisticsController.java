package com.yuanqing.project.tiansu.controller.operation;

import com.yuanqing.framework.redis.RedisCache;
import com.yuanqing.framework.web.domain.AjaxResult;
import com.yuanqing.project.tiansu.job.IndexStatisticsTask;
import com.yuanqing.project.tiansu.service.assets.IClientTerminalService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Dong.Chao
 * @Classname OperationStatisticsController
 * @Description 手动执行页面统计相关API
 * @Date 2021/2/22 16:42
 * @Version V1.0
 */
@RestController
@RequestMapping(value = "/api/statistics")
public class OperationStatisticsController {

    @Autowired
    private IndexStatisticsTask indexStatisticsTask;

    @Autowired
    private IClientTerminalService clientTerminalService;

    @Autowired
    private RedisCache redisCache;

    private String[] timeTypes = {"DAY","WEEK","MONTH"};

    private Long[] actionTypes = {-1L,3L,1L};

    @GetMapping(value = "/camera")
    @ApiOperation(value = "手动摄像头使用率", httpMethod = "GET")
    public AjaxResult camera(){
        try {
            for (String timeType : timeTypes) {
                for (Long actionType : actionTypes) {
                    indexStatisticsTask.visitCamera(timeType,actionType);
                }
            }
            indexStatisticsTask.initIndexAreaCache();
            return AjaxResult.success();
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error();
        }
    }



    @GetMapping(value = "/cameraRatio")
    @ApiOperation(value = "手动摄像头访问率", httpMethod = "GET")
    public AjaxResult cameraRatio(){
        try {
            indexStatisticsTask.initIndexAreaCache();
            return AjaxResult.success();
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error();
        }
    }


    @GetMapping(value = "/client")
    @ApiOperation(value = "手动终端访问", httpMethod = "GET")
    public AjaxResult client(){
        try {
            for (String timeType : timeTypes) {
                for (Long actionType : actionTypes) {
                    indexStatisticsTask.visitClient(timeType,actionType);
                }
            }
            return AjaxResult.success();
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error();
        }
    }


    @GetMapping(value = "/user")
    @ApiOperation(value = "手动用户访问", httpMethod = "GET")
    public AjaxResult user(){
        try {
            for (String timeType : timeTypes) {
                for (Long actionType : actionTypes) {
                    indexStatisticsTask.userClient(timeType,actionType);
                }
            }
            return AjaxResult.success();
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error();
        }
    }


    @GetMapping(value = "/card")
    @ApiOperation(value = "手动刷新首页卡片统计", httpMethod = "GET")
    public AjaxResult card(){
        try {
            indexStatisticsTask.cacheIndexCounts();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AjaxResult.success();
    }







}
