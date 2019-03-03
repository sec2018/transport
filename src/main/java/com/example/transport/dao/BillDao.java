package com.example.transport.dao;

import com.example.transport.pojo.SysBill;
import com.example.transport.pojo.SysBillAndLine;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

@Mapper
public interface BillDao {

    //新增订单
    @Insert({"insert into sys_bill(id,bill_code,sender_id,sender_name,sender_tel,shop_id,shop_name,company_id,company_name,trans_id,trans_name,batch_code,bill_status,sender_lat,sender_lng,goodsname,goodsnum,billinfo,sender_procity,sender_detailarea,rec_name,rec_tel,rec_procity,rec_detailarea,price,company_code,create_time,line_id,pay_method,give_method,keepfee,waitnote) values(0,#{sysBill.bill_code},#{sysBill.sender_id},#{sysBill.sender_name},#{sysBill.sender_tel},#{sysBill.shop_id},#{sysBill.shop_name},#{sysBill.company_id},#{sysBill.company_name},#{sysBill.trans_id},#{sysBill.trans_name},#{sysBill.batch_code},#{sysBill.bill_status},#{sysBill.sender_lat},#{sysBill.sender_lng},#{sysBill.goodsname},#{sysBill.goodsnum},#{sysBill.billinfo},#{sysBill.sender_procity},#{sysBill.sender_detailarea},#{sysBill.rec_name},#{sysBill.rec_tel},#{sysBill.rec_procity},#{sysBill.rec_detailarea},#{sysBill.price},#{sysBill.company_code},#{sysBill.create_time},#{sysBill.line_id},#{sysBill.pay_method},#{sysBill.give_method},#{sysBill.keepfee},#{sysBill.waitnote})"})
    @Options(useGeneratedKeys = true,keyProperty = "sysBill.id")
    int insertBill(@Param("sysBill") SysBill sysBill);

    //根据sender_id来查询某用户下所有订单
    @Select({"select * from sys_bill where sender_id = #{sender_id} and batch_code != 1"})
    List<SysBill> selectUserBill(long sender_id);

    //根据id来查询特定运单
    @Select({"select * from sys_bill where id = #{id}"})
    SysBill selectSingleBill(long id);

    //商家根据id来查询未接运单
    @Select({"select * from sys_bill where id = #{id} and bill_status = 1"})
    SysBill SenderSelectSingleBill(long id);


    //根据经纬度,周围2公里来查询未接运单
    //SELECT * FROM sys_bill where (abs(sender_lng - 121.46754455) <= (2/111) and abs(sender_lat - 31.25249099) <= abs((2/111) /cos(121.46754455 *PI()/180)) and trans_id=-1 and batch_code != 1);
    @Select({"SELECT * FROM sys_bill where (abs(sender_lng - #{sender_lng}) <= (2/111) and abs(sender_lat - #{sender_lat}) <= abs((2/111) /cos(#{sender_lng} *PI()/180)) and trans_id=-1 and batch_code != 1 and bill_status = 1)"})
    List<SysBill> selectBillsByLnglat(@Param("sender_lng")String sender_lng,@Param("sender_lat")String sender_lat);  //经度lng,维度lat

    //所有未接订单列表
    @Select({"SELECT * FROM sys_bill where trans_id=-1 and batch_code != 1"})
    List<SysBill> selectAllUnBills();

    //商户更新订单内容
    @Update({"update sys_bill set sender_name = #{sender_name},sender_tel = #{sender_tel},shop_id=#{shop_id},shop_name=#{shop_name},company_id=#{company_id},company_name=#{company_name},goodsname=#{goodsname},goodsnum=#{goodsnum},batch_code=#{batch_code},sender_lat=#{sender_lat},sender_lng=#{sender_lng},billinfo=#{billinfo},sender_procity=#{sender_procity},sender_detailarea=#{sender_detailarea},rec_name=#{rec_name},rec_tel=#{rec_tel},rec_procity=#{rec_procity},rec_detailarea=#{rec_detailarea},price=#{price},line_id=#{line_id},pay_method=#{pay_method},give_method=#{give_method},keepfee=#{keepfee},waitnote=#{waitnote} where id = #{id}"})
    int SenderUpdateBill(SysBill sysBill);

    //支付订单
    @Update({"update sys_bill set bill_status = 3,pay_time=#{pay_time} where id = #{id}"})
    int payBill(@Param("pay_time")Date pay_time, @Param("id")long id);

