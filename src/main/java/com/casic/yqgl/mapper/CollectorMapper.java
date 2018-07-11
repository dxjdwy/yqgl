package com.casic.yqgl.mapper;

import com.casic.yqgl.model.Collector;

import java.util.List;

public interface CollectorMapper {
    int deleteByPrimaryKey(Integer collectorId);

    int insert(Collector record);

    int insertSelective(Collector record);

    Collector selectByPrimaryKey(Integer collectorId);

    int updateByPrimaryKeySelective(Collector record);

    int updateByPrimaryKey(Collector record);

    List<Collector> getCollectorListPage(Collector collector);

    int getTotal();

}