package com.casic.yqgl.mapper;

import com.casic.yqgl.model.Event;

import java.util.Date;
import java.util.Map;

public interface EventMapper {
    int deleteByPrimaryKey(Integer eventId);

    int insert(Event record);

    int insertSelective(Event record);

    Event selectByPrimaryKey(Integer eventId);

    int updateByPrimaryKeySelective(Event record);

    int updateByPrimaryKey(Event record);

    int saveOrUpdate(Event event);

    int getTimeById(Integer collectorId);

    /**
     *
     * @param map  collectorId   day
     * @return
     */
    int getTimeByIdAndDay(Map map);

    /**
     *
     * @param map  collectorId   month
     * @return
     */
    int getTimeByIdAndMonth(Map map);

}