package com.example.transport.dao;

import com.example.transport.pojo.CompanyBill;

import java.util.List;

import org.apache.ibatis.annotations.*;

@Mapper
public interface CompanyBillMapper {

    int deleteByPrimaryKey(Integer id);

    @Insert({"insert into company_bill(id,bill_code,company_billcode,company_id,shop_name,trans_id,bill_status,company_lat,company_lng,goodsname,goodsnum,billinfo,company_procity,company_detailarea,rec_name,rec_tel,rec_procity,rec_detailarea,price,create_time) values(0,#{companyBill.bill_code},#{companyBill.company_billcode},#{companyBill.company_id},#{companyBill.shop_name},#{companyBill.trans_id},#{companyBill.bill_status},#{companyBill.company_lat},#{companyBill.company_lng},#{companyBill.goodsname},#{companyBill.goodsnum},#{companyBill.billinfo},#{companyBill.company_procity},#{companyBill.company_detailarea},#{companyBill.rec_name},#{companyBill.rec_tel},#{companyBill.rec_procity},#{companyBill.rec_detailarea},#{companyBill.price},#{companyBill.create_time})"})
    @Options(useGeneratedKeys = true,keyProperty = "companyBill.id")
    int insert(@Param("companyBill")CompanyBill companyBill);

    @Update({"update company_bill set companybill_code = #{companybill_code},company_id = #{company_id},shop_name = #{shop_name},trans_id=#{trans_id},bill_status=#{bill_status},company_lat=#{company_lat},company_lng=#{company_lng},goodsname=#{goodsname},goodsnum=#{goodsnum},billinfo=#{billinfo},company_procity=#{company_procity},company_detailarea=#{company_detailarea},rec_name=#{rec_name},rec_tel=#{rec_tel},rec_procity=#{rec_procity},rec_detailarea=#{rec_detailarea},price=#{price},create_time=#{create_time} where id=#{id})"})
    int updateCompanyBill(CompanyBill companyBill);

    @Select({"select * from company_bill where id = #{id}"})
    CompanyBill selectByPrimaryKey(Integer id);

    @Select({"select * from company_bill where (bill_status = 1 or bill_status = 2 or bill_status = 3) order by rec_time desc, create_time desc"})
    List<CompanyBill> selectAllCompanyUnBills();

    //管理员查看所有未完成订单
    @Select({"select * from company_bill where bill_status != 4 order by  rec_time desc, create_time desc"})
    List<CompanyBill> adminSelectCompanyUnfinishedBill();

    //管理员查看所有已完成订单
    @Select({"select * from company_bill where bill_status = 4  order by finish_time desc"})
    List<CompanyBill> adminSelectCompanyfinishedBill();

    //承运员查询未完成订单
    @Select({"select * from company_bill where trans_id = #{trans_id} and bill_status != 4 order by rec_time desc, create_time desc"})
    List<CompanyBill>  selectCompanyUnfinishedBillByTransId(@Param("trans_id")long trans_id) throws Exception;

    //承运员查询已完成订单
    @Select({"select * from company_bill where trans_id = #{trans_id} and bill_status = 4 order by finish_time desc"})
    List<CompanyBill>  selectCompanyfinishedBillByTransId(@Param("trans_id")long trans_id) throws Exception;

    //物流公司查询本公司所有已完成订单
    @Select({"select * from company_bill where company_id = #{company_id} and bill_status = 4  order by finish_time desc"})
    List<CompanyBill>  selectCompanyfinishedBillByCompanyId(@Param("company_id")Integer company_id) throws Exception;

    //物流公司查询本公司所有未完成订单
    @Select({"select * from company_bill where company_id = #{company_id} and bill_status != 4 order by rec_time desc, create_time desc"})
    List<CompanyBill>  selectCompanyUnfinishedBillByCompanyId(@Param("company_id")Integer company_id) throws Exception;

    //承运员根据名称和电话查询所有已完成订单
    @Select({"select * from company_bill where trans_id = #{wxuserid} and (rec_name like CONCAT('%',#{sender_param},'%') or rec_tel like CONCAT('%',#{sender_param},'%')) and bill_status = 4"})
    List<CompanyBill>  selectTransFinishCompanyBillByTelOrName(@Param("wxuserid")long wxuserid,@Param("sender_param")String sender_param);

    //承运员根据名称和电话查询所有未完成订单
    @Select({"select * from company_bill where trans_id = #{wxuserid} and (rec_name like CONCAT('%',#{sender_param},'%') or rec_tel like CONCAT('%',#{sender_param},'%')) and bill_status != 4"})
    List<CompanyBill>  selectTransUnfinishCompanyBillByTelOrName(@Param("wxuserid")long wxuserid,@Param("sender_param")String sender_param);


    //物流公司根据名称和电话查询所有已完成订单
    @Select({"select * from company_bill where company_id = #{company_id} and (rec_name like CONCAT('%',#{sender_param},'%') or rec_tel like CONCAT('%',#{sender_param},'%')) and bill_status = 4"})
    List<CompanyBill>  selectFinishCompanyBillByTelOrName(@Param("company_id")int company_id,@Param("sender_param")String sender_param);

    //物流公司根据名称和电话查询所有未完成订单
    @Select({"select * from company_bill where company_id = #{company_id} and (rec_name like CONCAT('%',#{sender_param},'%') or rec_tel like CONCAT('%',#{sender_param},'%')) and bill_status != 4"})
    List<CompanyBill>  selectUnfinishCompanyBillByTelOrName(@Param("company_id")int company_id,@Param("sender_param")String sender_param);


    //承运员查询未完成订单
    @Select({"select * from company_bill where trans_id = #{trans_id} and bill_status != 4 order by rec_time desc, create_time desc"})
    List<CompanyBill> selectunfinishedCompanyBillByTransId(@Param("trans_id")long trans_id);

    //承运员查询已完成订单
    @Select({"select * from company_bill where trans_id = #{trans_id} and bill_status = 4 order by finish_time desc"})
    List<CompanyBill>  selectfinishedCompanyBillByTransId(@Param("trans_id")long trans_id);

    //物流公司查询本公司所有已完成订单
    @Select({"select * from company_bill where company_id = #{company_id} and bill_status = 4  order by finish_time desc"})
    List<CompanyBill>  selectfinishedCompanyBillByCompanyId(@Param("company_id")Integer company_id);

    //物流公司查询本公司所有未完成订单
    @Select({"select * from company_bill where company_id = #{company_id} and bill_status != 4 order by rec_time desc, create_time desc"})
    List<CompanyBill>  selectunfinishedCompanyBillByCompanyId(@Param("company_id")Integer company_id);
}