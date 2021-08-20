package com.yuanqing.project.tiansu.service.analysis;

import com.alibaba.fastjson.JSONObject;
import com.yuanqing.framework.web.domain.BaseEntity;
import com.yuanqing.project.tiansu.domain.analysis.CameraVisit;
import com.yuanqing.project.tiansu.domain.analysis.Statistics;
import com.yuanqing.project.tiansu.domain.analysis.TerminalVisit;
import com.yuanqing.project.tiansu.domain.analysis.dto.CameraVisitDto;
import com.yuanqing.project.tiansu.domain.assets.Camera;
import com.yuanqing.project.tiansu.domain.operation.OperationBehavior;

import java.util.List;

/**
 * @author xucan
 * @version 1.0
 * @Date 2021-03-01 14:40
 */
public interface IStatisticsService {

    List<Statistics> getList(Statistics statistics);

    /**
     * 查询 相关终端操作
     * @param statistics
     * @return
     */
    List<Statistics> getClientUserList(Statistics statistics);

    List<JSONObject> getClientList(Statistics statistics);

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
    List<TerminalVisit> getTerminalVisit(TerminalVisit terminalVisit, String orderStr);


    /**
     * 获取摄像头访问列表
     * @param cameraList 摄像头集合
     * @param cameraVisit 过滤条件
     * @return
     */
    List<CameraVisitDto> getCameraVisit(List<Camera> cameraList, CameraVisit cameraVisit, String orderStr);


    /**
     * 根据 cameraVisit条件 查询统计表 deviceCodeList中含有相关摄像头
     * @param cameraList 摄像头集合
     * @param cameraVisit 过滤条件
     * @return 返回摄像头编号集合
     */
    List<String> getCameraVisited(List<Camera> cameraList,CameraVisit cameraVisit);


    /**
     * 操作行为关联摄像头信息
     *
     * @param operationBehaviorList 操作行为list
     * @return
     */
    List<JSONObject> associateCameraInfo(List<OperationBehavior> operationBehaviorList);

    /**
     * 操作行为关联终端信息
     * @param operationBehaviorList
     * @return
     */
    List<JSONObject> associateTerminalInfo(List<OperationBehavior> operationBehaviorList);

    /**
     * 根据 cameraVisit条件 查询统计表 deviceCodeList中含有相关终端IP
     * @param cameraList 摄像头集合
     * @param baseEntity 过滤条件
     * @return 返回终端IP集合
     */
    List<Long> getTerminalVisited(List<Camera> cameraList, BaseEntity baseEntity);

    List<JSONObject> getClientVisitToReport(JSONObject filters);

    List<JSONObject> getClientVisitCntToReport(JSONObject filters);

    List<JSONObject> getClientVisitRelateCameraToReport(JSONObject filters);

    List<JSONObject> getCameraVisitedToReport(CameraVisit cameraVisit,Camera camera,String orderStr);

    List<JSONObject> getCameraVisitedCntToReport(JSONObject filters);
}
