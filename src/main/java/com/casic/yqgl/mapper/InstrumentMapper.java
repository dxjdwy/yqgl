package com.casic.yqgl.mapper;

import com.casic.yqgl.model.Instrument;

import java.util.List;


public interface InstrumentMapper {
    int deleteByPrimaryKey(String id);

    int insert(Instrument record);

    int insertSelective(Instrument record);

    Instrument selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Instrument record);

    int updateByPrimaryKey(Instrument record);

    Instrument getInstrByCollectorId(Integer CollectorId);

    List<Instrument> getInstrList();

    List<Instrument> getInstrListPage(Instrument instrument);

    int saveOrUpdate(Instrument instrument);

    int getTotal();
}