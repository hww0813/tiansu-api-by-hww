package com.yuanqing.project.tiansu.mapper.assets;

import com.yuanqing.framework.web.mapper.BaseMapper;
import com.yuanqing.project.tiansu.domain.assets.ExternalDevice;

import java.util.List;

/**
 * 外部设备表Mapper接口
 *
 * @author cq
 * @date 2021-02-24
 */
public interface ExternalDeviceMapper extends BaseMapper<ExternalDevice,Long> {
    /**
     * 查询外部设备表
     *
     * @param id 外部设备表ID
     * @return 外部设备表
     */
    @Override
    ExternalDevice findById(Long id);

    /**
     * 查询外部设备表列表
     *
     * @param ExternalDevice 外部设备表
     * @return 外部设备表集合
     */
    @Override
    List<ExternalDevice> getList(ExternalDevice ExternalDevice);

    /**
     * 新增外部设备表
     *
     * @param ExternalDevice 外部设备表
     * @return 结果
     */
    @Override
    Long insert(ExternalDevice ExternalDevice);

    /**
     * 修改外部设备表
     *
     * @param ExternalDevice 外部设备表
     * @return 结果
     */
    @Override
    Long update(ExternalDevice ExternalDevice);

    /**
     * 删除外部设备表
     *
     * @param id 外部设备表ID
     * @return 结果
     */
    @Override
    void delete(Long id);

    /**
     * 批量删除外部设备表
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteBusiExternalDeviceByIds(Long[] ids);

    void batchInsert(List<ExternalDevice> list);

    void batchUpdate(List<ExternalDevice> list);
}
