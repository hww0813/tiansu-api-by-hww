package com.yuanqing.project.tiansu.service.event.impl;

import com.alibaba.fastjson.JSONObject;
import com.yuanqing.common.utils.http.HttpUtils;
import com.yuanqing.project.tiansu.domain.event.Event;
import com.yuanqing.project.tiansu.service.event.EventManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Service
public class EventManagerImpl implements EventManager {

    @Value("${tiansu.alarmhost}")
    private String prefix;

    private final String EVENT_UPDATE = "/BusiEvent/";
    private final String EVENT_INFO = "/BusiEvent/getTInfo";

    @Override
    public boolean batchChangStatus(List<Event> list) {
        RestTemplate restTemplate = new RestTemplate();
        StringBuilder sb = new StringBuilder();
        sb.append(prefix+EVENT_UPDATE);
        list.stream().forEach(f -> {
            sb.append(f.getId()+",");
        });
        restTemplate.put(sb.toString(),null);
        return true;
    }

    @Override
    public Event findById(Long id) {
        if (id == null) {
            throw new RuntimeException("id can't be null");
        }
        String url = prefix + EVENT_INFO+"?event_id="+id;
        String value = HttpUtils.sendGet(url, (String) null);
        JSONObject jsonObject = (JSONObject) JSONObject.parse(value);
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        JSONObject data = (JSONObject) jsonObject.get("data");
        if(data.containsKey("startTime")){
            data.put("startTime",LocalDateTime.parse(data.getString("startTime"),df));
        }
        Event event = data.toJavaObject(Event.class);
        return event;
    }

}
