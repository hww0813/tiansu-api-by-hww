package com.yuanqing.project.tiansu.service.operation.impl;

import java.util.List;

import com.alibaba.fastjson.JSONObject;

import com.yuanqing.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yuanqing.project.tiansu.mapper.operation.BusiHttpPerfMapper;
import com.yuanqing.project.tiansu.domain.operation.BusiHttpPerf;
import com.yuanqing.project.tiansu.service.operation.IBusiHttpPerfService;

/**
 * http接口审计Service业务层处理
 *
 * @author xucan
 * @date 2021-05-17
 */
@Service
public class BusiHttpPerfServiceImpl implements IBusiHttpPerfService {
    @Autowired
    private BusiHttpPerfMapper busiHttpPerfMapper;

    /**
     * 查询http接口审计
     *
     * @param id http接口审计ID
     * @return http接口审计
     */
    @Override
    public BusiHttpPerf selectBusiHttpPerfById(Long id) {
        return busiHttpPerfMapper.selectBusiHttpPerfById(id);
    }

    /**
     * 查询http接口审计列表
     *
     * @param busiHttpPerf http接口审计
     * @return http接口审计
     */
    @Override
    public List<BusiHttpPerf> selectBusiHttpPerfList(BusiHttpPerf busiHttpPerf) {
        return busiHttpPerfMapper.selectBusiHttpPerfList(busiHttpPerf);
    }

    @Override
    public List<JSONObject> selctHttpPerfListGroupByDstHost(BusiHttpPerf busiHttpPerf) {
        return busiHttpPerfMapper.selctHttpPerfListGroupByDstHost(busiHttpPerf);
    }

    /**
     * 新增http接口审计
     *
     * @param busiHttpPerf http接口审计
     * @return 结果
     */
    @Override
    public int insertBusiHttpPerf(BusiHttpPerf busiHttpPerf) {
        return busiHttpPerfMapper.insertBusiHttpPerf(busiHttpPerf);
    }

    /**
     * 修改http接口审计
     *
     * @param busiHttpPerf http接口审计
     * @return 结果
     */
    @Override
    public int updateBusiHttpPerf(BusiHttpPerf busiHttpPerf) {
        busiHttpPerf.setUpdateTime(DateUtils.getNowDate());
        return busiHttpPerfMapper.updateBusiHttpPerf(busiHttpPerf);
    }

    /**
     * 批量删除http接口审计
     *
     * @param ids 需要删除的http接口审计ID
     * @return 结果
     */
    @Override
    public int deleteBusiHttpPerfByIds(Long[] ids) {
        return busiHttpPerfMapper.deleteBusiHttpPerfByIds(ids);
    }

    /**
     * 删除http接口审计信息
     *
     * @param id http接口审计ID
     * @return 结果
     */
    @Override
    public int deleteBusiHttpPerfById(Long id) {
        return busiHttpPerfMapper.deleteBusiHttpPerfById(id);
    }

}
