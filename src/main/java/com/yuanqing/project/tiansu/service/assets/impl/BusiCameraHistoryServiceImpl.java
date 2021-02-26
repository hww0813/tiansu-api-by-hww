package com.yuanqing.project.tiansu.service.assets.impl;

import com.yuanqing.common.utils.DateUtils;
import com.yuanqing.project.tiansu.domain.assets.BusiCameraHistory;
import com.yuanqing.project.tiansu.mapper.assets.BusiCameraHistoryMapper;
import com.yuanqing.project.tiansu.service.assets.IBusiCameraHistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 摄像头历史Service业务层处理
 *
 * @author cq
 * @date 2021-02-26
 */
@Service
@CacheConfig(cacheNames = "cameraHistory")
public class BusiCameraHistoryServiceImpl implements IBusiCameraHistoryService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(BusiCameraHistoryServiceImpl.class);

    @Autowired
    private BusiCameraHistoryMapper busiCameraHistoryMapper;

    /**
     * 查询摄像头历史
     *
     * @param id 摄像头历史ID
     * @return 摄像头历史
     */
    @Override
    public BusiCameraHistory selectBusiCameraHistoryById(Long id)
    {
        return busiCameraHistoryMapper.selectBusiCameraHistoryById(id);
    }

    /**
     * 查询摄像头历史列表
     *
     * @param busiCameraHistory 摄像头历史
     * @return 摄像头历史
     */
    @Override
    public List<BusiCameraHistory> selectBusiCameraHistoryList(BusiCameraHistory busiCameraHistory)
    {
        return busiCameraHistoryMapper.selectBusiCameraHistoryList(busiCameraHistory);
    }

    /**
     * 新增摄像头历史
     *
     * @param busiCameraHistory 摄像头历史
     * @return 结果
     */
    @Override
    public int insertBusiCameraHistory(BusiCameraHistory busiCameraHistory)
    {
        busiCameraHistory.setCreateTime(DateUtils.getNowDate());
        return busiCameraHistoryMapper.insertBusiCameraHistory(busiCameraHistory);
    }

    /**
     * 修改摄像头历史
     *
     * @param busiCameraHistory 摄像头历史
     * @return 结果
     */
    @Override
    public int updateBusiCameraHistory(BusiCameraHistory busiCameraHistory)
    {
        return busiCameraHistoryMapper.updateBusiCameraHistory(busiCameraHistory);
    }

    /**
     * 批量删除摄像头历史
     *
     * @param ids 需要删除的摄像头历史ID
     * @return 结果
     */
    @Override
    public int deleteBusiCameraHistoryByIds(Long[] ids)
    {
        return busiCameraHistoryMapper.deleteBusiCameraHistoryByIds(ids);
    }

    /**
     * 删除摄像头历史信息
     *
     * @param id 摄像头历史ID
     * @return 结果
     */
    @Override
    public int deleteBusiCameraHistoryById(Long id)
    {
        return busiCameraHistoryMapper.deleteBusiCameraHistoryById(id);
    }

    @Override
    @Cacheable(key = "#deviceCode")
    public BusiCameraHistory selectBusiCameraHistoryByCode(String deviceCode) {
        LOGGER.error("selectBusiCameraHistoryByCode ============== 没走缓存，请求数据库: " + deviceCode);
        return busiCameraHistoryMapper.selectBusiCameraHistoryByCode(deviceCode);
    }
}
