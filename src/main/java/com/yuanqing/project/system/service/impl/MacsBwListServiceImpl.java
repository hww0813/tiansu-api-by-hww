package com.yuanqing.project.system.service.impl;

import java.util.List;

import com.yuanqing.common.utils.DateUtils;
import com.yuanqing.project.system.domain.MacsBwList;
import com.yuanqing.project.system.mapper.MacsBwListMapper;
import com.yuanqing.project.system.service.IMacsBwListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 黑白名单Service业务层处理
 *
 * @author xucan
 * @date 2020-10-21
 */
@Service
public class MacsBwListServiceImpl implements IMacsBwListService
{
    @Autowired
    private MacsBwListMapper macsBwListMapper;

    /**
     * 查询黑白名单
     *
     * @param id 黑白名单ID
     * @return 黑白名单
     */
    @Override
    public MacsBwList selectMacsBwListById(Long id)
    {
        return macsBwListMapper.selectMacsBwListById(id);
    }

    /**
     * 查询黑白名单列表
     *
     * @param macsBwList 黑白名单
     * @return 黑白名单
     */
    @Override
    public List<MacsBwList> selectMacsBwListList(MacsBwList macsBwList)
    {
        return macsBwListMapper.selectMacsBwListList(macsBwList);
    }

    /**
     * 新增黑白名单
     *
     * @param macsBwList 黑白名单
     * @return 结果
     */
    @Override
    public int insertMacsBwList(MacsBwList macsBwList)
    {
        return macsBwListMapper.insertMacsBwList(macsBwList);
    }

    /**
     * 修改黑白名单
     *
     * @param macsBwList 黑白名单
     * @return 结果
     */
    @Override
    public int updateMacsBwList(MacsBwList macsBwList)
    {
        macsBwList.setUpdateTime(DateUtils.getNowDate());
        return macsBwListMapper.updateMacsBwList(macsBwList);
    }

    /**
     * 批量删除黑白名单
     *
     * @param ids 需要删除的黑白名单ID
     * @return 结果
     */
    @Override
    public int deleteMacsBwListByIds(Long[] ids)
    {
        return macsBwListMapper.deleteMacsBwListByIds(ids);
    }

    /**
     * 删除黑白名单信息
     *
     * @param id 黑白名单ID
     * @return 结果
     */
    @Override
    public int deleteMacsBwListById(Long id)
    {
        return macsBwListMapper.deleteMacsBwListById(id);
    }
}
