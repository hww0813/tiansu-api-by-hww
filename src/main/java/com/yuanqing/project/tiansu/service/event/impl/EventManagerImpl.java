package com.yuanqing.project.tiansu.service.event.impl;

import com.yuanqing.project.tiansu.domain.event.Event;
import com.yuanqing.project.tiansu.service.event.EventManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.web.client.RestTemplate;

import java.util.List;


@Service
public class EventManagerImpl implements EventManager {

    @Value("${tiansu.alarmhost}")
    private String prefix;


    private final String EVENT_UPDATE = "/BusiEvent/";

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



}
