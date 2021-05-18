package com.yuanqing.project.tiansu.mapper.operation;

import java.util.Date;
import java.util.List;


import com.alibaba.fastjson.JSONObject;
import com.yuanqing.project.tiansu.domain.operation.BusiHttpPerf;
import org.springframework.stereotype.Repository;

/**
 * http接口审计Mapper接口
 *
 * @author xucan
 * @date 2021-05-17
 */
@Repository
public interface BusiHttpPerfMapper {
    /**
     * 查询http接口审计
     *
     * @param id http接口审计ID
     * @return http接口审计
     */
    public BusiHttpPerf selectBusiHttpPerfById(Long id);

    /**
     * 聚合查询 接口请求服务接口数量
     *
     * @param busiHttpPerf http接口审计
     * @return http接口审计集合
     */
    public List<JSONObject> selctHttpPerfListGroupByDstHost(BusiHttpPerf busiHttpPerf);

    /**
     * @param busiHttpPerf http接口审计
     * @return http接口审计集合
     */
    public List<BusiHttpPerf> selectBusiHttpPerfList(BusiHttpPerf busiHttpPerf);

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
     * 删除http接口审计
     *
     * @param id http接口审计ID
     * @return 结果
     */
    public int deleteBusiHttpPerfById(Long id);

    /**
     * 批量删除http接口审计
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteBusiHttpPerfByIds(Long[] ids);

    /**
     * 获取接口调用数
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 在该时间范围内的接口调用数
     */
    public Integer getHttpApiNum(Date startDate, Date endDate);

    /**
     * 获取接口错误数
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 在该时间范围内的接口错误数
     */
    public Integer getApiErrorNum(Date startDate, Date endDate);

    /**
     * 获取接口超时数
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 在该时间范围内的接口超时数
     */
    public Integer getApiOverTime(Date startDate, Date endDate, Double time);
}
