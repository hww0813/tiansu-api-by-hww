package com.yuanqing.project.tiansu.service.event.impl;

import com.alibaba.fastjson.JSONObject;
import com.yuanqing.project.tiansu.domain.event.Event;
import com.yuanqing.project.tiansu.service.event.EventManager;
import com.yuanqing.project.tiansu.service.feign.AlarmFeignClient;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class EventManagerImpl implements EventManager {

//    @Value("${tiansu.alarmhost}")
//    private String prefix;

    private final String EVENT_UPDATE = "/BusiEvent/";
//    private final String EVENT_INFO = "/BusiEvent/getTInfo";

    @Resource
    private AlarmFeignClient alarmFeignClient;

    @Override
    public boolean batchChangStatus(List<Event> list) {
        List<Long> idList = list.stream().map(f->f.getId()).collect(Collectors.toList());
        Long []ids = new Long[list.size()];
        idList.toArray(ids);
        alarmFeignClient.confirm(ids);
//        RestTemplate restTemplate = new RestTemplate();
//        StringBuilder sb = new StringBuilder();
//        sb.append(prefix+EVENT_UPDATE);
//        list.stream().forEach(f -> {
//            sb.append(f.getId()+",");
//        });
//        restTemplate.put(sb.toString(),null);
        return true;
    }

    @Override
    public Event findById(Long id) {
        if (id == null) {
            throw new RuntimeException("id can't be null");
        }
//        String url = prefix + EVENT_INFO+"?event_id="+id;
//        String value = HttpUtils.sendGet(url, (String) null);
        String value = alarmFeignClient.detailAndTOperUuid(id);
        JSONObject jsonObject = (JSONObject) JSONObject.parse(value);
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        JSONObject data = (JSONObject) jsonObject.get("data");
        if(data.containsKey("startTime")){
            data.put("startTime",LocalDateTime.parse(data.getString("startTime"),df));
        }
        if (data.containsKey("endTime")) {
            data.put("endTime",LocalDateTime.parse(data.getString("endTime"),df));
        }
        Event event = data.toJavaObject(Event.class);
        return event;
    }


}
