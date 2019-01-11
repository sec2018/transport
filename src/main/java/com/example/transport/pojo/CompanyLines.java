package com.example.transport.pojo;

public class CompanyLines {
    private Integer id;

    private Integer companyId;

    private String arriveAddr;

    private String arriveTel;

    private String beginAddr;

    public CompanyLines(Integer id, Integer companyId, String arriveAddr, String arriveTel, String beginAddr) {
        this.id = id;
        this.companyId = companyId;
        this.arriveAddr = arriveAddr;
        this.arriveTel = arriveTel;
        this.beginAddr = beginAddr;
    }

    public CompanyLines() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getArriveAddr() {
        return arriveAddr;
    }

    public void setArriveAddr(String arriveAddr) {
        this.arriveAddr = arriveAddr == null ? null : arriveAddr.trim();
    }

    public String getArriveTel() {
        return arriveTel;
    }

    public void setArriveTel(String arriveTel) {
        this.arriveTel = arriveTel == null ? null : arriveTel.trim();
    }

    public String getBeginAddr() {
        return beginAddr;
    }

    public void setBeginAddr(String beginAddr) {
        this.beginAddr = beginAddr == null ? null : beginAddr.trim();
    }
}