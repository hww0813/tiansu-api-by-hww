package com.yuanqing.project.tiansu.mapper.analysis;

import com.alibaba.fastjson.JSONObject;
import com.yuanqing.framework.web.domain.BaseEntity;
import com.yuanqing.project.tiansu.domain.analysis.CameraVisit;
import com.yuanqing.project.tiansu.domain.analysis.Statistics;
import com.yuanqing.project.tiansu.domain.analysis.TerminalVisit;
import com.yuanqing.project.tiansu.domain.analysis.VisitedRate;
import com.yuanqing.project.tiansu.domain.assets.ClientTerminal;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author xucan
 * @version 1.0
 * @Date 2021-03-01 14:42
 */

@Repository
public interface StatisticsMapper {

    /**
     * 获取相关
     * @param statistics
     * @return
     */
    List<Statistics> getList(Statistics statistics);

    /**
     * 根据地区统计访问率
     * 根据摄像头表中的region字段分组统计
     * @param filter 过滤条件
     * @return
     */
    List<VisitedRate> visitedRate(JSONObject filter);


//    /**
//     *
//     * @param filter
//     * @return
//     */
//    List<String> getVisitedRateRelatedCamera(JSONObject filter);


    /**
     * 获取终端访问列表
     * @param terminalVisit 过滤条件
     * @return
     */
    List<TerminalVisit> getTerminalVisit(@Param("terminalVisit") TerminalVisit terminalVisit, @Param("orderStr") String orderStr);

    /**
     * 获取摄像头访问列表
     * @param deviceCodeList 摄像头编号集合
     * @param cameraVisit 过滤条件
     * @return CameraVisit对象
     */
    List<CameraVisit> getCameraVisit(@Param("list") List<String> deviceCodeList,
                                     @Param("filter") CameraVisit cameraVisit);

    /**
     * 根据 cameraVisit条件 查询统计表 deviceCodeList中含有相关摄像头
     * @param deviceCodeList 摄像头编号集合
     * @param cameraVisit 过滤条件
     * @return 返回摄像头编号集合
     */
    List<String> getCameraVisited(@Param("list") List<String> deviceCodeList,
                                  @Param("filter") CameraVisit cameraVisit);


    /**
     * 根据 terminalVisit条件 查询统计表 deviceCodeList中含有相关终端IP
     * @param deviceCodeList 摄像头编号集合
     * @param baseEntity
     * @return 返回终端IP集合
     */
    List<Long> getTerminalVisited(@Param("list") List<String> deviceCodeList,
                                     @Param("filter") BaseEntity baseEntity);


}
