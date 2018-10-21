package com.example.transport.pojo;

public class SysCompany {
    private Integer companyId;

    private String companyName;

    private String companyProcity;

    private String companyDetailarea;

    private Long wxuserId;

    private String companyTel;

    public SysCompany(Integer companyId, String companyName, String companyProcity, String companyDetailarea, Long wxuserId, String companyTel) {
        this.companyId = companyId;
        this.companyName = companyName;
        this.companyProcity = companyProcity;
        this.companyDetailarea = companyDetailarea;
        this.wxuserId = wxuserId;
        this.companyTel = companyTel;
    }

    public SysCompany() {
        super();
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName == null ? null : companyName.trim();
    }

    public String getCompanyProcity() {
        return companyProcity;
    }

    public void setCompanyProcity(String companyProcity) {
        this.companyProcity = companyProcity == null ? null : companyProcity.trim();
    }

    public String getCompanyDetailarea() {
        return companyDetailarea;
    }

    public void setCompanyDetailarea(String companyDetailarea) {
        this.companyDetailarea = companyDetailarea == null ? null : companyDetailarea.trim();
    }

    public Long getWxuserId() {
        return wxuserId;
    }

    public void setWxuserId(Long wxuserId) {
        this.wxuserId = wxuserId;
    }

    public String getCompanyTel() {
        return companyTel;
    }

    public void setCompanyTel(String companyTel) {
        this.companyTel = companyTel == null ? null : companyTel.trim();
    }
}