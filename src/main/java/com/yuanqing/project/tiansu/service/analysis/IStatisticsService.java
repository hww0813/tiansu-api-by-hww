package com.yuanqing.project.tiansu.service.analysis;

import com.alibaba.fastjson.JSONObject;
import com.yuanqing.project.tiansu.domain.analysis.CameraVisit;
import com.yuanqing.project.tiansu.domain.analysis.TerminalVisit;
import com.yuanqing.project.tiansu.domain.assets.Camera;
import com.yuanqing.project.tiansu.domain.operation.OperationBehavior;

import java.util.List;

/**
 * @author xucan
 * @version 1.0
 * @Date 2021-03-01 14:40
 */
public interface IStatisticsService {

    /**
     * 获取下级地区访问率
     * @param regionId 地区代码
     * @param dateType 时间范围 （日/月/年）
     * @return
     */
    List<JSONObject> getVisitedRate(String regionId,String dateType);

    /**
     * 获取终端访问列表
     * @param terminalVisit 过滤条件
     * @return
     */
    List<TerminalVisit> getTerminalVisit(TerminalVisit terminalVisit);

    /**
     * 获取摄像头访问列表
     * @param cameraList 摄像头集合
     * @param cameraVisit 过滤条件
     * @return
     */

    List<CameraVisit> getCameraVisit(List<Camera> cameraList,CameraVisit cameraVisit);
    /**
     * 操作行为关联摄像头信息
     *
     * @param operationBehaviorList 操作行为list
     * @return
     */
    List<JSONObject> associateCameraInfo(List<OperationBehavior> operationBehaviorList);

}
