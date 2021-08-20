package com.yuanqing.project.tiansu.service.operation.impl;

import com.alibaba.fastjson.JSONObject;
import com.yuanqing.common.utils.StringUtils;
import com.yuanqing.common.utils.ip.IpUtils;
import com.yuanqing.framework.web.domain.PageResult;
import com.yuanqing.project.tiansu.domain.operation.RawSignal;
import com.yuanqing.project.tiansu.mapper.operation.RawSignalMapper;
import com.yuanqing.project.tiansu.service.operation.IRawSignalService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author Dong.Chao
 * @Classname RawSignalServiceImpl
 * @Description
 * @Date 2021/2/25 17:14
 * @Version V1.0
 */
@Service
public class RawSignalServiceImpl implements IRawSignalService {

    @Resource
    private RawSignalMapper rawSignalMapper;

    @Override
    public PageResult queryRawSignals(RawSignal rawSignal) throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> totalFuter = CompletableFuture.supplyAsync(() ->  rawSignalMapper.queryCount(rawSignal));
        CompletableFuture<List<RawSignal>> operationBehaviorsFuture = CompletableFuture.supplyAsync(() -> rawSignalMapper.queryListAll(rawSignal));
        return  PageResult.success(operationBehaviorsFuture.get(),rawSignal.getSize(),rawSignal.getNum(),totalFuter.get());
    }

    @Override
    public List<JSONObject> getAllToReport(JSONObject filters) {
        RawSignal condRawSignal = new RawSignal();
        condRawSignal.setstartDate(filters.getString("startDate"));
        condRawSignal.setendDate(filters.getString("endDate"));
        condRawSignal.setSrcIp(IpUtils.ipToLong(filters.getString("srcIp")));
        condRawSignal.setDstIp(IpUtils.ipToLong(filters.getString("dstIp")));

        condRawSignal.setNum(0);
        Integer count = rawSignalMapper.queryCount(condRawSignal);
        condRawSignal.setSize(count);
//        if(count > 1000) {
//            condRawSignal.setSize(1000);
//        } else {
//            condRawSignal.setSize(count);
//        }

        List<JSONObject> reportList = new ArrayList<JSONObject>();
        List<RawSignal> rawSignalList = rawSignalMapper.queryListAll(condRawSignal);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for (RawSignal rawSignal : rawSignalList) {
            JSONObject jsonObject = new JSONObject();
            if (rawSignal.getSrcIp() != null) {
                jsonObject.put("srcIp", IpUtils.longToIPv4(rawSignal.getSrcIp()));
            } else {
                jsonObject.put("srcIp", "");
            }
            if (StringUtils.isNotEmpty(rawSignal.getFromCode())) {
                jsonObject.put("fromCode", rawSignal.getFromCode());
            } else {
                jsonObject.put("fromCode", "");
            }
            if (rawSignal.getDstIp() != null) {
                jsonObject.put("dstIp", IpUtils.longToIPv4(rawSignal.getDstIp()));
            } else {
                jsonObject.put("dstIp", "");
            }
            if (rawSignal.getToCode() != null) {
                jsonObject.put("toCode", rawSignal.getToCode());
            } else {
                jsonObject.put("toCode", "");
            }
            if (StringUtils.isNotEmpty(rawSignal.getContent())) {
                jsonObject.put("content", rawSignal.getContent());
            } else {
                jsonObject.put("content", "");
            }
            if (rawSignal.getStamp() != null) {
                jsonObject.put("stamp", formatter.format(rawSignal.getStamp()));
            } else {
                jsonObject.put("stamp", "");
            }
            reportList.add(jsonObject);
        }
        return reportList;
    }

}
