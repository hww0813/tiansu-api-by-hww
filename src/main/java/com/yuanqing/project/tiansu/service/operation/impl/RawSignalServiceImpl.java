package com.yuanqing.project.tiansu.service.operation.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.fastjson.JSONObject;
import com.yuanqing.common.utils.SplitList;
import com.yuanqing.common.utils.StringUtils;
import com.yuanqing.common.utils.ip.IpUtils;
import com.yuanqing.framework.web.domain.PageResult;
import com.yuanqing.project.tiansu.domain.operation.RawSignal;
import com.yuanqing.project.tiansu.mapper.operation.RawSignalMapper;
import com.yuanqing.project.tiansu.service.operation.IRawSignalService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Arrays;
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
        CompletableFuture<Integer> totalFuter = CompletableFuture.supplyAsync(() -> rawSignalMapper.queryCount(rawSignal));
        CompletableFuture<List<RawSignal>> operationBehaviorsFuture = CompletableFuture.supplyAsync(() -> rawSignalMapper.queryListAll(rawSignal));
        return PageResult.success(operationBehaviorsFuture.get(), rawSignal.getSize(), rawSignal.getNum(), totalFuter.get());
    }

    @Override
    public List<JSONObject> getAllToReport(RawSignal condRawSignal) {
        condRawSignal.setNum(0);
        Integer count = rawSignalMapper.queryCount(condRawSignal);
        if (count >= 50000) {
            condRawSignal.setSize(50000);
        } else {
            condRawSignal.setSize(count);
        }

        List<JSONObject> rawSignalList = rawSignalMapper.queryList(condRawSignal);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        rawSignalList.stream().forEach(jsonObject -> {
            jsonObject.put("srcIp", IpUtils.longToIPv4(jsonObject.getLong("srcIp")));
            jsonObject.put("dstIp", IpUtils.longToIPv4(jsonObject.getLong("dstIp")));
            jsonObject.put("stamp", formatter.format(jsonObject.getTimestamp("stamp")));
        });

        return rawSignalList;
    }


    @Override
    public void getListToReport(HttpServletResponse response, RawSignal condRawSignal) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"); // .xlsx
        response.setCharacterEncoding("utf8");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("原始信令报表.xlsx", "utf-8"));

        //将数据写入sheet页中
        ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).build();


        List<JSONObject> someReport = new ArrayList<>();

        Integer count = rawSignalMapper.queryCount(condRawSignal);

        Integer num = 50000;//分页条数
        Boolean flag = true;
        int start = 0;//起始序号
        int k = 1;//用于sheet角标

        while (flag) {
            if ((start + num) >= count) {
                condRawSignal.setNum(start);
                condRawSignal.setSize(count);
                List<JSONObject> rawList = rawSignalMapper.queryList(condRawSignal);
                someReport = getSomeReport(rawList);
                k = transfer(excelWriter, someReport, k);
                excelWriter.finish();
                response.flushBuffer();
                flag = false;
            } else {
                condRawSignal.setNum(start);
                condRawSignal.setSize(num);
                List<JSONObject> rawList = rawSignalMapper.queryList(condRawSignal);
                start += num;
                someReport = getSomeReport(rawList);
                k = transfer(excelWriter, someReport, k);
            }
        }

    }

    public int transfer(ExcelWriter excelWriter, List<JSONObject> list, int k) {
        List<List<JSONObject>> lists = SplitList.splitList(list, 10000); // 分割的集合
        for (int i = 1; i <= lists.size(); i++) {
            List<JSONObject> objectList = lists.get(i - 1);
            List<List<Object>> dataList = new ArrayList<>();
            List<Object> headList = new ArrayList<>(Arrays.asList(new Object[]{"源IP", "目的IP", "目的设备编码", "内容", "时间"}));
            dataList.add(headList);

            for (JSONObject j : objectList) {
                List<Object> row = new ArrayList();
                row.add(j.get("srcIp"));
                row.add(j.get("dstIp"));
                row.add(j.get("toCode"));
                row.add(j.get("content"));
                row.add(j.get("stamp"));
                dataList.add(row);
            }

            WriteSheet writeSheet = EasyExcel.writerSheet(k - 1, "sheet" + k).build();
            k++;
            excelWriter.write(dataList, writeSheet);
        }
        return k;
    }

    public List<JSONObject> getSomeReport(List<JSONObject> rawSignalList) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        rawSignalList.stream().forEach(jsonObject->{
            jsonObject.put("srcIp", IpUtils.longToIPv4(jsonObject.getLong("srcIp")));
            jsonObject.put("dstIp", IpUtils.longToIPv4(jsonObject.getLong("dstIp")));
            jsonObject.put("stamp", formatter.format(jsonObject.getTimestamp("stamp")));
        });
        return rawSignalList;
    }

}
