package com.example.transport.dao;

import com.example.transport.pojo.SysTran;
import com.example.transport.model.SysTranExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SysTranMapper {
    int countByExample(SysTranExample example);

    int deleteByExample(SysTranExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(SysTran record);

    int insertSelective(SysTran record);

    List<SysTran> selectByExample(SysTranExample example);

    SysTran selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") SysTran record, @Param("example") SysTranExample example);

    int updateByExample(@Param("record") SysTran record, @Param("example") SysTranExample example);

    int updateByPrimaryKeySelective(SysTran record);

    int updateByPrimaryKey(SysTran record);

    @Select("select * from sys_tran where wxuser_id = #{wxuser_id}")
    SysTran selectByWxuserid(long wxuser_id);
}