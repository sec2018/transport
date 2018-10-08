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
}
