package com.casic.yqgl.service;

import com.casic.yqgl.model.Event;

import java.util.Map;

public interface EventService {
    Integer saveOrUpdate(Event event);

    int getTimeById(Integer collectorId);

    int getTimeByIdAndDay(Map map);

    int getTimeByIdAndMonth(Map map);
}
