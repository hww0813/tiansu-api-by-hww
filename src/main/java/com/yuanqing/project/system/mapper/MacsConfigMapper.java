package com.yuanqing.project.system.mapper;

import java.util.List;

import com.yuanqing.project.tiansu.domain.macs.MacsConfig;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 系统配置表Mapper接口
 *
 * @author xucan
 * @date 2020-10-21
 */
@Repository
public interface MacsConfigMapper
{
    /**
     * 查询系统配置表
     *
     * @param id 系统配置表ID
     * @return 系统配置表
     */
    public MacsConfig selectMacsConfigById(Long id);


    public List<MacsConfig> selectMacsConfigBytype(String type);

    /**
     * 查询系统配置表
     *
     * @param type 系统配置表类型
     * @param name 系统配置表名称
     * @return 系统配置表
     */
    public MacsConfig selectMacsConfigByTypeAndName(@Param("type") String type,
                                                    @Param("name") String name);

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
     * 删除系统配置表
     *
     * @param id 系统配置表ID
     * @return 结果
     */
    public int deleteMacsConfigById(Long id);

    /**
     * 批量删除系统配置表
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteMacsConfigByIds(Long[] ids);
}
