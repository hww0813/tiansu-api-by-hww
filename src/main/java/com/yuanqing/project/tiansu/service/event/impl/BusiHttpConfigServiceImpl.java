package com.yuanqing.project.tiansu.service.event.impl;


import com.yuanqing.common.utils.DateUtils;
import com.yuanqing.framework.redis.RedisCache;
import com.yuanqing.project.tiansu.domain.event.BusiHttpConfig;
import com.yuanqing.project.tiansu.mapper.event.BusiHttpConfigMapper;
import com.yuanqing.project.tiansu.service.event.IBusiHttpConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


/**
 * 接口告警阈值配置Service业务层处理
 *
 * @author lvjingjing
 * @date 2021-05-12
 */
@Service
public class BusiHttpConfigServiceImpl implements IBusiHttpConfigService
{
    @Autowired
    private BusiHttpConfigMapper busiHttpConfigMapper;
    @Resource
    private RedisCache redisCache;

    /**
     * 查询接口告警阈值配置列表
     *
     * @param busiHttpConfig 接口告警阈值配置
     * @return 接口告警阈值配置
     */
    @Override
    public List<BusiHttpConfig> selectBusiHttpConfigList(BusiHttpConfig busiHttpConfig)
    {
        return busiHttpConfigMapper.selectBusiHttpConfigList(busiHttpConfig);
    }

    /**
     * 修改接口告警阈值配置
     *
     * @param busiHttpConfig 接口告警阈值配置
     * @return 结果
     */
    @Override
    public int updateBusiHttpConfig(BusiHttpConfig busiHttpConfig)
    {
        busiHttpConfig.setUpdateTime(DateUtils.getNowDate());
        int i = busiHttpConfigMapper.updateBusiHttpConfig(busiHttpConfig);
        //修改数据后，更新缓存
        BusiHttpConfig httpConfig = new BusiHttpConfig();
        List<BusiHttpConfig> list = this.selectBusiHttpConfigList(httpConfig);
        redisCache.setCacheList("HTTP_CONFIG", list);
        return i;
    }

}
