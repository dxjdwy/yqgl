package com.casic.yqgl.model;

import java.util.Date;

public class Event {
    private Integer eventId;

    private String instrIp;

    private Integer eventTimeCount;

    private Double eventV;

    private Double eventA;

    private Double eventW;

    private String eventStatus;

    private Date eventRealtime;

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    public String getInstrIp() {
        return instrIp;
    }

    public void setInstrIp(String instrIp) {
        this.instrIp = instrIp;
    }

    public Integer getEventTimeCount() {
        return eventTimeCount;
    }

    public void setEventTimeCount(Integer eventTimeCount) {
        this.eventTimeCount = eventTimeCount;
    }

    public Double getEventV() {
        return eventV;
    }

    public void setEventV(Double eventV) {
        this.eventV = eventV;
    }

    public Double getEventA() {
        return eventA;
    }

    public void setEventA(Double eventA) {
        this.eventA = eventA;
    }

    public Double getEventW() {
        return eventW;
    }

    public void setEventW(Double eventW) {
        this.eventW = eventW;
    }

    public String getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(String eventStatus) {
        this.eventStatus = eventStatus;
    }

    public Date getEventRealtime() {
        return eventRealtime;
    }

    public void setEventRealtime(Date eventRealtime) {
        this.eventRealtime = eventRealtime;
    }
}