package com.yuanqing.project.tiansu.mapper.operation;

import com.alibaba.fastjson.JSONObject;
import com.yuanqing.project.tiansu.domain.operation.RawNetFlow;
import io.lettuce.core.dynamic.annotation.Param;

import java.util.Date;
import java.util.List;

public interface RawNetFlowMapper {

    /**
     * 查询原始流量
     *
     * @param id 原始流量ID
     * @return 原始流量
     */
    public RawNetFlow selectBusiRawNetFlowById(Long id);

    /**
     * 查询原始流量列表
     *
     * @param busiRawNetFlow 原始流量
     * @return 原始流量集合
     */
    public List<RawNetFlow> selectBusiRawNetFlowList(RawNetFlow busiRawNetFlow);

    /**
     * 新增原始流量
     *
     * @param busiRawNetFlow 原始流量
     * @return 结果
     */
    public int insertBusiRawNetFlow(RawNetFlow busiRawNetFlow);

    /**
     * 修改原始流量
     *
     * @param busiRawNetFlow 原始流量
     * @return 结果
     */
    public int updateBusiRawNetFlow(RawNetFlow busiRawNetFlow);

    /**
     * 删除原始流量
     *
     * @param id 原始流量ID
     * @return 结果
     */
    public int deleteBusiRawNetFlowById(Long id);

    /**
     * 批量删除原始流量
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteBusiRawNetFlowByIds(Long... ids);

    /**
     * 获取服务器相关访问的端口
     *
     * @return 结果
     */
    List<JSONObject>getServerRelationPort (JSONObject jsonObject);
    /**
     *
     *获取服务器当天的流量趋势
     *
     */
    List<JSONObject>getRawFlowTrend (@Param("dstIp") Long dstIp,@Param("startTime") Date startTime,@Param("endTime") Date endTime);


    /**
     *
     *获取终端当天的流量趋势
     *
     */
    List<JSONObject>getClientRawFlowTrend (@Param("srcIp") Long srcIp,@Param("startTime") Date startTime,@Param("endTime") Date endTime);


    /**
     *
     *获取服务器相关终端
     *
     */
    List<JSONObject> getServerFlowRelationClient (RawNetFlow rawNetFlow);

    /**
     * 获取流量列表终端排行
     * @param startTime
     * @param endTime
     * @param orderType
     * @return
     */
    List<JSONObject> getRawClientRank(Date startTime, Date endTime, String orderType);
}
