package com.example.transport.service.impl;

import com.example.transport.dao.BillDao;
import com.example.transport.pojo.SysBill;
import com.example.transport.service.BillService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.util.Date;
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
            e.printStackTrace();
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
    public boolean payBill(Date datetime, long id) {
        try{
            return billDao.payBill(datetime,id)==1?true:false;
        }
        catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
    }

    @Transactional
    @Override
    public boolean finishBill(Date datetime, long id, String company_code) {
        try{
            return billDao.finishBill(datetime,id,company_code)==1?true:false;
        }
        catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
    }

    @Transactional
    @Override
    public boolean deleteBill(long id,long wxuserid) {
        try{
            return billDao.deleteBill(id,wxuserid)==1?true:false;
        }
        catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
    }

    @Override
    public boolean deleteTransBill(long id, long trans_id) {
        try{
            return billDao.deleteTransBill(id,trans_id)==1?true:false;
        }
        catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
    }

    @Override
    public boolean deleteSenderBill(long id, long sender_id) {
        try{
            return billDao.deleteSenderBill(id,sender_id)==1?true:false;
        }
        catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
    }

    @Override
    public boolean deleteCompanyBill(long id, long company_id) {
        try{
            return billDao.deleteCompanyBill(id,company_id)==1?true:false;
        }
        catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
    }

    @Override
    public boolean cancelTransBill(long id, long trans_id) {
        try{
            return billDao.cancelTransBill(id,trans_id)==1?true:false;
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
    public List<SysBill> selectfinishedBill(long sender_id) {
        return billDao.selectfinishedBill(sender_id);
    }

    @Override
    public List<SysBill> selectUnfinishBillByTelOrName(String sender_param) {
        return billDao.selectUnfinishBillByTelOrName(sender_param);
    }

    @Transactional
    @Override
    public boolean updateBillSetTrans_id(long id, Date datetime,  long trans_id,String trans_name) {
        try{
            return billDao.updateBillSetTrans_id(id,datetime,trans_id,trans_name)==1?true:false;
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

    @Override
    public List<SysBill> selectfinishedBillByCompanyId(Integer company_id) {
        return billDao.selectfinishedBillByCompanyId(company_id);
    }

    @Override
    public List<SysBill> selectunfinishedBillByCompanyId(Integer company_id) {
        return billDao.selectunfinishedBillByCompanyId(company_id);
    }

    @Override
    public List<SysBill> selectAllUnBills() {
        return billDao.selectAllUnBills();
    }

    @Override
    public List<SysBill> selectunfinishedBillByTransId(long trans_id) {
        return billDao.selectunfinishedBillByTransId(trans_id);
    }

    @Override
    public List<SysBill> selectfinishedBillByTransId(long trans_id) {
        return billDao.selectfinishedBillByTransId(trans_id);
    }

    @Override
    public List<SysBill> selectBatchBills(long sender_id) {
        return billDao.selectBatchBills(sender_id);
    }

    @Override
    public boolean updateBatchBillsCode(long sender_id, String batch_code) {
        try{
            return billDao.updateBatchBillsCode(sender_id,batch_code)!=0?true:false;
        }
        catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
    }
}
