package com.example.transport.pojo;

/**
 * Created by WangZJ on 2018/10/7.
 */
public class SysUserAddr {
    private long id;
    private long wxuser_id;
    private String tel;
    private String pro_city;
    private String detail_addr;
    private String uname;
    private int isdefault;

    @Override
    public String toString() {
        return "SysUserAddr{" +
                "id=" + id +
                ", wxuser_id=" + wxuser_id +
                ", tel='" + tel + '\'' +
                ", pro_city='" + pro_city + '\'' +
                ", detail_addr='" + detail_addr + '\'' +
                ", uname='" + uname + '\'' +
                ", isdefault=" + isdefault +
                '}';
    }

    public int getIsdefault() {
        return isdefault;
    }

    public void setIsdefault(int isdefault) {
        this.isdefault = isdefault;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getWxuser_id() {
        return wxuser_id;
    }

    public void setWxuser_id(long wxuser_id) {
        this.wxuser_id = wxuser_id;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getPro_city() {
        return pro_city;
    }

    public void setPro_city(String pro_city) {
        this.pro_city = pro_city;
    }

    public String getDetail_addr() {
        return detail_addr;
    }

    public void setDetail_addr(String detail_addr) {
        this.detail_addr = detail_addr;
    }
}
