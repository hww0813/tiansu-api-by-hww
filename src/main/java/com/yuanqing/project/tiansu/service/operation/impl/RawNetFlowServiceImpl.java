package com.yuanqing.project.tiansu.service.operation.impl;

import com.alibaba.fastjson.JSONObject;
import com.yuanqing.project.tiansu.domain.operation.RawNetFlow;
import com.yuanqing.project.tiansu.mapper.operation.RawNetFlowMapper;
import com.yuanqing.project.tiansu.service.operation.IRawNetFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class RawNetFlowServiceImpl implements IRawNetFlowService {

    @Autowired
    private RawNetFlowMapper busiRawNetFlowMapper;

    /**
     * 查询原始流量
     *
     * @param id 原始流量ID
     * @return 原始流量
     */
    @Override
    public RawNetFlow selectBusiRawNetFlowById(Long id)
    {
        return busiRawNetFlowMapper.selectBusiRawNetFlowById(id);
    }

    /**
     * 查询原始流量列表
     *
     * @param busiRawNetFlow 原始流量
     * @return 原始流量
     */
    @Override
    public List<RawNetFlow> selectBusiRawNetFlowList(RawNetFlow busiRawNetFlow)
    {
        return busiRawNetFlowMapper.selectBusiRawNetFlowList(busiRawNetFlow);
    }

    /**
     * 新增原始流量
     *
     * @param busiRawNetFlow 原始流量
     * @return 结果
     */
    @Override
    public int insertBusiRawNetFlow(RawNetFlow busiRawNetFlow)
    {
        return busiRawNetFlowMapper.insertBusiRawNetFlow(busiRawNetFlow);
    }

    /**
     * 修改原始流量
     *
     * @param busiRawNetFlow 原始流量
     * @return 结果
     */
    @Override
    public int updateBusiRawNetFlow(RawNetFlow busiRawNetFlow)
    {
        return busiRawNetFlowMapper.updateBusiRawNetFlow(busiRawNetFlow);
    }

    /**
     * 批量删除原始流量
     * @param ids 需要删除的原始流量ID
     * @return 结果
     *
     */
    @Override
    public int deleteBusiRawNetFlowByIds(Long[] ids)
    {
        return busiRawNetFlowMapper.deleteBusiRawNetFlowByIds(ids);
    }

    /**
     * 删除原始流量信息
     *
     * @param id 原始流量ID
     * @return 结果
     */
    @Override
    public int deleteBusiRawNetFlowById(Long id)
    {
        return busiRawNetFlowMapper.deleteBusiRawNetFlowById(id);
    }


    /**
     * 获取服务器原始流量趋势
     *
     * @return 结果
     */
    public  List<JSONObject>getServerFlowTrend (RawNetFlow rawNetFlow){

        List<JSONObject> trendList = busiRawNetFlowMapper.getRawFlowTrend(rawNetFlow);
        List<String> hourList = new ArrayList<>();


        for(JSONObject json :trendList){
            hourList.add(json.getString("Hour"));
        }
        for(int i=0;i<24;i++) {
            if (hourList.contains(String.valueOf(i))) {

            } else {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("Hour", i);
                jsonObject.put("Size", "0");
                jsonObject.put("Count", "0");
                trendList.add(jsonObject);
            }
        }
        return  trendList;
    }

    public  List<JSONObject>getServerFlowRelationClient (RawNetFlow rawNetFlow){
        return  busiRawNetFlowMapper.getServerFlowRelationClient(rawNetFlow);
    }
}