    //支付回调订单
    @Update({"update sys_bill set bill_status = 3,pay_time=#{pay_time},out_trade_no=#{out_trade_no},transaction_id=#{transaction_id} where id = #{id}"})
    int payNotifyBill(Date pay_time, long id, String out_trade_no, String transaction_id);

    //完成订单
    @Update({"update sys_bill set bill_status = 4,finish_time=#{finish_time},company_code=#{company_code},delivery_fee=#{delivery_fee} where id = #{id}"})
    int finishBill(@Param("finish_time")Date finish_time,@Param("id")long id,@Param("company_code")String company_code,@Param("delivery_fee")double delivery_fee);

    //删除所下未接订单，硬删除
    //@Delete({"delete from sys_bill where id = #{id} and sender_id = #{sender_id} and trans_id = -1"})
    @Delete({"delete from sys_bill where id = #{id}"})
    int deleteSenderUnRecBill(@Param("id")long id,@Param("sender_id")long sender_id);

    //软删除，对已完成的订单，把承运员设置为不可见
    //@Update({"update sys_bill set trans_id=-1 where id = #{id} and trans_id = #{trans_id} and bill_status = 4"})
    @Update({"update sys_bill set trans_id=-1 where id = #{id}"})
    int deleteTransBill(@Param("id")long id,@Param("trans_id")long trans_id);

    //软删除，对已完成的订单，把商户设置为不可见
    //@Update({"update sys_bill set sender_id=-1 where id = #{id} and sender_id = #{sender_id} and bill_status = 4"})
    @Update({"update sys_bill set sender_id=-1 where id = #{id}"})
    int deleteSenderBill(@Param("id")long id,@Param("sender_id")long sender_id);

    //软删除，对已完成的订单，把物流公司设置为不可见
    //@Update({"update sys_bill set company_id=-1 where id = #{id} and company_id = #{company_id} and bill_status = 4"})
    @Update({"update sys_bill set company_id=-1 where id = #{id}"})
    int deleteCompanyBill(@Param("id")long id,@Param("company_id")long company_id);

    //软删除，对已接的订单，承运员取消接单
    //@Update({"update sys_bill set trans_id=-1，bill_status = 1 where id = #{id} and trans_id = #{trans_id} and bill_status = 2"})
    @Update({"update sys_bill set trans_id=-1，bill_status = 1 where id = #{id}"})
    int cancelTransBill(@Param("id")long id,@Param("trans_id")long trans_id);


    //商家根据sender_id和状态bill_status = 1，2 或 3 来查询未完成订单
//    @Select({"select * from sys_bill where sender_id = #{sender_id} and (bill_status = 1 or bill_status = 2 or bill_status = 3) and batch_code!=1"})
    @Select({"select * from sys_bill as a inner join company_lines as b on a.sender_id = #{sender_id} and (a.bill_status = 1 or a.bill_status = 2 or a.bill_status = 3) and a.batch_code!=1 and a.line_id = b.id order by pay_time desc, rec_time desc, create_time desc"})
//    List<SysBill>  selectUnfinishBill(@Param("sender_id")long sender_id);
    List<SysBillAndLine>  selectUnfinishBill(@Param("sender_id")long sender_id);

    //商家根据sender_id和状态bill_status = 4来查询已完成订单
//    @Select({"select * from sys_bill where sender_id = #{sender_id} and bill_status = 4"})
    @Select({"select * from sys_bill as a inner join company_lines as b on a.sender_id = #{sender_id} and a.bill_status = 4 and a.line_id = b.id order by finish_time desc"})
    List<SysBill>  selectfinishedBill(@Param("sender_id")long sender_id);


    //承运员根据名称和电话查询所有已完成订单
    @Select({"select * from sys_bill where trans_id = #{wxuserid} and (rec_name like CONCAT('%',#{sender_param},'%') or rec_tel like CONCAT('%',#{sender_param},'%')) and bill_status = 4"})
    List<SysBill>  selectTransFinishBillByTelOrName(@Param("wxuserid")long wxuserid,@Param("sender_param")String sender_param);

    //承运员根据名称和电话查询所有未完成订单
    @Select({"select * from sys_bill where trans_id = #{wxuserid} and (rec_name like CONCAT('%',#{sender_param},'%') or rec_tel like CONCAT('%',#{sender_param},'%')) and bill_status != 4"})
    List<SysBill>  selectTransUnfinishBillByTelOrName(@Param("wxuserid")long wxuserid,@Param("sender_param")String sender_param);

