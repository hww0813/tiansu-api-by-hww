package com.yuanqing.project.tiansu.service.operation.impl;

import com.alibaba.fastjson.JSONObject;
import com.yuanqing.common.utils.DateUtils;
import com.yuanqing.project.tiansu.domain.operation.RawNetFlow;
import com.yuanqing.project.tiansu.mapper.operation.RawNetFlowMapper;
import com.yuanqing.project.tiansu.service.operation.IRawNetFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    public RawNetFlow selectBusiRawNetFlowById(Long id) {
        return busiRawNetFlowMapper.selectBusiRawNetFlowById(id);
    }

    /**
     * 查询原始流量列表
     *
     * @param busiRawNetFlow 原始流量
     * @return 原始流量
     */
    @Override
    public List<RawNetFlow> selectBusiRawNetFlowList(RawNetFlow busiRawNetFlow) {
        return busiRawNetFlowMapper.selectBusiRawNetFlowList(busiRawNetFlow);
    }

    /**
     * 新增原始流量
     *
     * @param busiRawNetFlow 原始流量
     * @return 结果
     */
    @Override
    public int insertBusiRawNetFlow(RawNetFlow busiRawNetFlow) {
        return busiRawNetFlowMapper.insertBusiRawNetFlow(busiRawNetFlow);
    }

    /**
     * 修改原始流量
     *
     * @param busiRawNetFlow 原始流量
     * @return 结果
     */
    @Override
    public int updateBusiRawNetFlow(RawNetFlow busiRawNetFlow) {
        return busiRawNetFlowMapper.updateBusiRawNetFlow(busiRawNetFlow);
    }

    /**
     * 批量删除原始流量
     *
     * @param ids 需要删除的原始流量ID
     * @return 结果
     */
    @Override
    public int deleteBusiRawNetFlowByIds(Long[] ids) {
        return busiRawNetFlowMapper.deleteBusiRawNetFlowByIds(ids);
    }

    /**
     * 删除原始流量信息
     *
     * @param id 原始流量ID
     * @return 结果
     */
    @Override
    public int deleteBusiRawNetFlowById(Long id) {
        return busiRawNetFlowMapper.deleteBusiRawNetFlowById(id);
    }

    /**
     * 获取服务器原始流量趋势
     *
     * @return 结果
     */
    @Override
    public List<JSONObject> getServerFlowTrend(Long dstIp, Date startTime, Date endTime) throws ParseException {

        List<JSONObject> trendList = busiRawNetFlowMapper.getRawFlowTrend(dstIp, startTime, endTime);

        trendList = dealTrendList(trendList,startTime,endTime);

        return trendList;
    }

    @Override
    public List<JSONObject> getClientRawFlowTrend(Long srcIp, Date startTime, Date endTime) throws ParseException {
        List<JSONObject> trendList = busiRawNetFlowMapper.getClientRawFlowTrend(srcIp, startTime, endTime);

        trendList = dealTrendList(trendList,startTime,endTime);

        return trendList;
    }

    @Override
    public List<JSONObject> getServerFlowRelationClient(RawNetFlow rawNetFlow) {

        return busiRawNetFlowMapper.getServerFlowRelationClient(rawNetFlow);
    }

    public List<JSONObject> dealTrendList(List<JSONObject> trendList,Date startTime, Date endTime) throws ParseException {
        //获得两个时间段之内的所有日期小时
        String strDateFormat = "yyyy-MM-dd HH";
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
        List<String> hours = DateUtils.getHoursBetweenTwoDate(sdf.format(startTime), sdf.format(endTime));

        List<JSONObject> list = new ArrayList<>();

        //将没有的时间段补齐
        int j = 0, i = 0;
        while (j < trendList.size()) {
            String date = hours.get(i);
            String sqlDate = trendList.get(j).getString("Hour");
            if (date.equals(sqlDate)) {
                list.add(trendList.get(j++));
            } else {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("Hour", date);
                jsonObject.put("Size", "0");
                jsonObject.put("Count", "0");
                list.add(jsonObject);
            }
            i++;
        }

        while (i < hours.size()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Hour", hours.get(i));
            jsonObject.put("Size", "0");
            jsonObject.put("Count", "0");
            list.add(jsonObject);
            i++;
        }
        return list;
    }




}
