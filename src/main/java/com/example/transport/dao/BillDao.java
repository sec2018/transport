package com.example.transport.dao;

import com.example.transport.pojo.SysBill;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BillDao {

    //新增订单
    @Insert({"insert into sys_bill(id,bill_code,sender_id,sender_name,sender_tel,shop_name,company_id,company_name,trans_id,trans_name,batch_code,bill_status,sender_lat,sender_lng,goodsname,goodsnum,billinfo,sender_procity,sender_detailarea,rec_name,rec_tel,rec_procity,rec_detailarea) values(0,#{bill_code},#{sender_id},#{sender_name},#{sender_tel},#{shop_name},#{company_id},#{company_name},#{trans_id},#{trans_name},#{batch_code},#{bill_status},#{sender_lat},#{sender_lng},#{goodsname},#{goodsnum},#{billinfo},#{sender_procity},#{sender_detailarea},#{rec_name},#{rec_tel},#{rec_procity},#{rec_detailarea})"})
    int insertBill(SysBill sysBill);

    //根据sender_id来查询某用户下所有订单
    @Select({"select * from sys_bill where sender_id = #{sender_id} and batch_code != 1"})
    List<SysBill> selectUserBill(long sender_id);

    //根据id来查询特定运单
    @Select({"select * from sys_bill where id = #{id}"})
    SysBill selectSingleBill(long id);

    //根据经纬度,周围2公里来查询未接运单
    //SELECT * FROM transport.sys_bill where (abs(sender_lng - 121.34500) <= (10/111) and abs(sender_lat - 30.34300) <= (10/111) /cos(121.34500 *PI()/180));
    @Select({"SELECT * FROM sys_bill where (abs(sender_lng - lng) <= (2/111) and abs(sender_lat - lat) <= (2/111) /cos(lng *PI()/180) and trans_id=-1 and batch_code != 1)"})
    List<SysBill> selectBillsByLnglat(String lng,String lat);  //经度lng,维度lat

    //所有未接订单列表
    @Select({"SELECT * FROM sys_bill where trans_id=-1 and batch_code != 1)"})
    List<SysBill> selectAllUnBills();

    //更新订单内容
    @Update({"update sys_bill set sender_tel = #{sender_tel},shop_name=#{shop_name},company_id=#{company_id},company_name=#{company_name},trans_id=#{trans_id},trans_name=#{trans_name},batch_code=#{batch_code},bill_status=#{bill_status} where id = #{id}"})
    int updateBill(SysBill sysBill);

    //更新订单状态
    @Update({"update sys_bill set bill_status = #{bill_status} where id = #{id} and batch_code != 1"})
    int updateBillStatus(@Param("bill_status")int bill_status,@Param("id")long id);

    //删除订单
    @Delete({"delete from sys_bill where id = #{id}"})
    int deleteBill(long id);

    //商家根据sender_id和状态bill_status = 1，2 或 3 来查询未完成订单
    @Select({"select * from sys_bill where sender_id = #{sender_id} and (bill_status = 1 or bill_status = 2 or bill_status = 3) and batch_code != 1"})
    List<SysBill>  selectUnfinishBill(@Param("sender_id")long sender_id);

    //商家根据sender_id和状态bill_status = 4来查询已完成订单
    @Select({"select * from sys_bill where sender_id = #{sender_id} and bill_status = 4"})
    List<SysBill>  selectfinishedBill(@Param("sender_id")long sender_id);


    //承运员根据名称和电话查询所有已完成订单
    @Select({"select * from sys_bill where (sender_name = #{sender_param} or sender_tel = #{sender_param}) and bill_status = 4"})
    List<SysBill>  selectUnfinishBillByTelOrName(@Param("sender_param")String sender_param);

    //接单
    @Update({"update sys_bill set trans_id = #{trans_id},bill_status = #{bill_status} where id = #{id}"})
    int updateBillSetTrans_id(@Param("id")long id,@Param("bill_status")int bill_status,@Param("trans_id")long trans_id);

    //物流公司查询本公司所有已完成订单
    @Select({"select * from sys_bill where company_id = #{company_id} and bill_status = 4"})
    List<SysBill>  selectfinishedBillByCompanyId(@Param("company_id")Integer company_id);

    //物流公司查询本公司所有未完成订单
    @Select({"select * from sys_bill where company_id = #{company_id} and bill_status != 4 and batch_code != 1"})
    List<SysBill>  selectunfinishedBillByCompanyId(@Param("company_id")Integer company_id);



}
