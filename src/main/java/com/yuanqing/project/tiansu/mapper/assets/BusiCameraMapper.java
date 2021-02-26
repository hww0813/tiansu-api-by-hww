package com.yuanqing.project.tiansu.mapper.assets;

import com.yuanqing.project.tiansu.domain.assets.BusiCamera;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 摄像头Mapper接口
 * 
 * @author cq
 * @date 2021-02-25
 */
public interface BusiCameraMapper 
{
    /**
     * 查询摄像头
     * 
     * @param id 摄像头ID
     * @return 摄像头
     */
    public BusiCamera selectBusiCameraById(Long id);

    /**
     * 查询摄像头列表
     * 
     * @param busiCamera 摄像头
     * @return 摄像头集合
     */
    public List<BusiCamera> selectBusiCameraList(BusiCamera busiCamera);

    /**
     * 新增摄像头
     * 
     * @param busiCamera 摄像头
     * @return 结果
     */
    public int insertBusiCamera(BusiCamera busiCamera);

    /**
     * 修改摄像头
     * 
     * @param busiCamera 摄像头
     * @return 结果
     */
    public int updateBusiCamera(BusiCamera busiCamera);

    /**
     * 删除摄像头
     * 
     * @param id 摄像头ID
     * @return 结果
     */
    public int deleteBusiCameraById(Long id);

    /**
     * 批量删除摄像头
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteBusiCameraByIds(Long[] ids);

    /**
     * 批量插入
     *
     * @param list
     * @return
     */
    void batchInsert(@Param("list") List<BusiCamera> list);
}
