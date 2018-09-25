package com.example.transport.dao;

import com.example.transport.pojo.UserToken;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface SysUserTokenDao {

    @Select("select * from user_token where token = #{token}")
    UserToken queryByToken(String token);

    @Select("select * from user_token where user_id = #{user_id}")
    UserToken selectById(long user_id);

    @Insert({"insert into user_token(user_id,token,update_time,expire_time) values(#{user_id},#{token},#{update_time},#{expire_time})"})
    int insertToken(UserToken userToken);

    @Update({"update user_token set token = #{token},update_time=#{update_time},expire_time=#{expire_time} where user_id=#{user_id}"})
    int updateToken(UserToken userToken);
}
