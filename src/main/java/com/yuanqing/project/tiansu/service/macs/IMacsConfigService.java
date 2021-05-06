package com.yuanqing.project.tiansu.service.macs;

import com.yuanqing.framework.web.service.BaseService;
import com.yuanqing.project.tiansu.domain.macs.MacsConfig;
import com.yuanqing.project.tiansu.domain.macs.MacsRegion;

import java.util.List;

/**
 * @author xucan
 *
 */
public interface IMacsConfigService extends BaseService<MacsConfig, Long>{

    /**
     * 根据类型和名称查询系统配置
     * @param macsConfig
     * @return
     */
    List<MacsConfig> selectMacsConfigByTypeAndName(MacsConfig macsConfig);

    /**
     * 根据区域ID获取下级区域信息
     * @param id
     * @return
     */
    List<MacsRegion> getLowerRegion(String id);

    /**
     * 根据区域ID获取区域详情
     * @param id
     * @return
     */
    MacsRegion selectMacsRegionInfo(String id);


    /**
     * 获取区域信息
     * @param cityCode
     * @return
     */
    MacsRegion getRegion(String cityCode);

    /**
     * 获取地区列表
     * @return
     */
    List<MacsRegion> getRegionList();

    /**
     * 获取所有本级及下级市区的regionId
     * @param regionId
     * @return 返回regionId的list
     */
    List<String> getAllLowerRegion(String regionId);
}
