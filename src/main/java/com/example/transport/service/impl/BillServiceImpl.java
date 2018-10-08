package com.example.transport.service.impl;

import com.example.transport.dao.BillDao;
import com.example.transport.pojo.SysBill;
import com.example.transport.service.BillService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("billService")
public class BillServiceImpl implements BillService{

    @Resource
    private BillDao billDao;

    @Override
    public boolean insertBill(SysBill sysBill) {
        return billDao.insertBill(sysBill)==1?true:false;
    }

    @Override
    public List<SysBill> selectUserBill(long sender_id) {
        return billDao.selectUserBill(sender_id);
    }

    @Override
    public SysBill selectSingleBill(long id) {
        return billDao.selectSingleBill(id);
    }

    @Override
    public boolean updateBill(SysBill sysBill) {
        return billDao.updateBill(sysBill)==1?true:false;
    }

    @Override
    public boolean updateBillStatus(int bill_status,long id) {
        return billDao.updateBillStatus(bill_status,id)==1?true:false;
    }

    @Override
    public boolean deleteBill(long id) {
        return billDao.deleteBill(id)==1?true:false;
    }

    @Override
    public List<SysBill> selectUnfinishBill(long sender_id) {
        return billDao.selectUnfinishBill(sender_id);
    }

    @Override
    public List<SysBill> selectUnfinishBillByTelOrName(String sender_param) {
        return billDao.selectUnfinishBillByTelOrName(sender_param);
    }
}
