package com.example.transport.service.impl;

import com.example.transport.dao.CompanyBillMapper;
import com.example.transport.pojo.CompanyBill;
import com.example.transport.service.CompanyBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;




@Service("companyBillService")
public class CompanyBillServiceImpl implements CompanyBillService{

    @Autowired
    private CompanyBillMapper companyBillMapper;


    @Override
    public Map<String, Object> adminSelectCompanyUnfinishedBill(int startPage, int pageSize) {
        Page page = PageHelper.startPage(startPage, pageSize);
        List<CompanyBill> sysbilllist = companyBillMapper.adminSelectCompanyUnfinishedBill();
        Map<String, Object> map = new HashMap<String,Object>();
        //每页信息
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
}
