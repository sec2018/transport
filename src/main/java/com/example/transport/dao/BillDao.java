package com.example.transport.dao;

import com.example.transport.pojo.SysBill;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BillDao {

    //新增订单
    @Insert({"insert into sys_bill(id,bill_code,sender_id,sender_name,sender_tel,shop_name,company_id,company_name,trans_id,trans_name,batch_code,bill_status) values(0,#{bill_code},#{sender_id},#{sender_name},#{sender_tel},#{shop_name},#{company_id},#{company_name},#{trans_id},#{trans_name},#{batch_code},#{bill_status})"})
    int insertBill(SysBill sysBill);

    //根据sender_id来查询某用户下所有订单
    @Select({"select * from sys_bill where sender_id = #{sender_id}"})
    List<SysBill> selectUserBill(long sender_id);

    //根据id来查询特定订单
    @Select({"select * from sys_bill where id = #{id}"})
    SysBill selectSingleBill(long id);

    //更新订单内容
    @Update({"update sys_bill set sender_tel = #{sender_tel},shop_name=#{shop_name},company_id=#{company_id},company_name=#{company_name},trans_id=#{trans_id},trans_name=#{trans_name},batch_code=#{batch_code},bill_status=#{bill_status} where id = #{id}"})
    int updateBill(SysBill sysBill);

    //更新订单状态
    @Update({"update sys_bill set bill_status = #{bill_status} where id = #{id}"})
    int updateBillStatus(@Param("bill_status")int bill_status,@Param("id")long id);

    //删除订单
    @Delete({"delete from sys_bill where id = #{id}"})
    int deleteBill(long id);
}
