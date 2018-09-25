package com.example.transport.service;


import com.example.transport.pojo.User;

/**
 * Created by WangZJ on 2018/8/13.
 */
public interface UserService {
    String getPasswordByUserName(String loginname);

    User getUserByLoginName(String loginname);
}
