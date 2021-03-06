package com.example.transport.dao;

import com.example.transport.model.SysCompanyExample;
import com.example.transport.pojo.SysCompany;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SysCompanyMapper {
    int countByExample(SysCompanyExample example);

    int deleteByExample(SysCompanyExample example);

    int deleteByPrimaryKey(Integer companyId);

    int insert(SysCompany record);

    int insertSelective(SysCompany record);

    List<SysCompany> selectByExample(SysCompanyExample example);

    SysCompany selectByPrimaryKey(Integer companyId);

    int updateByExampleSelective(@Param("record") SysCompany record, @Param("example") SysCompanyExample example);

    int updateByExample(@Param("record") SysCompany record, @Param("example") SysCompanyExample example);

    int updateByPrimaryKeySelective(SysCompany record);

    int updateByPrimaryKey(SysCompany record);

    int selectCompanyIdbyWxuserid(Long wxuser_id);

    SysCompany selectByWxuserid(long wxuser_id);

    @Select({"select company_id from sys_company where companycheckstatus = 1"})
    List<Integer>  selectCheckIdList();
}