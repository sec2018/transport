package com.example.transport.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.transport.pojo.SysUserAddr;
import com.example.transport.service.SysUserAddrService;
import com.example.transport.service.UserService;
import com.example.transport.util.R;
import com.example.transport.util.redis.RedisService;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by WangZJ on 2018/10/7.
 */
@RequestMapping("api")
@Controller
public class SysUserAddrController {

    @Autowired
    private SysUserAddrService sysUserAddrService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private UserService userService;


    @RequestMapping(value = "addaddr",method = RequestMethod.POST)
    @ResponseBody
    public R insertSysUserAddr(@RequestParam(value="uname") String uname, @RequestParam(value="tel")String tel, @RequestParam(value="pro_city")String pro_city,
                       @RequestParam(value="detail_addr")String detail_addr, @RequestParam(value="isdefault")int isdefault, HttpServletRequest request){
        String token = request.getHeader("token");

        R r = new R();
        String tokenvalue = null;
        try {
            tokenvalue = redisService.get(token);
        } catch (Exception e) {
            return r.error(1003,"服务超时");
        }
        if(tokenvalue != null){
            redisService.expire(token, Expiration.seconds(1800).getExpirationTime());
            String openid = tokenvalue.split("\\|")[0];
            String session_key = tokenvalue.split("\\|")[1];
            //获取当前微信用户id
            long wxuserid = userService.getWxUserId(openid);
            //判断用户已存在地址数是否大于10
            if(sysUserAddrService.getAddrCount(wxuserid)>=10){
                return r.error(1001,"最多存放10个收货地址");
            }

            SysUserAddr sysUserAddr = new SysUserAddr();
            sysUserAddr.setUname(uname);
            sysUserAddr.setTel(tel);
            sysUserAddr.setPro_city(pro_city);
            sysUserAddr.setWxuser_id(wxuserid);
            sysUserAddr.setDetail_addr(detail_addr);
            sysUserAddr.setIsdefault(isdefault==1?1:0);

            boolean flag = sysUserAddrService.insertSysUserAddr(sysUserAddr);
            if(flag){
                return r.ok().put("code",1000).put("msg","保存地址成功！");
            }

        }else{
            return r.error(1002,"当前登录已失效，请先登录！");
        }
        return r;
    }

    @RequestMapping(value = "searchaddr")
    @ResponseBody
    public R searchSysUserAddr(HttpServletRequest request){
        String token = request.getHeader("token");
        R r = new R();
        String tokenvalue = null;
        try {
            tokenvalue = redisService.get(token);
        } catch (Exception e) {
            return r.error(1003,"服务超时");
        }
        if(tokenvalue != null){
            redisService.expire(token, Expiration.seconds(1800).getExpirationTime());
            String openid = tokenvalue.split("\\|")[0];
            String session_key = tokenvalue.split("\\|")[1];
            //获取当前微信用户id
            long wxuserid = userService.getWxUserId(openid);
            List<SysUserAddr> addrList = sysUserAddrService.getAddrList(wxuserid);
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("data",addrList);
            return R.ok(map).put("code",1000);
        }else{
            return r.error(1002,"当前登录已失效，请先登录！");
        }
    }

    @RequestMapping(value = "updateaddr",method = RequestMethod.POST)
    @ResponseBody
    public R insertSysUserAddr(@RequestParam(value="id") long id,@RequestParam(value="uname") String uname, @RequestParam(value="tel")String tel, @RequestParam(value="pro_city")String pro_city,
                               @RequestParam(value="detail_addr")String detail_addr, @RequestParam(value="isdefault")int isdefault, HttpServletRequest request){
        String token = request.getHeader("token");

        R r = new R();
        String tokenvalue = null;
        try {
            tokenvalue = redisService.get(token);
        } catch (Exception e) {
            return r.error(1003,"服务超时");
        }
        if(tokenvalue != null){
            redisService.expire(token, Expiration.seconds(1800).getExpirationTime());
            String openid = tokenvalue.split("\\|")[0];
            String session_key = tokenvalue.split("\\|")[1];

            SysUserAddr sysUserAddr = sysUserAddrService.getAddrById(id);
            sysUserAddr.setUname(uname);
            sysUserAddr.setTel(tel);
            sysUserAddr.setPro_city(pro_city);
            sysUserAddr.setDetail_addr(detail_addr);
            sysUserAddr.setIsdefault(isdefault==1?1:0);

            boolean flag = sysUserAddrService.updateSysUserAddr(sysUserAddr)==0?false:true;
            if(flag){
                return r.ok().put("code",1000).put("msg","更新地址成功！");
            }

        }else{
            return r.error(1002,"当前登录已失效，请先登录！");
        }
        return r;
    }

    @RequestMapping(value = "deleteaddr")
    @ResponseBody
    public R deleteSysUserAddr(@RequestParam(value="id") long id,HttpServletRequest request){
        String token = request.getHeader("token");
        R r = new R();
        String tokenvalue = null;
        try {
            tokenvalue = redisService.get(token);
        } catch (Exception e) {
            return r.error(1003,"服务超时");
        }
        if(tokenvalue != null){
            redisService.expire(token, Expiration.seconds(1800).getExpirationTime());

            boolean flag = sysUserAddrService.deleteAddrById(id)==0?false:true;
            if(flag){
                return r.ok().put("code",1000).put("msg","删除地址成功！");
            }
        }else{
            return r.error(1002,"当前登录已失效，请先登录！");
        }
        return r;
    }
}
