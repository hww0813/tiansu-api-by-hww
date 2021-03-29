package com.yuanqing.project.tiansu.service.event;

import com.yuanqing.project.tiansu.domain.event.Event;

import java.util.List;

public interface EventManager{

    boolean batchChangStatus(List<Event> list);
}
