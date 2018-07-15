package com.casic.yqgl.service.impl;

import com.casic.yqgl.mapper.EventMapper;
import com.casic.yqgl.model.Event;
import com.casic.yqgl.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class EventServiceImpl implements EventService {
    @Autowired
    private EventMapper eventMapper;

    @Override
    public int getTimeById(Integer collectorId) {
        return eventMapper.getTimeById(collectorId);
    }

    @Override
    public int getTimeByIdAndDay(Map map) {
        return eventMapper.getTimeByIdAndDay(map);
    }

    @Override
    public int getTimeByIdAndMonth(Map map) {
        return eventMapper.getTimeByIdAndMonth(map);
    }

    @Override
    public Integer saveOrUpdate(Event event) {
        return eventMapper.saveOrUpdate(event);
    }
}
