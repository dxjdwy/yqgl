package com.casic.yqgl.mapper;

import com.casic.yqgl.model.Instrument;

import java.util.List;


public interface InstrumentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Instrument record);

    int insertSelective(Instrument record);

    Instrument selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Instrument record);

    int updateByPrimaryKey(Instrument record);

    List<Instrument> getInstrList();

    List<Instrument> getInstrListPage(Instrument instrument);

    int getTotal();
}