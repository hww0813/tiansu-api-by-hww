package com.yuanqing.project.tiansu.service.assets;

import java.util.List;
import com.yuanqing.project.tiansu.domain.assets.BusiServerRemark;

/**
 * 服务标注Service接口
 *
 * @author xucan
 * @date 2021-05-18
 */
public interface IBusiServerRemarkService
{
    /**
     * 查询服务标注
     *
     * @param id 服务标注ID
     * @return 服务标注
     */
    public BusiServerRemark selectBusiServerRemarkById(Long id);


    public BusiServerRemark selectBusiServerRemarkByName(String name);

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
     * 批量删除服务标注
     *
     * @param ids 需要删除的服务标注ID
     * @return 结果
     */
    public int deleteBusiServerRemarkByIds(Long[] ids);

    /**
     * 删除服务标注信息
     *
     * @param id 服务标注ID
     * @return 结果
     */
    public int deleteBusiServerRemarkById(Long id);
}
