package com.example.transport.dao;

import com.example.transport.pojo.User;
import com.example.transport.pojo.UserToken;
import com.example.transport.pojo.WxUser;
import org.apache.ibatis.annotations.Insert;
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

    @Insert({"insert into wx_user(id,openid,nickname,gender,city,province,country,avatarurl,unionid,timestamp) values(#{id},#{openid},#{nickname},#{gender},#{city},#{province},#{country},#{avatarurl},#{unionid},#{timestamp})"})
    int insertWxUser(WxUser wxUser);

}
