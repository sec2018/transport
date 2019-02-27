package com.example.transport.service.impl;

import com.example.transport.dao.UserDao;
import com.example.transport.pojo.User;
import com.example.transport.pojo.WxUser;
import com.example.transport.service.UserService;
import com.example.transport.util.AESUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by WangZJ on 2018/8/13.
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
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
    public User userLogin(String username, String pwd) {
        AESUtil util = new AESUtil();
        User user = null;
        user = userDao.getUserByLoginName(username);
        try {
            if(pwd.equals(user.getPassword())){
                return user;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
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

    @Override
    public long getWxUserId(String openid) {
        return userDao.getWxUserId(openid);
    }

    @Override
    public WxUser getWxUserById(long id) {
        return userDao.getWxUserById(id);
    }

    @Override
    public List<WxUser> getAllWxUser() {
        return userDao.getAllWxUser();
    }

    @Override
    public List<WxUser> getAllTrans() {
        return userDao.getAllTrans();
    }


}
