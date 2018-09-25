package com.example.transport.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by WangZJ on 2018/7/22.
 */
public class User implements Serializable{
    private static final long serialVersionUID = 1L;

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
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

    public String getWechat_name() {
        return wechat_name;
    }

    public void setWechat_name(String wechat_name) {
        this.wechat_name = wechat_name;
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

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    @Override
    public String toString() {
        return "User{" +
                "user_id=" + user_id +
                ", loginname='" + loginname + '\'' +
                ", password='" + password + '\'' +
                ", wechat_name='" + wechat_name + '\'' +
                ", telephone='" + telephone + '\'' +
                ", create_time=" + create_time +
                ", shop_name='" + shop_name + '\'' +
                '}';
    }

    private Long user_id;
    private String loginname;
    private String password;
    private String wechat_name;
    private String telephone;
    private Date create_time;
    private String shop_name;




}
