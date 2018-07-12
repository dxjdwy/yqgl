package com.casic.yqgl.service;

import com.casic.yqgl.model.Collector;

import java.util.List;

public interface CollectorService {
    public List<Collector> getCollectorListPage(Collector collector);

    Integer insert(Collector collector);

    Integer delete(Integer collectorId);

    Integer getTotal();
}
