package com.example.transport.service.impl;

import com.example.transport.dao.CompanyBillMapper;
import com.example.transport.pojo.CompanyBill;
import com.example.transport.pojo.CompanyBillView;
import com.example.transport.service.CompanyBillService;
import com.example.transport.util.MapUtil;
import com.example.transport.util.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.*;


@Service("companyBillService")
public class CompanyBillServiceImpl implements CompanyBillService{

    @Autowired
    private CompanyBillMapper companyBillMapper;

    @Autowired
    private RedisService redisService;


    @Override
    public Map<String, Object> adminSelectCompanyUnfinishedBill(int startPage, int pageSize) {
        Page page = PageHelper.startPage(startPage, pageSize);
        List<CompanyBill> sysbilllist = companyBillMapper.adminSelectCompanyUnfinishedBill();
        Map<String, Object> map = new HashMap<String,Object>();

        List<CompanyBillView> viewList = new ArrayList<>();
        CompanyBillView companyBillView;
        for(CompanyBill sb : sysbilllist){
            companyBillView = new CompanyBillView();
            companyBillView.setId(sb.getId());
            companyBillView.setCompany_name(sb.getCompany_name());
            companyBillView.setBill_code(sb.getBill_code());
            companyBillView.setCompany_billcode(sb.getCompany_billcode());
            companyBillView.setShop_name(sb.getShop_name());
            companyBillView.setRec_name(sb.getRec_name());
            companyBillView.setRec_tel(sb.getRec_tel());
            switch (sb.getBill_status()){
                case 1:
                    companyBillView.setStatus("已下单，待接单");
                    companyBillView.setStatustime(sb.getCreate_time());
                    break;
                case 2:
                    companyBillView.setStatus("已接单，待揽收");
                    companyBillView.setStatustime(sb.getRec_time());
                    break;
                case 3:
                    companyBillView.setStatus("已揽收，托运中");
                    companyBillView.setStatustime(sb.getConfirm_time());
                    break;
            }
            viewList.add(companyBillView);
        }
        //每页信息
        map.put("companybillviewdata", viewList);
        map.put("companybilldata", sysbilllist);
        //管理员总数
        map.put("totalNum", page.getTotal());
        return map;
    }

    @Override
    public Map<String, Object> adminSelectCompanyfinishedBill(int startPage, int pageSize) {
        Page page = PageHelper.startPage(startPage, pageSize);
        List<CompanyBill>  sysbilllist = companyBillMapper.adminSelectCompanyfinishedBill();
        Map<String, Object> map = new HashMap<String,Object>();
        //每页信息
        map.put("companybilldata", sysbilllist);
        //管理员总数
        map.put("totalNum", page.getTotal());
        return map;
    }

    @Override
    public Map<String, Object> selectCompanyUnfinishedBillByTransId(long trans_id, int startPage, int pageSize) throws Exception {
        Page page = PageHelper.startPage(startPage, pageSize);
        List<CompanyBill>  sysbilllist = companyBillMapper.selectCompanyUnfinishedBillByTransId(trans_id);
        Map<String, Object> map = new HashMap<String,Object>();
        //每页信息
        map.put("data", sysbilllist);
        //管理员总数
        map.put("totalNum", page.getTotal());
        return map;
    }

    @Override
    public Map<String, Object> selectCompanyfinishedBillByTransId(long trans_id, int startPage, int pageSize) throws Exception {
        Page page = PageHelper.startPage(startPage, pageSize);
        List<CompanyBill>  sysbilllist = companyBillMapper.selectCompanyfinishedBillByTransId(trans_id);
        Map<String, Object> map = new HashMap<String,Object>();
        //每页信息
        map.put("data", sysbilllist);
        //管理员总数
        map.put("totalNum", page.getTotal());
        return map;
    }

    @Override
    public Map<String, Object> selectCompanyfinishedBillByCompanyId(Integer company_id, int startitem, int pagesize) throws Exception {
        Page page = PageHelper.startPage(startitem, pagesize);
        List<CompanyBill>  sysbilllist = companyBillMapper.selectCompanyfinishedBillByCompanyId(company_id);
        Map<String, Object> map = new HashMap<String,Object>();
        //每页信息
        map.put("data", sysbilllist);
        //管理员总数
        map.put("totalNum", page.getTotal());
        return map;
    }

    @Override
    public Map<String, Object> selectCompanyUnfinishedBillByCompanyId(Integer company_id, int startitem, int pagesize) throws Exception {
        Page page = PageHelper.startPage(startitem, pagesize);
        List<CompanyBill>  sysbilllist = companyBillMapper.selectCompanyUnfinishedBillByCompanyId(company_id);
        Map<String, Object> map = new HashMap<String,Object>();
        //每页信息
        map.put("data", sysbilllist);
        //管理员总数
        map.put("totalNum", page.getTotal());
        return map;
    }

