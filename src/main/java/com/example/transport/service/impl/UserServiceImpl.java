package com.example.transport.service.impl;

import com.example.transport.dao.UserDao;
import com.example.transport.pojo.User;
import com.example.transport.pojo.WxUser;
import com.example.transport.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by WangZJ on 2018/8/13.
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;

    @Override
    public String getPasswordByUserName(String loginname) {
        return userDao.getPasswordByUserName(loginname);
    }

    @Override
    public User getUserByLoginName(String loginname) {
        return userDao.getUserByLoginName(loginname);
    }

    @Override
    public boolean insertWxUser(WxUser wxUser) {
        return userDao.insertWxUser(wxUser)==1?true:false;
    }

    @Override
    public WxUser getWxUser(String openid) {
        return userDao.getWxUser(openid);
    }

    @Override
    public boolean updateWxUser(WxUser wxUser) {
        return userDao.updateWxUser(wxUser)==1?true:false;
    }
}
