package com.yuanqing.project.tiansu.mapper.assets;

import com.yuanqing.project.tiansu.domain.assets.BusiExternalDevice;

import java.util.List;

/**
 * 外部设备表Mapper接口
 * 
 * @author cq
 * @date 2021-02-24
 */
public interface BusiExternalDeviceMapper 
{
    /**
     * 查询外部设备表
     * 
     * @param id 外部设备表ID
     * @return 外部设备表
     */
    public BusiExternalDevice selectBusiExternalDeviceById(Long id);

    /**
     * 查询外部设备表列表
     * 
     * @param busiExternalDevice 外部设备表
     * @return 外部设备表集合
     */
    public List<BusiExternalDevice> selectBusiExternalDeviceList(BusiExternalDevice busiExternalDevice);

    /**
     * 新增外部设备表
     * 
     * @param busiExternalDevice 外部设备表
     * @return 结果
     */
    public int insertBusiExternalDevice(BusiExternalDevice busiExternalDevice);

    /**
     * 修改外部设备表
     * 
     * @param busiExternalDevice 外部设备表
     * @return 结果
     */
    public int updateBusiExternalDevice(BusiExternalDevice busiExternalDevice);

    /**
     * 删除外部设备表
     * 
     * @param id 外部设备表ID
     * @return 结果
     */
    public int deleteBusiExternalDeviceById(Long id);

    /**
     * 批量删除外部设备表
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteBusiExternalDeviceByIds(Long[] ids);

    void batchInsert(List<BusiExternalDevice> list);

    void batchUpdate(List<BusiExternalDevice> list);
}
