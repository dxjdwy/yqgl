package com.casic.yqgl.service;

import com.casic.yqgl.model.Instrument;

import java.util.List;

public interface InstrService {
   List<Instrument> getInstrList();


    List<Instrument> getInstrListPage(Instrument instrument);

    Integer getTotal();

    Integer insert(Instrument instrument);

    Integer delete(String instrId);

    Integer update(Instrument instrument);

    Integer saveOrUpdate(Instrument instrument);

    Instrument getInstrByCollectorId(Integer collectorId);
}
