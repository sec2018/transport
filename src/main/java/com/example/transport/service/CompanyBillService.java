package com.example.transport.service;

import com.example.transport.pojo.CompanyBill;

import java.util.List;
import java.util.Map;

public interface CompanyBillService {

    //管理员查看所有未完成订单
    Map<String, Object> adminSelectCompanyUnfinishedBill(int startitem, int pagesize);

    //管理员查看所有已完成订单
    Map<String, Object> adminSelectCompanyfinishedBill(int startitem,int pagesize);

    //承运员查询未完成订单(分页)
    Map<String, Object>  selectCompanyUnfinishedBillByTransId(long trans_id,int startitem,int pagesize) throws Exception;

    //承运员查询已完成订单(分页)
    Map<String, Object>  selectCompanyfinishedBillByTransId(long trans_id,int startitem,int pagesize) throws Exception;

    //物流公司查询本公司所有已完成订单（分页）
    Map<String, Object>  selectCompanyfinishedBillByCompanyId(Integer company_id,int startitem,int pagesize) throws Exception;

    //物流公司查询本公司所有未完成订单（分页）
    Map<String, Object>  selectCompanyUnfinishedBillByCompanyId(Integer company_id,int startitem,int pagesize) throws Exception;

    //承运员根据名称和电话查询所有已完成订单
    Map<String, Object>  selectTransFinishCompanyBillByTelOrName(long wxuserid,String sender_param,int startitem,int pagesize);

    //承运员根据名称和电话查询所有未完成订单
    Map<String, Object>  selectTransUnfinishCompanyBillByTelOrName(long wxuserid,String sender_param,int startitem,int pagesize);

    //物流公司根据名称和电话查询所有已完成订单
    Map<String, Object>  selectFinishCompanyBillByTelOrName(Integer companyid,String sender_param,int startitem,int pagesize);

    //物流公司根据名称和电话查询所有未完成订单
    Map<String, Object>  selectUnfinishCompanyBillByTelOrName(Integer companyid,String sender_param,int startitem,int pagesize);

    //承运员查询未完成订单
    List<CompanyBill> selectunfinishedCompanyBillByTransId(long trans_id);

    //承运员查询已完成订单
    List<CompanyBill>  selectfinishedCompanyBillByTransId(long trans_id);

    //物流公司查询本公司所有已完成订单
    List<CompanyBill>  selectfinishedCompanyBillByCompanyId(Integer company_id);

    //物流公司查询本公司所有未完成订单
    List<CompanyBill>  selectunfinishedCompanyBillByCompanyId(Integer company_id);
}
