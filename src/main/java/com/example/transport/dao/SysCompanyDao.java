package com.example.transport.dao;

import com.example.transport.pojo.SysCompany;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SysCompanyDao {

    //新增物流公司
    @Insert({"insert into sys_company(company_id,company_name) values(0,#{company_name})"})
    int insertCompany(SysCompany sysCompany);

    //获取物流公司列表
    @Select({"select * from sys_company"})
    List<SysCompany> getCompanies();
}