    //商户根据名称和电话查询所有已完成订单
    @Select({"select * from sys_bill where sender_id = #{wxuserid} and (rec_name like CONCAT('%',#{sender_param},'%') or rec_tel like CONCAT('%',#{sender_param},'%')) and bill_status = 4"})
    List<SysBill>  selectShoperFinishBillByTelOrName(@Param("wxuserid")long wxuserid,@Param("sender_param")String sender_param);

    //商户根据名称和电话查询所有未完成订单
    @Select({"select * from sys_bill where sender_id = #{wxuserid} and (rec_name like CONCAT('%',#{sender_param},'%') or rec_tel like CONCAT('%',#{sender_param},'%')) and bill_status != 4"})
    List<SysBill>  selectShoperUnfinishBillByTelOrName(@Param("wxuserid")long wxuserid,@Param("sender_param")String sender_param);

    //物流公司根据名称和电话查询所有已完成订单
    @Select({"select * from sys_bill where company_id = #{wxuserid} and (rec_name like CONCAT('%',#{sender_param},'%') or rec_tel like CONCAT('%',#{sender_param},'%')) and bill_status = 4"})
    List<SysBill>  selectCompanyFinishBillByTelOrName(@Param("wxuserid")long wxuserid,@Param("sender_param")String sender_param);

    //物流公司根据名称和电话查询所有未完成订单
    @Select({"select * from sys_bill where company_id = #{wxuserid} and (rec_name like CONCAT('%',#{sender_param},'%') or rec_tel like CONCAT('%',#{sender_param},'%')) and bill_status != 4"})
    List<SysBill>  selectCompanyUnfinishBillByTelOrName(@Param("wxuserid")long wxuserid,@Param("sender_param")String sender_param);

    //接单
    @Update({"update sys_bill set trans_id = #{trans_id},rec_time=#{datetime},bill_status = 2,trans_name=#{trans_name} where id = #{id}"})
    int updateBillSetTrans_id(@Param("id")long id,@Param("datetime")Date datetime, @Param("trans_id")long trans_id,@Param("trans_name")String trans_name);

    //物流公司查询本公司所有已完成订单
    @Select({"select * from sys_bill where company_id = #{company_id} and bill_status = 4  order by finish_time desc"})
    List<SysBill>  selectfinishedBillByCompanyId(@Param("company_id")Integer company_id);

    //物流公司查询本公司所有未完成订单
    @Select({"select * from sys_bill where company_id = #{company_id} and bill_status != 4 order by pay_time desc, rec_time desc, create_time desc"})
    List<SysBill>  selectunfinishedBillByCompanyId(@Param("company_id")Integer company_id);


    //承运员查询未完成订单
    @Select({"select * from sys_bill where trans_id = #{trans_id} and bill_status != 4 order by pay_time desc, rec_time desc, create_time desc"})
    List<SysBill>  selectunfinishedBillByTransId(@Param("trans_id")long trans_id);

    //承运员查询已完成订单
    @Select({"select * from sys_bill where trans_id = #{trans_id} and bill_status = 4 order by finish_time desc"})
    List<SysBill>  selectfinishedBillByTransId(@Param("trans_id")long trans_id);

    //商户查询所保存批量未下订单
    @Select({"select * from sys_bill where sender_id = #{sender_id} and batch_code = 1  and bill_status != 4"})
    List<SysBill>  selectBatchBills(@Param("sender_id")long sender_id);

    //商户保存提交所下批量订单
    @Update({"update sys_bill set batch_code=#{batch_code} where sender_id = #{sender_id} and batch_code='1'"})
    int updateBatchBillsCode(@Param("sender_id")long sender_id,@Param("batch_code")String batch_code);

    //管理员查看所有未完成订单
    @Select({"select * from sys_bill where bill_status != 4 order by pay_time desc, rec_time desc, create_time desc"})
    List<SysBill> adminSelectunfinishedBill();

    //管理员查看所有已完成订单
    @Select({"select * from sys_bill where bill_status = 4  order by finish_time desc"})
    List<SysBill> adminSelectfinishedBill();

    //退款回调订单
    @Update({"update sys_bill set refund_time=#{refund_time},refundstatus=#{refundstatus},refundcode=#{refundcode} where out_trade_no = #{out_trade_no}"})
    int refundBill(Date refund_time, String out_trade_no, int refundstatus, String refundcode);

}
