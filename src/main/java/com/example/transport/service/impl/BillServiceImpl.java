package com.example.transport.service.impl;

import com.example.transport.dao.BillDao;
import com.example.transport.pojo.SysBill;
import com.example.transport.service.BillService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.util.List;

@Service("billService")
public class BillServiceImpl implements BillService{

    @Resource
    private BillDao billDao;

    @Transactional
    @Override
    public boolean insertBill(SysBill sysBill) {
        try{
            return billDao.insertBill(sysBill)==1?true:false;
        }
        catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
    }

    @Override
    public List<SysBill> selectUserBill(long sender_id) {
        return billDao.selectUserBill(sender_id);
    }

    @Override
    public SysBill selectSingleBill(long id) {
        return billDao.selectSingleBill(id);
    }

    @Transactional
    @Override
    public boolean updateBill(SysBill sysBill) {
        try{
            return billDao.updateBill(sysBill)==1?true:false;
        }
        catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
    }

    @Transactional
    @Override
    public boolean updateBillStatus(int bill_status,long id) {
        try{
            return billDao.updateBillStatus(bill_status,id)==1?true:false;
        }
        catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
    }

    @Transactional
    @Override
    public boolean deleteBill(long id) {
        try{
            return billDao.deleteBill(id)==1?true:false;
        }
        catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
    }

    @Override
    public List<SysBill> selectUnfinishBill(long sender_id) {
        return billDao.selectUnfinishBill(sender_id);
    }

    @Override
    public List<SysBill> selectUnfinishBillByTelOrName(String sender_param) {
        return billDao.selectUnfinishBillByTelOrName(sender_param);
    }

    @Transactional
    @Override
    public boolean updateBillSetTrans_id(long id, int bill_status, long trans_id) {
        try{
            return billDao.updateBillSetTrans_id(id,bill_status,trans_id)==1?true:false;
        }
        catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
    }

    @Override
    public List<SysBill> selectBillsByLnglat(String lng, String lat) {
        return billDao.selectBillsByLnglat(lng,lat);
    }
}
