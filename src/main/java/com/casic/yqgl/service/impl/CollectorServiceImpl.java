package com.casic.yqgl.service.impl;

import com.casic.yqgl.mapper.CollectorMapper;
import com.casic.yqgl.model.Collector;
import com.casic.yqgl.service.CollectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CollectorServiceImpl implements CollectorService {
    @Autowired
    private CollectorMapper collectorMapper;
    @Override
    public List<Collector> getCollectorListPage(Collector collector) {
        return collectorMapper.getCollectorListPage(collector);
    }

    @Override
    public Integer getTotal() {
        return collectorMapper.getTotal();
    }
}
