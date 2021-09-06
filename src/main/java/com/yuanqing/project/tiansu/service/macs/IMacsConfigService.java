package com.yuanqing.project.tiansu.service.macs;

import com.alibaba.fastjson.JSONObject;
import com.yuanqing.common.exception.config.ConfigFileException;
import com.yuanqing.framework.web.domain.AjaxResult;
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
     * @throws ConfigFileException 配置文件相关异常
     */
    List<MacsConfig> selectMacsConfigByTypeAndName(MacsConfig macsConfig) throws ConfigFileException;

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


    /**
     * 匹配下级所在地区
     * @param list
     * @return
     */
    void setLowerRegionByCamera(List<Camera> list);


    /**
     * 查询系统配置表
     *
     * @param id 系统配置表ID
     * @return 系统配置表
     */
    public AjaxResult selectMacsConfigById(Long id);


    /**
     * 查询系统配置表列表
     *
     * @param macsConfig 系统配置表
     * @return 系统配置表集合
     */
    public JSONObject selectMacsConfigList(MacsConfig macsConfig);

    /**
     * 新增系统配置表
     *
     * @param macsConfig 系统配置表
     * @return 结果
     */
    public AjaxResult insertMacsConfig(MacsConfig macsConfig);

    /**
     * 修改系统配置表
     *
     * @param macsConfig 系统配置表
     * @return 结果
     */
    public AjaxResult updateMacsConfig(MacsConfig macsConfig);

    /**
     * 批量删除系统配置表
     *
     * @param ids 需要删除的系统配置表ID
     * @return 结果
     */
    public AjaxResult deleteMacsConfigByIds(Long[] ids);

    /**
     * 查询系统配置表
     *
     * @param type 系统配置表类型
     * @param name 系统配置表名称
     * @return 系统配置表
     */
//    public MacsConfig selectMacsConfigByTypeAndName(String type,String name);

    public AjaxResult operate(String instance, String operation, String serverName);

}
