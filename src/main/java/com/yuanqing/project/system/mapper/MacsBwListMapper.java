package com.yuanqing.project.system.mapper;

import com.yuanqing.project.system.domain.MacsBwList;

import java.util.List;


/**
 * 黑白名单Mapper接口
 *
 * @author xucan
 * @date 2020-10-21
 */
public interface MacsBwListMapper
{
    /**
     * 查询黑白名单
     *
     * @param id 黑白名单ID
     * @return 黑白名单
     */
    public MacsBwList selectMacsBwListById(Long id);

    /**
     * 查询黑白名单列表
     *
     * @param macsBwList 黑白名单
     * @return 黑白名单集合
     */
    public List<MacsBwList> selectMacsBwListList(MacsBwList macsBwList);

    /**
     * 新增黑白名单
     *
     * @param macsBwList 黑白名单
     * @return 结果
     */
    public int insertMacsBwList(MacsBwList macsBwList);

    /**
     * 修改黑白名单
     *
     * @param macsBwList 黑白名单
     * @return 结果
     */
    public int updateMacsBwList(MacsBwList macsBwList);

    /**
     * 删除黑白名单
     *
     * @param id 黑白名单ID
     * @return 结果
     */
    public int deleteMacsBwListById(Long id);

    /**
     * 批量删除黑白名单
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteMacsBwListByIds(Long[] ids);
}
