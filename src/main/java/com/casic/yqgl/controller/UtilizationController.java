package com.casic.yqgl.controller;

import com.casic.yqgl.model.Instrument;
import com.casic.yqgl.service.EventService;
import com.casic.yqgl.service.InstrService;
import com.casic.yqgl.uitls.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
public class UtilizationController {
    @Autowired
    private InstrService instrService;
    @Autowired
    private EventService eventService;
    @RequestMapping("utilization")
    public String utilization(Model model){

        return "utilization";
    }

    @RequestMapping("getUtilizationPage")
    @ResponseBody
    public PageHelper<Instrument> getUtilizationPage(Instrument instrument, HttpServletRequest request){
        PageHelper<Instrument> pageHelper = new PageHelper<Instrument>();
        Integer total = instrService.getTotal();
        List<Instrument> instrListPage = instrService.getInstrListPage(instrument);
        for (Instrument ins:instrListPage
             ) {
            int runTime = eventService.getTimeById(ins.getInstrCollectorId());
            ins.setInstrRuntime(runTime/60+"小时");
            if (ins.getInstrCreateDate() != null){
                int daysBetween = daysBetween(ins.getInstrCreateDate(),new Date())+1;
                ins.setInstrUtilization(runTime/60/daysBetween+"小时");
                ins.setInstrRes(runTime/60/daysBetween/21+"小时");
            }
        }
        pageHelper.setTotal(total);
        pageHelper.setRows(instrListPage);

        return pageHelper;
    }
    @RequestMapping("utilization/getTimeByMonth")
    @ResponseBody
    public List<Map> getTimeByMonth(String year,String instrId){
        if (instrId != null && instrId != "" && year != null && year != ""){
            Instrument instrument = instrService.getByinstrId(instrId);
            double[] timeArray = new double[12];
            Map map = new HashMap();
            map.put("year",year);
            map.put("collectorId",instrument.getInstrCollectorId());
            for (int i = 0; i < 12; i++) {
                map.put("month",(i+1));
                int res = eventService.getTimeByIdAndMonth(map);
                timeArray[i] = res/60;
            }
            List<Map> resMapLsit = new ArrayList<>();
            Map resMap = new HashMap();
            resMap.put("name",instrId);
            resMap.put("time",timeArray);
            resMapLsit.add(resMap);
            return resMapLsit;
        }

        return null;
    }
    /**
     * 计算两个日期的相隔天数
     * @author shijing
     * 2015年9月6日下午5:14:54
     * @param date1
     * @param date2
     * @return
     */
    public static int daysBetween(Date date1, Date date2){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date1);
        long time1 = cal.getTimeInMillis();
        cal.setTime(date2);
        long time2 = cal.getTimeInMillis();
        long between_days=(time2-time1)/(1000*3600*24);

        return Integer.parseInt(String.valueOf(between_days));
    }
}
