package com.yuanqing.project.tiansu.service.operation;

import java.util.List;

import com.alibaba.fastjson.JSONObject;

import com.yuanqing.project.tiansu.domain.operation.BusiHttpPerf;

/**
 * http接口审计Service接口
 *
 * @author xucan
 * @date 2021-05-17
 */
public interface IBusiHttpPerfService {
    /**
     * 查询http接口审计
     *
     * @param id http接口审计ID
     * @return http接口审计
     */
    public BusiHttpPerf selectBusiHttpPerfById(Long id);

    /**
     * 查询http接口审计列表
     *
     * @param busiHttpPerf http接口审计
     * @return http接口审计集合
     */
    public List<BusiHttpPerf> selectBusiHttpPerfList(BusiHttpPerf busiHttpPerf);


    /**
     * 聚合查询 接口请求服务接口数量
     *
     * @param busiHttpPerf http接口审计
     * @return http接口审计集合
     */
    public List<JSONObject> selctHttpPerfListGroupByDstHost(BusiHttpPerf busiHttpPerf);

    /**
     * 新增http接口审计
     *
     * @param busiHttpPerf http接口审计
     * @return 结果
     */
    public int insertBusiHttpPerf(BusiHttpPerf busiHttpPerf);

    /**
     * 修改http接口审计
     *
     * @param busiHttpPerf http接口审计
     * @return 结果
     */
    public int updateBusiHttpPerf(BusiHttpPerf busiHttpPerf);

    /**
     * 批量删除http接口审计
     *
     * @param ids 需要删除的http接口审计ID
     * @return 结果
     */
    public int deleteBusiHttpPerfByIds(Long[] ids);

    /**
     * 删除http接口审计信息
     *
     * @param id http接口审计ID
     * @return 结果
     */
    public int deleteBusiHttpPerfById(Long id);

}
