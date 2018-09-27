package com.example.transport.dao;

import com.example.transport.pojo.User;
import com.example.transport.pojo.WxUser;
import org.apache.ibatis.annotations.*;


/**
 * Created by WangZJ on 2018/8/13.
 */
@Mapper
public interface UserDao {
    @Select("select password from sys_user where loginname = #{loginname}")
    String getPasswordByUserName(@Param("loginname") String loginname);

    @Select("select * from sys_user where loginname = #{loginname}")
    User getUserByLoginName(@Param("loginname") String loginname);

    @Insert({"insert into wx_user(id,openid,nickname,gender,city,province,country,avatarurl,language,timestamp) values(0,#{openid},#{nickname},#{gender},#{city},#{province},#{country},#{avatarurl},#{language},#{timestamp})"})
    int insertWxUser(WxUser wxUser);

    @Select("select * from wx_user where openid = #{openid}")
    WxUser getWxUser(String openid);

    @Update({"update wx_user set nickname = #{nickname},gender = #{gender},city = #{city},province = #{province},country = #{country},avatarurl = #{avatarurl},language = #{language},timestamp = #{timestamp} where openid = #{openid}"})
    int updateWxUser(WxUser wxUser);
}
