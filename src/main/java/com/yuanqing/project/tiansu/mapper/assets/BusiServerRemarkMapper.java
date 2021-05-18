package com.yuanqing.project.tiansu.mapper.assets;

import java.util.List;
import com.yuanqing.project.tiansu.domain.assets.BusiServerRemark;

/**
 * 服务标注Mapper接口
 *
 * @author xucan
 * @date 2021-05-18
 */
public interface BusiServerRemarkMapper
{
    /**
     * 查询服务标注
     *
     * @param id 服务标注ID
     * @return 服务标注
     */
    public BusiServerRemark selectBusiServerRemarkById(Long id);

    /**
     * 查询服务标注列表
     *
     * @param busiServerRemark 服务标注
     * @return 服务标注集合
     */
    public List<BusiServerRemark> selectBusiServerRemarkList(BusiServerRemark busiServerRemark);

    /**
     * 新增服务标注
     *
     * @param busiServerRemark 服务标注
     * @return 结果
     */
    public int insertBusiServerRemark(BusiServerRemark busiServerRemark);

    /**
     * 修改服务标注
     *
     * @param busiServerRemark 服务标注
     * @return 结果
     */
    public int updateBusiServerRemark(BusiServerRemark busiServerRemark);

    /**
     * 删除服务标注
     *
     * @param id 服务标注ID
     * @return 结果
     */
    public int deleteBusiServerRemarkById(Long id);

    /**
     * 批量删除服务标注
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteBusiServerRemarkByIds(Long[] ids);
}
