package com.example.transport.dao;

import com.example.transport.model.CompanyLinesExample;
import com.example.transport.pojo.CompanyLines;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CompanyLinesMapper {
    int countByExample(CompanyLinesExample example);

    int deleteByExample(CompanyLinesExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(CompanyLines record);

    int insertSelective(CompanyLines record);

    List<CompanyLines> selectByExample(CompanyLinesExample example);

    CompanyLines selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") CompanyLines record, @Param("example") CompanyLinesExample example);

    int updateByExample(@Param("record") CompanyLines record, @Param("example") CompanyLinesExample example);

    int updateByPrimaryKeySelective(CompanyLines record);

    int updateByPrimaryKey(CompanyLines record);

    int insertLineList(List<CompanyLines> list);

    @Delete({"delete from company_lines where company_id = #{company_id}"})
    int deleteByCompanyId(@Param("company_id")Integer company_id);
}