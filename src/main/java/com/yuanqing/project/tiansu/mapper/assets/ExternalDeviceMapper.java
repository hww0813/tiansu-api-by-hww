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
     * 批量删除外部设备表
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteBusiExternalDeviceByIds(Long[] ids);

    void batchInsert(List<ExternalDevice> list);

    void batchUpdate(List<ExternalDevice> list);
}
