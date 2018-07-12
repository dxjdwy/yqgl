package com.casic.yqgl.service;

import com.casic.yqgl.model.Instrument;

import java.util.List;

public interface InstrService {
    public List<Instrument> getInstrList();

    public List<Instrument> getInstrListPage(Instrument instrument);


    Integer getTotal();
}
