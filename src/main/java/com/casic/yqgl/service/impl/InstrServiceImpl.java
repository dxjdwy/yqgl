package com.casic.yqgl.service.impl;

import com.casic.yqgl.mapper.InstrumentMapper;
import com.casic.yqgl.model.Instrument;
import com.casic.yqgl.service.InstrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class InstrServiceImpl implements InstrService {

    @Autowired
    private InstrumentMapper instrumentMapper;

    @Override
    public Integer getTotal() {
        return instrumentMapper.getTotal();
    }

    @Override
    public List<Instrument> getInstrListPage(Instrument instrument) {
        return instrumentMapper.getInstrListPage(instrument);
    }

    @Override
    public List<Instrument> getInstrList() {
        return instrumentMapper.getInstrList();
    }
}
