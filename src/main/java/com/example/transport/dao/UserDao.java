package com.example.transport.dao;

import com.example.transport.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;



/**
 * Created by WangZJ on 2018/8/13.
 */
@Mapper
public interface UserDao {
    @Select("select password from sys_user where loginname = #{loginname}")
    String getPasswordByUserName(@Param("loginname") String loginname);

    @Select("select * from sys_user where loginname = #{loginname}")
    User getUserByLoginName(@Param("loginname") String loginname);
}
