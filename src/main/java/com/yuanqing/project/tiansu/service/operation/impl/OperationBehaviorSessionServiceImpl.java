package com.yuanqing.project.tiansu.service.operation.impl;

import com.alibaba.fastjson.JSONObject;
import com.yuanqing.common.utils.DateUtils;
import com.yuanqing.common.utils.StringUtils;
import com.yuanqing.common.utils.ip.IpUtils;
import com.yuanqing.framework.web.domain.PageResult;
import com.yuanqing.project.tiansu.domain.operation.OperationBehaviorSession;
import com.yuanqing.project.tiansu.mapper.operation.OperationBehaviorSessionMapper;
import com.yuanqing.project.tiansu.service.operation.IOperationBehaviorSessionService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author Dong.Chao
 * @Classname OperationBehaviorSessionImpl
 * @Description
 * @Date 2021/2/25 15:25
 * @Version V1.0
 */
@Service
public class OperationBehaviorSessionServiceImpl implements IOperationBehaviorSessionService {

    @Resource
    private OperationBehaviorSessionMapper operationBehaviorSessionMapper;


    @Override
    public PageResult getList(OperationBehaviorSession operationBehaviorSession) throws ExecutionException, InterruptedException {

        //总数据
        CompletableFuture<Integer> totalFuter = CompletableFuture.supplyAsync(() ->  operationBehaviorSessionMapper.getCount(operationBehaviorSession));
        //操作行为列表
        CompletableFuture<List<OperationBehaviorSession>> operationBehaviorsFuture = CompletableFuture.supplyAsync(() -> operationBehaviorSessionMapper.getList(operationBehaviorSession));

        return  PageResult.success(operationBehaviorsFuture.get(),operationBehaviorSession.getSize(),operationBehaviorSession.getNum(),totalFuter.get());

    }

    @Override
    public List<JSONObject> getAllToReport(OperationBehaviorSession session) {
        Integer count = operationBehaviorSessionMapper.getCount(session);
        session.setNum(0);
        session.setSize(count);
        List<OperationBehaviorSession> operationBehaviorSessionList = operationBehaviorSessionMapper.getList(session);
        List<JSONObject> reportList = new ArrayList<JSONObject>();
        if(!CollectionUtils.isEmpty(operationBehaviorSessionList)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            for (OperationBehaviorSession operationBehaviorSession : operationBehaviorSessionList) {
                JSONObject jsonObject = new JSONObject();

                if (operationBehaviorSession.getSrcIp() != null) {
                    jsonObject.put("srcIp", IpUtils.longToIPv4(operationBehaviorSession.getSrcIp()));
                } else {
                    jsonObject.put("srcIp", "");
                }
                if (StringUtils.isNotEmpty(operationBehaviorSession.getUsername())) {
                    jsonObject.put("username", operationBehaviorSession.getUsername());
                }else {
                    jsonObject.put("username", "");
                }
                if (operationBehaviorSession.getStime() != null) {
                    jsonObject.put("startTime", DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, operationBehaviorSession.getStime()));
                } else {
                    jsonObject.put("startTime", "");
                }
                if (operationBehaviorSession.getActiveTime() != null) {
                    jsonObject.put("activeTime", DateUtils.getStrFromLocalDateTime(operationBehaviorSession.getActiveTime()));
                } else {
                    jsonObject.put("activeTime", "");
                }

                reportList.add(jsonObject);
            }
        }
        return reportList;
    }

    @Override
    public Map<String, Integer> getBehaviorCategory(JSONObject filters) {
        List<JSONObject> list=operationBehaviorSessionMapper.getBehaviorCategory(filters);
        Map<String, Integer> map=countBehavior(list);
        return map;
    }

    /**
     * 统计不同操作行为的数量
     *
     * @param list
     * @return
     */
    private Map<String, Integer> countBehavior(List<JSONObject> list) {

        Map<String, Integer> group = new HashMap<>();

        int play = 0, download = 0, control = 0,playback = 0,other = 0;

        for (JSONObject jsonObject : list) {
            int action = Integer.parseInt(jsonObject.get("action").toString());
            switch (action){
                case 0:
                    play = Integer.parseInt(jsonObject.get("cnt").toString());
                    break;
                case 1:
                    download = Integer.parseInt(jsonObject.get("cnt").toString());
                    break;
                case 2:
                    playback = Integer.parseInt(jsonObject.get("cnt").toString());
                    break;
                case 3:
                    control = Integer.parseInt(jsonObject.get("cnt").toString());
                    break;
                case 6:
                    other = Integer.parseInt(jsonObject.get("cnt").toString());
                    break;
            }
        }
        group.put("play", play);
        group.put("download", download);
        group.put("playback", playback);
        group.put("control", control);
        group.put("other", other);
        return group;
    }

}
