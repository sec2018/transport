package com.example.transport.service;


import com.example.transport.pojo.SysBill;

import java.util.List;

public interface BillService {

    boolean insertBill(SysBill sysBill);

    //根据sender_id来查询某用户下所有订单
    List<SysBill> selectUserBill(long sender_id);

    //根据id来查询特定订单
    SysBill selectSingleBill(long id);

    //更新订单内容
    boolean updateBill(SysBill sysBill);

    //更新订单状态
    boolean updateBillStatus(int bill_status,long id);

    //删除订单
    boolean deleteBill(long id);

    //商家根据sender_id和状态bill_status 来查询未完成订单
    List<SysBill>  selectUnfinishBill(long sender_id);

    //商家根据sender_id和状态bill_status = 4来查询已完成订单
    List<SysBill>  selectfinishedBill(long sender_id);

    //承运员根据名称和电话查询所有已完成订单
    List<SysBill>  selectUnfinishBillByTelOrName(String sender_param);

    //接单
    boolean updateBillSetTrans_id(long id, int bill_status,long trans_id);

    //根据经纬度得到未接订单
    List<SysBill> selectBillsByLnglat(String lng,String lat);  //经度lng,维度lat

    //物流公司查询本公司所有已完成订单
    List<SysBill>  selectfinishedBillByCompanyId(Integer company_id);

    //物流公司查询本公司所有未完成订单
    List<SysBill>  selectunfinishedBillByCompanyId(Integer company_id);

    //所有未接订单列表
    List<SysBill> selectAllUnBills();
}
