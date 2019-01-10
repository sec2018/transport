package com.example.transport.service;

import com.example.transport.pojo.UserToken;
import com.example.transport.util.R;

public interface SysUserTokenService {
    UserToken queryByToken(String token);

    /**
     * 生成token
     * @param userId
     * @return
     */
    R createToken(long userId);

    int updateToken(UserToken userToken);

    String getToken(long userId);
}
