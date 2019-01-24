package com.example.transport.service.impl;

import com.example.transport.api.Common;
import com.example.transport.dao.BillDao;
import com.example.transport.dao.CompanyLinesMapper;
import com.example.transport.pojo.BillView;
import com.example.transport.pojo.CompanyLines;
import com.example.transport.pojo.SysBill;
import com.example.transport.service.BillService;
import com.example.transport.util.MapUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.util.*;

@Service("billService")
public class BillServiceImpl implements BillService{

    @Resource
    private BillDao billDao;

    @Autowired
    private CompanyLinesMapper companyLinesMapper;

    @Transactional
    @Override
    public boolean insertBill(SysBill sysBill) {
        try{
            boolean flag = billDao.insertBill(sysBill)==1?true:false;
            if(flag){
                Common.unbilllist  = billDao.selectAllUnBills();
            }
            return flag;
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
    public boolean SenderUpdateBill(SysBill sysBill) {
        try{
            return billDao.SenderUpdateBill(sysBill)==1?true:false;
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
    public boolean deleteSenderBill(long id,long wxuserid) {
        try{
            return billDao.deleteSenderBill(id,wxuserid)==1?true:false;
        }
        catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
    }

    @Transactional
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

    @Transactional
    @Override
    public boolean deleteSenderUnRecBill(long id, long sender_id) {
        try{
            return billDao.deleteSenderUnRecBill(id,sender_id)==1?true:false;
        }
        catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
    }

    @Transactional
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

    @Transactional
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
    public Map<String, Object> selectUnfinishBill(long sender_id, int startPage, int pageSize) throws Exception {
        Page page = PageHelper.startPage(startPage, pageSize);
        List<SysBill>  sysbilllist = billDao.selectUnfinishBill(sender_id);
        Map<String, Object> map = new HashMap<String,Object>();
        //每页信息
        map.put("data", sysbilllist);
        //管理员总数
        map.put("totalNum", page.getTotal());
        return map;
    }

    @Override
    public List<SysBill> selectfinishedBill(long sender_id) {
        return billDao.selectfinishedBill(sender_id);
    }

    @Override
    public Map<String, Object> selectfinishedBill(long sender_id, int startitem, int pagesize) throws Exception {
        Page page = PageHelper.startPage(startitem, pagesize);
        List<SysBill>  sysbilllist = billDao.selectfinishedBill(sender_id);
        Map<String, Object> map = new HashMap<String,Object>();
        //每页信息
        map.put("data", sysbilllist);
        //管理员总数
        map.put("totalNum", page.getTotal());
        return map;
    }

    @Override
    public Map<String, Object> selectTransFinishBillByTelOrName(long wxuserid, String sender_param,int startitem,int pagesize) {
        Page page = PageHelper.startPage(startitem, pagesize);
        List<SysBill>  sysbilllist = billDao.selectTransFinishBillByTelOrName(wxuserid,sender_param);
        Map<String, Object> map = new HashMap<String,Object>();
        //每页信息
        map.put("data", sysbilllist);
        //管理员总数
        map.put("totalNum", page.getTotal());
        return map;
    }

    @Override
    public Map<String, Object> selectTransUnfinishBillByTelOrName(long wxuserid, String sender_param,int startitem,int pagesize) {
        Page page = PageHelper.startPage(startitem, pagesize);
        List<SysBill>  sysbilllist = billDao.selectTransUnfinishBillByTelOrName(wxuserid,sender_param);
        Map<String, Object> map = new HashMap<String,Object>();
        //每页信息
        map.put("data", sysbilllist);
        //管理员总数
        map.put("totalNum", page.getTotal());
        return map;
    }

    @Override
    public Map<String, Object> selectShoperFinishBillByTelOrName(long wxuserid, String sender_param,int startitem,int pagesize) {
        Page page = PageHelper.startPage(startitem, pagesize);
        List<SysBill>  sysbilllist = billDao.selectShoperFinishBillByTelOrName(wxuserid,sender_param);
        Map<String, Object> map = new HashMap<String,Object>();
        //每页信息
        map.put("data", sysbilllist);
        //管理员总数
        map.put("totalNum", page.getTotal());
        return map;
    }

    @Override
    public Map<String, Object> selectShoperUnfinishBillByTelOrName(long wxuserid, String sender_param,int startitem,int pagesize) {
        Page page = PageHelper.startPage(startitem, pagesize);
        List<SysBill>  sysbilllist = billDao.selectShoperUnfinishBillByTelOrName(wxuserid,sender_param);
        Map<String, Object> map = new HashMap<String,Object>();
        //每页信息
        map.put("data", sysbilllist);
        //管理员总数
        map.put("totalNum", page.getTotal());
        return map;
    }

    @Override
    public Map<String, Object> selectCompanyFinishBillByTelOrName(long wxuserid, String sender_param,int startitem,int pagesize) {
        Page page = PageHelper.startPage(startitem, pagesize);
        List<SysBill>  sysbilllist = billDao.selectCompanyFinishBillByTelOrName(wxuserid,sender_param);
        Map<String, Object> map = new HashMap<String,Object>();
        //每页信息
        map.put("data", sysbilllist);
        //管理员总数
        map.put("totalNum", page.getTotal());
        return map;
    }

    @Override
    public Map<String, Object> selectCompanyUnfinishBillByTelOrName(long wxuserid, String sender_param,int startitem,int pagesize) {
        Page page = PageHelper.startPage(startitem, pagesize);
        List<SysBill>  sysbilllist = billDao.selectCompanyUnfinishBillByTelOrName(wxuserid,sender_param);
        Map<String, Object> map = new HashMap<String,Object>();
        //每页信息
        map.put("data", sysbilllist);
        //管理员总数
        map.put("totalNum", page.getTotal());
        return map;
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
    public List<SysBill> selectBillsByLnglat(String sender_lng, String sender_lat) {
        return billDao.selectBillsByLnglat(sender_lng,sender_lat);
    }

    @Override
    public List<SysBill> selectBillsIn2Mills(String sender_lng, String sender_lat) {
        List<SysBill> list =  Common.unbilllist;
        if(list.size()==0){
            list = billDao.selectAllUnBills();
        }
        List<SysBill> templist = new ArrayList<SysBill>();
        for (SysBill sysBill:list) {
            double distance = MapUtil.getDistance(sender_lng,sender_lat,sysBill.getSender_lng(),sysBill.getSender_lat());
            if(distance<=2.0){
                templist.add(sysBill);
            }
        }
        return templist;
    }

    @Override
    public List<SysBill> selectfinishedBillByCompanyId(Integer company_id) {
        return billDao.selectfinishedBillByCompanyId(company_id);
    }

    @Override
    public Map<String, Object> selectfinishedBillByCompanyId(Integer company_id, int startitem, int pagesize) throws Exception {
        Page page = PageHelper.startPage(startitem, pagesize);
        List<SysBill>  sysbilllist = billDao.selectfinishedBillByCompanyId(company_id);
        Map<String, Object> map = new HashMap<String,Object>();
        //每页信息
        map.put("data", sysbilllist);
        //管理员总数
        map.put("totalNum", page.getTotal());
        return map;
    }

    @Override
    public List<SysBill> selectunfinishedBillByCompanyId(Integer company_id) {
        return billDao.selectunfinishedBillByCompanyId(company_id);
    }

    @Override
    public Map<String, Object> selectunfinishedBillByCompanyId(Integer company_id, int startPage, int pageSize) throws Exception {
        Page page = PageHelper.startPage(startPage, pageSize);
        List<SysBill>  sysbilllist = billDao.selectunfinishedBillByCompanyId(company_id);
        Map<String, Object> map = new HashMap<String,Object>();
        //每页信息
        map.put("data", sysbilllist);
        //管理员总数
        map.put("totalNum", page.getTotal());
        return map;
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
    public Map<String, Object> selectunfinishedBillByTransId(long trans_id, int startPage, int pageSize) throws Exception {
        Page page = PageHelper.startPage(startPage, pageSize);
        List<SysBill>  sysbilllist = billDao.selectunfinishedBillByTransId(trans_id);
        Map<String, Object> map = new HashMap<String,Object>();
        //每页信息
        map.put("data", sysbilllist);
        //管理员总数
        map.put("totalNum", page.getTotal());
        return map;
    }

    @Override
    public List<SysBill> selectfinishedBillByTransId(long trans_id) {
        return billDao.selectfinishedBillByTransId(trans_id);
    }

    @Override
    public Map<String, Object> selectfinishedBillByTransId(long trans_id, int startitem, int pagesize) throws Exception {
        Page page = PageHelper.startPage(startitem, pagesize);
        List<SysBill>  sysbilllist = billDao.selectfinishedBillByTransId(trans_id);
        Map<String, Object> map = new HashMap<String,Object>();
        //每页信息
        map.put("data", sysbilllist);
        //管理员总数
        map.put("totalNum", page.getTotal());
        return map;
    }

    @Override
    public List<SysBill> selectBatchBills(long sender_id) {
        return billDao.selectBatchBills(sender_id);
    }

    @Override
    public List<SysBill> selectBatchBills(long sender_id, int startPage, int pageSize) throws Exception {
        Page page = PageHelper.startPage(startPage, pageSize);
        List<SysBill>  sysbilllist = billDao.selectBatchBills(sender_id);
        return sysbilllist;
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

    @Override
    public Map<String, Object> adminSelectunfinishedBill(int startPage, int pageSize) {
        Page page = PageHelper.startPage(startPage, pageSize);
        List<SysBill>  sysbilllist = billDao.adminSelectunfinishedBill();
        List<BillView> viewList = new ArrayList<>();
        BillView billView;
        CompanyLines companyLine;
        for(SysBill sb : sysbilllist){
            billView = new BillView();
            billView.setId(sb.getId());
            billView.setBill_code(sb.getBill_code());
            companyLine = companyLinesMapper.selectByPrimaryKey(sb.getLine_id());
            billView.setLine(companyLine.getBeginAddr()+"-->"+companyLine.getArriveAddr());
            billView.setShop_name(sb.getShop_name());
            billView.setRec_name(sb.getRec_name());
            billView.setCompany_name(sb.getCompany_name());
            switch (sb.getBill_status()){
                case 1:
                    billView.setStatus("已下单，待接单");
                    billView.setStatustime(sb.getCreate_time());
                    break;
                case 2:
                    billView.setStatus("已接单，待揽收");
                    billView.setStatustime(sb.getRec_time());
                    break;
                case 3:
                    billView.setStatus("已揽收，托运中");
                    billView.setStatustime(sb.getPay_time());
                    break;
            }
            viewList.add(billView);
        }
        Map<String, Object> map = new HashMap<String,Object>();
        //每页信息
        map.put("billviewdata", viewList);
        map.put("billdata", sysbilllist);
        //管理员总数
        map.put("totalNum", page.getTotal());
        return map;
    }

    @Override
    public Map<String, Object> adminSelectfinishedBill(int startPage, int pageSize) {
        Page page = PageHelper.startPage(startPage, pageSize);
        List<SysBill>  sysbilllist = billDao.adminSelectfinishedBill();
        List<BillView> viewList = new ArrayList<>();
        BillView billView;
        CompanyLines companyLine;
        for(SysBill sb : sysbilllist){
            billView = new BillView();
            billView.setId(sb.getId());
            billView.setBill_code(sb.getBill_code());
            companyLine = companyLinesMapper.selectByPrimaryKey(sb.getLine_id());
            billView.setLine(companyLine.getBeginAddr()+"-->"+companyLine.getArriveAddr());
            billView.setShop_name(sb.getShop_name());
            billView.setRec_name(sb.getRec_name());
            billView.setCompany_name(sb.getCompany_name());
            billView.setStatus("运单完成");
            billView.setStatustime(sb.getFinish_time());
            viewList.add(billView);
        }
        Map<String, Object> map = new HashMap<String,Object>();
        //每页信息
        map.put("billviewdata", viewList);
        map.put("billdata", sysbilllist);
        //管理员总数
        map.put("totalNum", page.getTotal());
        return map;
    }
}
