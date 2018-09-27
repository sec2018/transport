package com.example.transport.dao;

import com.example.transport.pojo.SysBill;
import com.example.transport.pojo.WxUser;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BillDao {

    @Insert({"insert into sys_bill(id,bill_code,sender_id,sender_name,send_tel,shop_name,company_id,company_name,trans_id,trans_name,batch_code,bill_status) values(0,#{bill_code},#{sender_id},#{sender_name},#{send_tel},#{shop_name},#{company_id},#{company_name},#{trans_id},#{trans_name},#{batch_code},#{bill_status})"})
    int insertBill(SysBill sysBill);

}
