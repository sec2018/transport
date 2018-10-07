package com.example.transport.service.impl;

import com.example.transport.dao.SysUserAddrDao;
import com.example.transport.pojo.SysUserAddr;
import com.example.transport.service.SysUserAddrService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by WangZJ on 2018/10/7.
 */
@Service("sysUserAddrService")
public class SysUserAddrServiceImpl implements SysUserAddrService{
    @Resource
    private SysUserAddrDao sysUserAddrDao;

    @Override
    public boolean insertSysUserAddr(SysUserAddr sysUserAddr) {
        return sysUserAddrDao.insertSysUserAddr(sysUserAddr)==1?true:false;
    }

    @Override
    public int getAddrCount(long wxuserid) {
        return sysUserAddrDao.getAddrCount(wxuserid);
    }

    @Override
    public List<SysUserAddr> getAddrList(long wxuserid) {
        return sysUserAddrDao.getAddrList(wxuserid);
    }

    @Override
    public int updateSysUserAddr(SysUserAddr sysUserAddr) {
        return sysUserAddrDao.updateSysUserAddr(sysUserAddr);
    }

    @Override
    public SysUserAddr getAddrById(long id) {
        return sysUserAddrDao.getAddrById(id);
    }

    @Override
    public int deleteAddrById(long id) {
        return sysUserAddrDao.deleteAddrById(id);
    }
}
