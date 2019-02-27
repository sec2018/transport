package com.example.transport.dao;

import com.example.transport.pojo.SysUserAddr;
import com.example.transport.pojo.UserToken;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by WangZJ on 2018/10/7.
 */
@Mapper
public interface SysUserAddrDao {

    @Insert({"insert into sys_user_addr(id,wxuser_id,tel,pro_city,detail_addr,uname,isdefault,addrrole) values(0,#{wxuser_id},#{tel},#{pro_city},#{detail_addr},#{uname},#{isdefault},#{addrrole})"})
    int insertSysUserAddr(SysUserAddr sysUserAddr);

    @Select("select count(*) from sys_user_addr where wxuser_id = #{wxuserid}")
    int getAddrCount(long wxuserid);

    @Select("select * from sys_user_addr where wxuser_id = #{wxuserid} and addrrole = #{addrrole}")
    List<SysUserAddr> getAddrList(@Param("wxuserid")long wxuserid,@Param("addrrole")int addrrole);

    @Select("select * from sys_user_addr where id = #{id}")
    SysUserAddr getAddrById(long id);

    @Update({"update sys_user_addr set tel = #{tel},pro_city=#{pro_city},detail_addr=#{detail_addr},uname=#{uname},isdefault=#{isdefault} where id=#{id}"})
    int updateSysUserAddr(SysUserAddr sysUserAddr);

    @Delete("delete from sys_user_addr where id = #{id}")
    int deleteAddrById(long id);

    //把原默认地址置为非默认
    @Update({"update sys_user_addr set isdefault=0 where wxuser_id = #{wxuserid} and isdefault=1"})
    int updateSysUserAddrDefault(long wxuserid);
}
