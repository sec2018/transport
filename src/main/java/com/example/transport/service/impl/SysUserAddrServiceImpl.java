package com.example.transport.service.impl;

import com.example.transport.dao.SysUserAddrDao;
import com.example.transport.pojo.SysUserAddr;
import com.example.transport.service.SysUserAddrService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by WangZJ on 2018/10/7.
 */
@Service("sysUserAddrService")
public class SysUserAddrServiceImpl implements SysUserAddrService{
    @Resource
    private SysUserAddrDao sysUserAddrDao;

    @Transactional
    @Override
    public boolean insertSysUserAddr(SysUserAddr sysUserAddr) {
        try{
            return sysUserAddrDao.insertSysUserAddr(sysUserAddr)==1?true:false;
        }
        catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
    }

    @Override
    public int getAddrCount(long wxuserid) {
        return sysUserAddrDao.getAddrCount(wxuserid);
    }

    @Override
    public List<SysUserAddr> getAddrList(long wxuserid, int addrole) {
        return sysUserAddrDao.getAddrList(wxuserid,addrole);
    }

    @Transactional
    @Override
    public boolean updateSysUserAddr(SysUserAddr sysUserAddr) {
        try{
            return sysUserAddrDao.updateSysUserAddr(sysUserAddr)==1?true:false;
        }
        catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
    }

    @Override
    public SysUserAddr getAddrById(long id) {
        return sysUserAddrDao.getAddrById(id);
    }

    @Transactional
    @Override
    public boolean deleteAddrById(long id) {
        try{
            return sysUserAddrDao.deleteAddrById(id)==1?true:false;
        }
        catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
    }

    @Transactional
    @Override
    public boolean updateSysUserAddrDefault(long wxuserid) {
        try{
            return sysUserAddrDao.updateSysUserAddrDefault(wxuserid)==1?true:false;
        }
        catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
    }
}
