package com.example.transport.service.impl;

import com.example.transport.dao.SysUserTokenDao;
import com.example.transport.pojo.UserToken;
import com.example.transport.service.SysUserTokenService;
import com.example.transport.util.R;
import com.example.transport.util.TokenGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SysUserTokenServiceImpl implements SysUserTokenService {
    @Autowired
    private SysUserTokenDao sysUserTokenDao;

    //12小时候过期
    private final static int EXPIRE = 3600 * 12;

    @Override
    public UserToken queryByToken(String token){
        return sysUserTokenDao.queryByToken(token);
    }


    @Override
    public R createToken(long userId) {
        //生成一个token
        String token = TokenGenerator.generateValue();

        //当前时间
        Date now = new Date();
        //过期时间
        Date expireTime = new Date(now.getTime()+EXPIRE*1000);

        //判断是否生成过token
        UserToken tokenEntity = sysUserTokenDao.selectById(userId);
        if(tokenEntity == null){
            tokenEntity = new UserToken();
            tokenEntity.setUser_id(userId);
            tokenEntity.setToken(token);
            tokenEntity.setUpdate_time(now);
            tokenEntity.setExpire_time(expireTime);

            //保存token
            sysUserTokenDao.insertToken(tokenEntity);
        }else{
            tokenEntity.setToken(token);
            tokenEntity.setUpdate_time(now);
            tokenEntity.setExpire_time(expireTime);

            //更新token
            sysUserTokenDao.updateToken(tokenEntity);
        }
        R r = R.ok().put("token",token).put("expire",EXPIRE);
        return r;
    }

    @Override
    public int updateToken(UserToken userToken) {
        return sysUserTokenDao.updateToken(userToken);
    }
}
