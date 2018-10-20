package com.example.transport.pojo;

public class SysCompany {

    private int company_id;
    private String company_name;
    private String company_procity;
    private String company_detailarea;

    public int getCompany_id() {
        return company_id;
    }

    public String getCompany_procity() {
        return company_procity;
    }

    public void setCompany_procity(String company_procity) {
        this.company_procity = company_procity;
    }

    public String getCompany_detailarea() {
        return company_detailarea;
    }

    public void setCompany_detailarea(String company_detailarea) {
        this.company_detailarea = company_detailarea;
    }

    public void setCompany_id(int company_id) {
        this.company_id = company_id;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }
}
