package com.example.transport.dao;

import com.example.transport.model.SysShopExample;
import com.example.transport.pojo.SysShop;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SysShopMapper {
    int countByExample(SysShopExample example);

    int deleteByExample(SysShopExample example);

    int deleteByPrimaryKey(Integer shopId);

    int insert(SysShop record);

    int insertSelective(SysShop record);

    List<SysShop> selectByExample(SysShopExample example);

    SysShop selectByPrimaryKey(Integer shopId);

    int updateByExampleSelective(@Param("record") SysShop record, @Param("example") SysShopExample example);

    int updateByExample(@Param("record") SysShop record, @Param("example") SysShopExample example);

    int updateByPrimaryKeySelective(SysShop record);

    int updateByPrimaryKey(SysShop record);

    @Select("select * from sys_shop where wxuser_id = #{wxuser_id}")
    SysShop selectByWxuserid(long wxuser_id);
}