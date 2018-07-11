package com.casic.yqgl.controller;

import com.casic.yqgl.model.Instrument;
import com.casic.yqgl.service.InstrService;
import com.casic.yqgl.uitls.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class instrListController {
    @Autowired
    private InstrService instrService;

    @RequestMapping("monitor")
    public String monitor(Model model){

        return "monitor";
    }

    @RequestMapping("getInstrListPage")
    @ResponseBody
    public PageHelper<Instrument> getInstrListPage(Instrument instrument, HttpServletRequest request){
        PageHelper<Instrument> pageHelper = new PageHelper<Instrument>();
        Integer total = instrService.getTotal();
        List<Instrument> instrListPage = instrService.getInstrListPage(instrument);
        pageHelper.setTotal(total);
        pageHelper.setRows(instrListPage);

        return pageHelper;
    }
}
