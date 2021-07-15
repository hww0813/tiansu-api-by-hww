package com.yuanqing.project.tiansu.service.macs;

import com.alibaba.fastjson.JSONObject;
import com.yuanqing.framework.web.service.BaseService;
import com.yuanqing.project.tiansu.domain.assets.Camera;
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

<<<<<<< Updated upstream

    /**
     * 匹配下级所在地区
     * @param list
     * @return
     */
    void setLowerRegionByCamera(List<Camera> list);





=======
    /**
     * 查询系统配置表
     *
     * @param id 系统配置表ID
     * @return 系统配置表
     */
    public MacsConfig selectMacsConfigById(Long id);


    /**
     * 查询系统配置表列表
     *
     * @param macsConfig 系统配置表
     * @return 系统配置表集合
     */
    public List<MacsConfig> selectMacsConfigList(MacsConfig macsConfig);

    /**
     * 新增系统配置表
     *
     * @param macsConfig 系统配置表
     * @return 结果
     */
    public int insertMacsConfig(MacsConfig macsConfig);

    /**
     * 修改系统配置表
     *
     * @param macsConfig 系统配置表
     * @return 结果
     */
    public int updateMacsConfig(MacsConfig macsConfig);

    /**
     * 批量删除系统配置表
     *
     * @param ids 需要删除的系统配置表ID
     * @return 结果
     */
    public int deleteMacsConfigByIds(Long[] ids);

    /**
     * 查询系统配置表
     *
     * @param type 系统配置表类型
     * @param name 系统配置表名称
     * @return 系统配置表
     */
    public MacsConfig selectMacsConfigByTypeAndName(String type,String name);
>>>>>>> Stashed changes
}
