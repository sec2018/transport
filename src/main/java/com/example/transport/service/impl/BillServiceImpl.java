package com.example.transport.service.impl;

import com.example.transport.dao.BillDao;
import com.example.transport.pojo.SysBill;
import com.example.transport.service.BillService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("billService")
public class BillServiceImpl implements BillService{

    @Resource
    private BillDao billDao;

    @Override
    public boolean insertBill(SysBill sysBill) {
        return billDao.insertBill(sysBill)==1?true:false;
    }
}
