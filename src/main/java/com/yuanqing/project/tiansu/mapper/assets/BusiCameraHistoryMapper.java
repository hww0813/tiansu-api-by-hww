package com.yuanqing.project.tiansu.mapper.assets;

import com.yuanqing.project.tiansu.domain.assets.BusiCameraHistory;

import java.util.List;

/**
 * 摄像头历史Mapper接口
 * 
 * @author cq
 * @date 2021-02-26
 */
public interface BusiCameraHistoryMapper 
{
    /**
     * 查询摄像头历史
     * 
     * @param id 摄像头历史ID
     * @return 摄像头历史
     */
    public BusiCameraHistory selectBusiCameraHistoryById(Long id);

    /**
     * 查询摄像头历史列表
     * 
     * @param busiCameraHistory 摄像头历史
     * @return 摄像头历史集合
     */
    public List<BusiCameraHistory> selectBusiCameraHistoryList(BusiCameraHistory busiCameraHistory);

    /**
     * 新增摄像头历史
     * 
     * @param busiCameraHistory 摄像头历史
     * @return 结果
     */
    public int insertBusiCameraHistory(BusiCameraHistory busiCameraHistory);

    /**
     * 修改摄像头历史
     * 
     * @param busiCameraHistory 摄像头历史
     * @return 结果
     */
    public int updateBusiCameraHistory(BusiCameraHistory busiCameraHistory);

    /**
     * 删除摄像头历史
     * 
     * @param id 摄像头历史ID
     * @return 结果
     */
    public int deleteBusiCameraHistoryById(Long id);

    /**
     * 批量删除摄像头历史
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteBusiCameraHistoryByIds(Long[] ids);

    /**
     * 根据编码查询摄像头历史
     *
     * @param deviceCode
     * @return 摄像头历史
     */
    public BusiCameraHistory selectBusiCameraHistoryByCode(String deviceCode);
}
