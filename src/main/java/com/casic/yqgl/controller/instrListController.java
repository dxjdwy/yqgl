package com.casic.yqgl.controller;

import com.casic.yqgl.model.Collector;
import com.casic.yqgl.model.Instrument;
import com.casic.yqgl.service.InstrService;
import com.casic.yqgl.uitls.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    @RequestMapping("instr/delete")
    public String instrDelete(String instrId,Model model) throws IOException {
        Integer res = instrService.delete(instrId);
        if (res == 1){
            model.addAttribute("status","success");
            return "monitor";
        }
        model.addAttribute("status","error");
        return "monitor";
    }
    @RequestMapping("instr/insert")
    public @ResponseBody String instrInsert(@ModelAttribute Instrument instrument, Model model){
        Integer res = instrService.insert(instrument);
        if (res == 1){
            model.addAttribute("status","success");
            return "monitor";
        }
        model.addAttribute("status","error");
        return "monitor";
    }
    @RequestMapping("instr/update")
    public @ResponseBody String instrUpdate(@ModelAttribute Instrument instrument, Model model){
        Integer res = instrService.update(instrument);
        if (res == 1){
            model.addAttribute("status","success");
            return "monitor";
        }
        model.addAttribute("status","error");
        return "monitor";
    }
    @RequestMapping("instr/saveOrUpdate")
    public @ResponseBody String saveOrUpdate(@ModelAttribute Instrument instrument, Model model){
        Integer res = instrService.saveOrUpdate(instrument);
        if (res == 1){
            model.addAttribute("status","success");
            return "monitor";
        }
        model.addAttribute("status","error");
        return "monitor";
    }

    @InitBinder//必须有一个参数WebDataBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));

        binder.registerCustomEditor(int.class, new PropertyEditorSupport() {

            @Override
            public String getAsText() {
                // TODO Auto-generated method stub
                return getValue().toString();
            }

            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                // TODO Auto-generated method stub

                setValue(Integer.parseInt(text));
            }

        });
    }
}
