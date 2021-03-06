package com.example.transport.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by WangZJ on 2018/7/22.
 */
public class User implements Serializable{
    private static final long serialVersionUID = 1L;

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getWxuser_id() {
        return wxuser_id;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", wxuser_id=" + wxuser_id +
                ", loginname='" + loginname + '\'' +
                ", password='" + password + '\'' +
                ", openid='" + openid + '\'' +
                ", telephone='" + telephone + '\'' +
                ", create_time=" + create_time +
                '}';
    }

    public void setWxuser_id(Long wxuser_id) {
        this.wxuser_id = wxuser_id;
    }

    public String getLoginname() {
        return loginname;
    }

    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    private Long wxuser_id;
    private String loginname;
    private String password;

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    private String openid;
    private String telephone;
    private Date create_time;




}
