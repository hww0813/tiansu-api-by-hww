package com.yuanqing.project.system.service;

import com.yuanqing.framework.web.domain.AjaxResult;
import com.yuanqing.project.system.domain.MacsBwList;

import java.util.List;


/**
 * 黑白名单Service接口
 *
 * @author xucan
 * @date 2020-10-21
 */
public interface IMacsBwListService
{
    /**
     * 查询黑白名单
     *
     * @param id 黑白名单ID
     * @return 黑白名单
     */
    public AjaxResult selectMacsBwListById(Long id);

    /**
     * 查询黑白名单列表
     *
     * @param macsBwList 黑白名单
     * @return 黑白名单集合
     */
    public List<MacsBwList> selectMacsBwListList(MacsBwList macsBwList, Integer pageNum,Integer pageSize);

    /**
     * 新增黑白名单
     *
     * @param macsBwList 黑白名单
     * @return 结果
     */
    public AjaxResult insertMacsBwList(MacsBwList macsBwList);

    /**
     * 修改黑白名单
     *
     * @param macsBwList 黑白名单
     * @return 结果
     */
    public AjaxResult updateMacsBwList(MacsBwList macsBwList);

    /**
     * 批量删除黑白名单
     *
     * @param ids 需要删除的黑白名单ID
     * @return 结果
     */
    public AjaxResult deleteMacsBwListByIds(Long[] ids);

    /**
     * 删除黑白名单信息
     *
     * @param id 黑白名单ID
     * @return 结果
     */
//    public int deleteMacsBwListById(Long id);
}
