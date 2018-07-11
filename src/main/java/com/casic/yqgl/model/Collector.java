package com.casic.yqgl.model;

public class Collector extends Page{
    private Integer collectorId;

    private String collectorName;

    private String collectorIp;

    private String collectorAddress;

    private String collectorStatus;

    private Integer collectorPort;

    private String collectorGateway;

    private String collectorMask;

    public Integer getCollectorId() {
        return collectorId;
    }

    public void setCollectorId(Integer collectorId) {
        this.collectorId = collectorId;
    }

    public String getCollectorName() {
        return collectorName;
    }

    public void setCollectorName(String collectorName) {
        this.collectorName = collectorName;
    }

    public String getCollectorIp() {
        return collectorIp;
    }

    public void setCollectorIp(String collectorIp) {
        this.collectorIp = collectorIp;
    }

    public String getCollectorAddress() {
        return collectorAddress;
    }

    public void setCollectorAddress(String collectorAddress) {
        this.collectorAddress = collectorAddress;
    }

    public String getCollectorStatus() {
        return collectorStatus;
    }

    public void setCollectorStatus(String collectorStatus) {
        this.collectorStatus = collectorStatus;
    }

    public Integer getCollectorPort() {
        return collectorPort;
    }

    public void setCollectorPort(Integer collectorPort) {
        this.collectorPort = collectorPort;
    }

    public String getCollectorGateway() {
        return collectorGateway;
    }

    public void setCollectorGateway(String collectorGateway) {
        this.collectorGateway = collectorGateway;
    }

    public String getCollectorMask() {
        return collectorMask;
    }

    public void setCollectorMask(String collectorMask) {
        this.collectorMask = collectorMask;
    }
}