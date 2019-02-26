package com.example.transport.service;

import com.example.transport.pojo.SysUserAddr;
import com.example.transport.pojo.UserToken;

import java.util.List;

/**
 * Created by WangZJ on 2018/10/7.
 */
public interface SysUserAddrService {

    boolean insertSysUserAddr(SysUserAddr sysUserAddr);

    int getAddrCount(long wxuserid);

    List<SysUserAddr> getAddrList(long wxuserid,int addrrole);

    boolean updateSysUserAddr(SysUserAddr sysUserAddr);

    SysUserAddr getAddrById(long id);

    boolean deleteAddrById(long id);

    boolean updateSysUserAddrDefault(long wxuserid);
}
