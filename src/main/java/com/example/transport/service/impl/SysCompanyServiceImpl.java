package com.example.transport.service.impl;

import com.example.transport.dao.SysCompanyDao;
import com.example.transport.pojo.SysCompany;
import com.example.transport.service.SysCompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("sysCompanyService")
public class SysCompanyServiceImpl implements SysCompanyService{

    @Autowired
    private SysCompanyDao sysCompanyDao;

    @Override
    public boolean insertBill(SysCompany sysCompany) {
        return sysCompanyDao.insertBill(sysCompany)==1?true:false;
    }
}
