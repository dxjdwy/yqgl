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
    public Integer insert(Instrument instrument) {
        return instrumentMapper.insert(instrument);
    }

    @Override
    public Integer delete(String instrId) {
        return instrumentMapper.deleteByPrimaryKey(instrId);
    }

    @Override
    public Integer saveOrUpdate(Instrument instrument) {
        return instrumentMapper.saveOrUpdate(instrument);
    }

    @Override
    public Integer update(Instrument instrument) {
        return instrumentMapper.updateByPrimaryKeySelective(instrument);
    }

    @Override
    public Instrument getInstrByCollectorId(Integer collectorId) {
        return instrumentMapper.getInstrByCollectorId(collectorId);
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