    @Override
    public Map<String, Object> selectTransFinishCompanyBillByTelOrName(long wxuserid, String sender_param, int startitem, int pagesize) {
        Page page = PageHelper.startPage(startitem, pagesize);
        List<CompanyBill>  sysbilllist = companyBillMapper.selectTransFinishCompanyBillByTelOrName(wxuserid,sender_param);
        Map<String, Object> map = new HashMap<String,Object>();
        //每页信息
        map.put("data", sysbilllist);
        //管理员总数
        map.put("totalNum", page.getTotal());
        return map;
    }

    @Override
    public Map<String, Object> selectTransUnfinishCompanyBillByTelOrName(long wxuserid, String sender_param, int startitem, int pagesize) {
        Page page = PageHelper.startPage(startitem, pagesize);
        List<CompanyBill>  sysbilllist = companyBillMapper.selectTransUnfinishCompanyBillByTelOrName(wxuserid,sender_param);
        Map<String, Object> map = new HashMap<String,Object>();
        //每页信息
        map.put("data", sysbilllist);
        //管理员总数
        map.put("totalNum", page.getTotal());
        return map;
    }

    @Override
    public Map<String, Object> selectFinishCompanyBillByTelOrName(Integer company_id, String sender_param, int startitem, int pagesize) {
        Page page = PageHelper.startPage(startitem, pagesize);
        List<CompanyBill>  sysbilllist = companyBillMapper.selectFinishCompanyBillByTelOrName(company_id,sender_param);
        Map<String, Object> map = new HashMap<String,Object>();
        //每页信息
        map.put("data", sysbilllist);
        //管理员总数
        map.put("totalNum", page.getTotal());
        return map;
    }

    @Override
    public Map<String, Object> selectUnfinishCompanyBillByTelOrName(Integer company_id, String sender_param, int startitem, int pagesize) {
        Page page = PageHelper.startPage(startitem, pagesize);
        List<CompanyBill>  sysbilllist = companyBillMapper.selectUnfinishCompanyBillByTelOrName(company_id,sender_param);
        Map<String, Object> map = new HashMap<String,Object>();
        //每页信息
        map.put("data", sysbilllist);
        //管理员总数
        map.put("totalNum", page.getTotal());
        return map;
    }

    @Override
    public List<CompanyBill> selectunfinishedCompanyBillByTransId(long trans_id) {
        return companyBillMapper.selectunfinishedCompanyBillByTransId(trans_id);
    }

    @Override
    public List<CompanyBill> selectfinishedCompanyBillByTransId(long trans_id) {
        return companyBillMapper.selectfinishedCompanyBillByTransId(trans_id);
    }

    @Override
    public List<CompanyBill> selectfinishedCompanyBillByCompanyId(Integer company_id) {
        return companyBillMapper.selectfinishedCompanyBillByCompanyId(company_id);
    }

    @Override
    public List<CompanyBill> selectunfinishedCompanyBillByCompanyId(Integer company_id) {
        return companyBillMapper.selectunfinishedCompanyBillByCompanyId(company_id);
    }

    @Override
    public List<CompanyBill> selectCompanyBillsIn2Mills(String sender_lng, String sender_lat) {
        List<CompanyBill> unbilllist = companyBillMapper.selectAllCompanyUnRecBills();
        List<CompanyBill> templist = new ArrayList<CompanyBill>();
        for (CompanyBill bill:unbilllist) {
            double distance = MapUtil.getDistance(sender_lng,sender_lat,bill.getCompany_lng(),bill.getCompany_lat());
            if(distance<=2.0){
                templist.add(bill);
            }
        }
        return templist;
    }

    @Override
    public List<CompanyBill> selectAllCompanyUnBills() {
        return companyBillMapper.selectAllCompanyUnBills();
    }

    @Transactional
    @Override
    public boolean updateCompanyBillSetTrans_id(long id, Date datetime, long trans_id, String trans_name) {
        try{
            return companyBillMapper.updateCompanyBillSetTrans_id(id,datetime,trans_id,trans_name)==1?true:false;
        }
        catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
    }

    @Transactional
    @Override
    public boolean deleteCompanyUnRecBill(long id) {
        try{
            return companyBillMapper.deleteCompanyUnRecBill(id)==1?true:false;
        }
        catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
    }

    @Override
    public CompanyBill selectSingleCompanyBill(long id) {
        return companyBillMapper.selectSingleCompanyBill(id);
    }

    @Transactional
    @Override
    public boolean confirmCompanyBill(Date comfirm_time, long id) {
        try{
            return companyBillMapper.confirmCompanyBill(comfirm_time,id)==1?true:false;
        }
        catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
    }


}
