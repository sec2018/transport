package com.example.transport.service;


import com.example.transport.pojo.User;
import com.example.transport.pojo.WxUser;

import java.util.List;

/**
 * Created by WangZJ on 2018/8/13.
 */
public interface UserService {
    String getPasswordByUserName(String loginname);

    User getUserByLoginName(String loginname);

    User userLogin(String username, String pwd);

    boolean insertWxUser(WxUser wxUser);

    WxUser getWxUser(String openid);

    boolean updateWxUser(WxUser wxUser);

    long getWxUserId(String openid);

    WxUser getWxUserById(long id);

    List<WxUser> getAllWxUser();

    List<WxUser> getAllTrans();
 }
