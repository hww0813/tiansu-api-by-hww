package com.yuanqing.project.tiansu.service.assets.impl;

import java.util.List;

import com.yuanqing.common.exception.CustomException;
import com.yuanqing.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yuanqing.project.tiansu.mapper.assets.BusiServerRemarkMapper;
import com.yuanqing.project.tiansu.domain.assets.BusiServerRemark;
import com.yuanqing.project.tiansu.service.assets.IBusiServerRemarkService;

/**
 * 服务标注Service业务层处理
 *
 * @author xucan
 * @date 2021-05-18
 */
@Service
public class BusiServerRemarkServiceImpl implements IBusiServerRemarkService
{
    @Autowired
    private BusiServerRemarkMapper busiServerRemarkMapper;

    /**
     * 查询服务标注
     *
     * @param id 服务标注ID
     * @return 服务标注
     */
    @Override
    public BusiServerRemark selectBusiServerRemarkById(Long id)
    {
        return busiServerRemarkMapper.selectBusiServerRemarkById(id);
    }

    @Override
    public BusiServerRemark selectBusiServerRemarkByName(String name) {
        return busiServerRemarkMapper.selectBusiServerRemarkByName(name);
    }

    /**
     * 查询服务标注列表
     *
     * @param busiServerRemark 服务标注
     * @return 服务标注
     */
    @Override
    public List<BusiServerRemark> selectBusiServerRemarkList(BusiServerRemark busiServerRemark)
    {
        return busiServerRemarkMapper.selectBusiServerRemarkList(busiServerRemark);
    }

    /**
     * 新增服务标注
     *
     * @param busiServerRemark 服务标注
     * @return 结果
     */
    @Override
    public int insertBusiServerRemark(BusiServerRemark busiServerRemark)
    {
        busiServerRemark.setCreateTime(DateUtils.getNowDate());
        if(busiServerRemarkMapper.selectBusiServerRemarkByName(busiServerRemark.getServerName())!=null){
            throw new CustomException("服务标注名称已经存在");
        }
        return busiServerRemarkMapper.insertBusiServerRemark(busiServerRemark);
    }

    /**
     * 修改服务标注
     *
     * @param busiServerRemark 服务标注
     * @return 结果
     */
    @Override
    public int updateBusiServerRemark(BusiServerRemark busiServerRemark)
    {
        busiServerRemark.setUpdateTime(DateUtils.getNowDate());
        if(busiServerRemarkMapper.selectBusiServerRemarkByName(busiServerRemark.getServerName())!=null){
            throw new CustomException("服务标注名称已经存在");
        }
        return busiServerRemarkMapper.updateBusiServerRemark(busiServerRemark);
    }

    /**
     * 批量删除服务标注
     *
     * @param ids 需要删除的服务标注ID
     * @return 结果
     */
    @Override
    public int deleteBusiServerRemarkByIds(Long[] ids)
    {
        return busiServerRemarkMapper.deleteBusiServerRemarkByIds(ids);
    }

    /**
     * 删除服务标注信息
     *
     * @param id 服务标注ID
     * @return 结果
     */
    @Override
    public int deleteBusiServerRemarkById(Long id)
    {
        return busiServerRemarkMapper.deleteBusiServerRemarkById(id);
    }
}
