package com.example.transport.pojo;

import org.springframework.data.redis.core.types.Expiration;

public class Constant {

    //常量
    public static final String[]  NO_MATCH_PATHLIST = {".*/(swagger).*",".*/(api).*",".*/(logout).*",".*/(login).*",".*/(bootstrap).*",".*/(dist).*",".*/(pages).*",".*/adminlte/pages/(register.html).*"};


    //redis过期时间
    public final static Expiration expire = Expiration.seconds(7200);//7200秒后数据过期
    private int code;
    private String msg;

    private Constant( ) { }
    private Constant( int code,String msg ) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "Constant{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }

    public static Constant SUCCESS = new Constant(200, "success");
    public static Constant NOT_LOGIN = new Constant(50101, "没有登录");
    public static Constant LOGIN_EXCEPTION = new Constant(50102, "登录异常");
    public static Constant LOGIN_USERNOTEXIST = new Constant(50103, "用户不存在");
    public static Constant LOGIN_ALREADY = new Constant(50104, "用户已登录");

}
