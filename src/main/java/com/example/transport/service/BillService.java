package com.example.transport.service;


import com.example.transport.pojo.SysBill;
import com.example.transport.pojo.SysBillAndLine;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface BillService {

    boolean insertBill(SysBill sysBill);

    //根据sender_id来查询某用户下所有订单
    List<SysBill> selectUserBill(long sender_id);

    //根据id来查询特定订单
    SysBill selectSingleBill(long id);

    //商户更新订单内容
    boolean SenderUpdateBill(SysBill sysBill);

    //支付订单
    boolean payBill(Date pay_time, long id);

    //完成订单
    boolean finishBill(Date finish_time,long id,String company_code);

    //删除订单,硬删除
    boolean deleteSenderUnRecBill(long id,long wxuserid);

    //软删除，对已完成的订单，把承运员设置为不可见
    boolean deleteTransBill(long id,long trans_id);

    //软删除，对已完成的订单，把商户设置为不可见
    boolean deleteSenderBill(long id,long sender_id);

    //软删除，对已完成的订单，把物流公司设置为不可见
    boolean deleteCompanyBill(long id,long company_id);

    //软删除，对已接的订单，承运员取消接单
    boolean cancelTransBill(long id,long trans_id);

    //商家根据sender_id和状态bill_status 来查询未完成订单
    List<SysBillAndLine>  selectUnfinishBill(long sender_id);

    //商家根据sender_id和状态bill_status 来查询未完成订单(分页)
    Map<String, Object> selectUnfinishBill(long sender_id, int startitem, int pagesize) throws Exception;

    //商家根据sender_id和状态bill_status = 4来查询已完成订单
    List<SysBill>  selectfinishedBill(long sender_id);

    //商家根据sender_id和状态bill_status 来查询已完成订单(分页)
    Map<String, Object> selectfinishedBill(long sender_id, int startitem, int pagesize) throws Exception;

    //承运员根据名称和电话查询所有已完成订单
    Map<String, Object>  selectTransFinishBillByTelOrName(long wxuserid,String sender_param,int startitem,int pagesize);

    //承运员根据名称和电话查询所有未完成订单
    Map<String, Object>  selectTransUnfinishBillByTelOrName(long wxuserid,String sender_param,int startitem,int pagesize);

    //商户根据名称和电话查询所有已完成订单
    Map<String, Object>  selectShoperFinishBillByTelOrName(long wxuserid,String sender_param,int startitem,int pagesize);

    //商户根据名称和电话查询所有未完成订单
    Map<String, Object>  selectShoperUnfinishBillByTelOrName(long wxuserid,String sender_param,int startitem,int pagesize);

    //物流公司根据名称和电话查询所有已完成订单
    Map<String, Object>  selectCompanyFinishBillByTelOrName(long wxuserid,String sender_param,int startitem,int pagesize);

    //物流公司根据名称和电话查询所有未完成订单
    Map<String, Object>  selectCompanyUnfinishBillByTelOrName(long wxuserid,String sender_param,int startitem,int pagesize);

    //接单
    boolean updateBillSetTrans_id(long id, Date datetime, long trans_id,String trans_name);

    //根据经纬度得到未接订单
    List<SysBill> selectBillsByLnglat(String lng,String lat);  //经度lng,维度lat

    List<SysBill> selectBillsIn2Mills(String sender_lng, String sender_lat);

    //物流公司查询本公司所有已完成订单
    List<SysBill>  selectfinishedBillByCompanyId(Integer company_id);

    //物流公司查询本公司所有已完成订单（分页）
    Map<String, Object>  selectfinishedBillByCompanyId(Integer company_id,int startitem,int pagesize) throws Exception;

    //物流公司查询本公司所有未完成订单
    List<SysBill>  selectunfinishedBillByCompanyId(Integer company_id);

    //物流公司查询本公司所有未完成订单（分页）
    Map<String, Object>  selectunfinishedBillByCompanyId(Integer company_id,int startitem,int pagesize) throws Exception;

    //所有未接订单列表
    List<SysBill> selectAllUnBills();

    //承运员查询未完成订单
    List<SysBill>  selectunfinishedBillByTransId(long trans_id);

    //承运员查询未完成订单(分页)
    Map<String, Object>  selectunfinishedBillByTransId(long trans_id,int startitem,int pagesize) throws Exception;

    //承运员查询已完成订单
    List<SysBill>  selectfinishedBillByTransId(long trans_id);

    //承运员查询已完成订单(分页)
    Map<String, Object>  selectfinishedBillByTransId(long trans_id,int startitem,int pagesize) throws Exception;

    //商户查询所下批量订单
    List<SysBill>  selectBatchBills(long sender_id);

    //商户查询所下批量订单(分页)
    List<SysBill>  selectBatchBills(long sender_id,int startitem,int pagesize) throws Exception;

    //商户保存提交所下批量订单
    boolean updateBatchBillsCode(long sender_id,String batch_code);

    //管理员查看所有未完成订单
    Map<String, Object> adminSelectunfinishedBill(int startitem,int pagesize);

    //管理员查看所有未完成订单
    Map<String, Object> adminSelectfinishedBill(int startitem,int pagesize);
}
