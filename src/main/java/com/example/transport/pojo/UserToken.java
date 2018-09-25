package com.example.transport.pojo;

import java.util.Date;

public class UserToken {
    private static final long serialVersionUID = 1L;

    private Long user_id;

    private String token;

    private Date expire_time;

    private Date update_time;

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getExpire_time() {
        return expire_time;
    }

    public void setExpire_time(Date expire_time) {
        this.expire_time = expire_time;
    }

    public Date getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Date update_time) {
        this.update_time = update_time;
    }

    @Override
    public String toString() {
        return "UserToken{" +
                "user_id=" + user_id +
                ", token='" + token + '\'' +
                ", expire_time=" + expire_time +
                ", update_time=" + update_time +
                '}';
    }
}
