package com.yuanqing.project.system.service.impl;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yuanqing.common.utils.DateUtils;
import com.yuanqing.common.utils.StringUtils;
import com.yuanqing.framework.web.domain.AjaxResult;
import com.yuanqing.project.system.domain.MacsBwList;
import com.yuanqing.project.system.service.IMacsBwListService;
import com.yuanqing.project.tiansu.service.feign.MacsFeignClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 * 黑白名单Service业务层处理
 *
 * @author xucan
 * @date 2020-10-21
 */
@Service
public class MacsBwListServiceImpl implements IMacsBwListService
{

    @Resource
    private MacsFeignClient macsFeignClient;

    public static final String MACS_TOKEN = "BearereyJhbGciOiJIUzUxMiJ9.eyJsb2dpbl91c2VyX2tleSI6ImUzNjQ4YmI0LWQyMWEtNDRmZi05Mj" +
            "c0LWJjMDcxMDBjMzgzOSJ9.Ix7KKtW5Q4UZCbKgK5roz0y7xv7z5a_tb37k8alIwuAG9uiga6R6dBuCDEsx8HWqlUnXTVqNxHRaeo_6RY_e-w";

    /**
     * 查询黑白名单
     *
     * @param id 黑白名单ID
     * @return 黑白名单
     */
    @Override
    public AjaxResult selectMacsBwListById(Long id)
    {

        return macsFeignClient.selectMacsBwListById(id, MACS_TOKEN);
    }

    /**
     * 查询黑白名单列表
     *
     * @param macsBwList 黑白名单
     * @return 黑白名单
     */
    @Override
    public List<MacsBwList> selectMacsBwListList(MacsBwList macsBwList, Integer pageNum,Integer pageSize)
    {
        String rspStr = macsFeignClient.getBlackWhiteList(macsBwList, pageSize, pageNum, MACS_TOKEN);
        if (StringUtils.isEmpty(rspStr))
        {
            return null;
        }
        JSONObject object = JSONObject.parseObject(rspStr);
        if (object != null) {
            int code = object.getInteger("code");
            if (200 == code) {
                List<MacsBwList> result = JSONArray.parseArray(object.getString("rows"), MacsBwList.class);
                return result;
            }
        }

        return null;
    }


    /**
     * 新增黑白名单
     *
     * @param macsBwList 黑白名单
     * @return 结果
     */
    @Override
    public AjaxResult insertMacsBwList(MacsBwList macsBwList)
    {

        return macsFeignClient.insertBlackWhite(macsBwList, MACS_TOKEN);
    }

    /**
     * 修改黑白名单
     *
     * @param macsBwList 黑白名单
     * @return 结果
     */
    @Override
    public AjaxResult updateMacsBwList(MacsBwList macsBwList)
    {
        return macsFeignClient.updateBlackWhite(macsBwList, MACS_TOKEN);
    }

    /**
     * 批量删除黑白名单
     *
     * @param ids 需要删除的黑白名单ID
     * @return 结果
     */
    @Override
    public AjaxResult deleteMacsBwListByIds(Long[] ids)
    {
        return macsFeignClient.deleteMacsBwListByIds(ids, MACS_TOKEN);
    }

    /**
     * 删除黑白名单信息
     *
     * @param id 黑白名单ID
     * @return 结果
     */
//    @Override
//    public int deleteMacsBwListById(Long id)
//    {
//        return macsBwListMapper.deleteMacsBwListById(id);
//    }
}
