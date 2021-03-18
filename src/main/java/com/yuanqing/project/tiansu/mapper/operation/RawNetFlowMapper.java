package com.yuanqing.project.tiansu.mapper.operation;

import com.yuanqing.project.tiansu.domain.operation.RawNetFlow;

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
}
