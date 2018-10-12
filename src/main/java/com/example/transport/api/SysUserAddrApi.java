package com.example.transport.api;

import com.example.transport.pojo.SysUserAddr;
import com.example.transport.service.Constant;
import com.example.transport.service.SysUserAddrService;
import com.example.transport.service.UserService;
import com.example.transport.util.JsonResult;
import com.example.transport.util.R;
import com.example.transport.util.redis.RedisService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
public class SysUserAddrApi {

    @Autowired
    private SysUserAddrService sysUserAddrService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private UserService userService;


    @ApiOperation(value = "添加地址接口", notes = "添加地址")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uname", value = "姓名", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "tel", value = "电话", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "pro_city", value = "省市区", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "detail_addr", value = "详细地址", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "isdefault", value = "是否设为默认地址", required = true, dataType = "Integer",paramType = "query"),
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String",paramType = "header")
    })
    @RequestMapping(value = "addaddr",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JsonResult> insertSysUserAddr(@RequestParam(value="uname") String uname, @RequestParam(value="tel")String tel, @RequestParam(value="pro_city")String pro_city,
                       @RequestParam(value="detail_addr")String detail_addr, @RequestParam(value="isdefault")int isdefault, HttpServletRequest request){
        String token = request.getHeader("token");
        String tokenvalue = null;
        JsonResult r = new JsonResult();
        try {
            tokenvalue = redisService.get(token);
        } catch (Exception e) {
            r.setCode(Constant.Redis_TIMEDOWN.getCode()+"");
            r.setData(e.getClass().getName() + ":" + e.getMessage());
            r.setMsg(Constant.Redis_TIMEDOWN.getMsg());
            r.setSuccess(false);
            e.printStackTrace();
            return ResponseEntity.ok(r);
        }
        try {
            redisService.expire(token, Constant.expire.getExpirationTime());
            String openid = tokenvalue.split("\\|")[0];
            String session_key = tokenvalue.split("\\|")[1];
            //获取当前微信用户id
            long wxuserid = userService.getWxUserId(openid);
            //判断用户已存在地址数是否大于10
            if(sysUserAddrService.getAddrCount(wxuserid)>=10){
                r.setCode(Constant.Addr_BEYOND.getCode()+"");
                r.setData(null);
                r.setMsg(Constant.Addr_BEYOND.getMsg());
                r.setSuccess(false);
                return ResponseEntity.ok(r);
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
                r.setCode("200");
                r.setMsg("添加地址成功！");
                r.setData(null);
                r.setSuccess(true);
            }
        } catch (Exception e) {
            r.setCode(Constant.TOKEN_ERROR.getCode()+"");
            r.setData(e.getClass().getName() + ":" + e.getMessage());
            r.setMsg(Constant.TOKEN_ERROR.getMsg());
            r.setSuccess(false);
            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
    }

    @ApiOperation(value = "搜索用户地址接口", notes = "搜索地址")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String",paramType = "header")
    })
    @RequestMapping(value = "searchaddr",method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<JsonResult> searchSysUserAddr(HttpServletRequest request){
        String token = request.getHeader("token");
        JsonResult r = new JsonResult();
        String tokenvalue = null;
        try {
            tokenvalue = redisService.get(token);
        } catch (Exception e) {
            r.setCode(Constant.Redis_TIMEDOWN.getCode()+"");
            r.setData(e.getClass().getName() + ":" + e.getMessage());
            r.setMsg(Constant.Redis_TIMEDOWN.getMsg());
            r.setSuccess(false);
            return ResponseEntity.ok(r);
        }
        try {
            if(tokenvalue != null){
                redisService.expire(token, Constant.expire.getExpirationTime());
                String openid = tokenvalue.split("\\|")[0];
                String session_key = tokenvalue.split("\\|")[1];
                //获取当前微信用户id
                long wxuserid = userService.getWxUserId(openid);
                List<SysUserAddr> addrList = sysUserAddrService.getAddrList(wxuserid);
                r.setCode("200");
                r.setMsg("查询成功！");
                r.setData(addrList);
                r.setSuccess(true);
            }else{
                r.setCode(Constant.TOKEN_ERROR.getCode()+"");
                r.setData(null);
                r.setMsg(Constant.TOKEN_ERROR.getMsg());
                r.setSuccess(false);
            }
        } catch (Exception e) {
            r.setCode(Constant.TOKEN_ERROR.getCode()+"");
            r.setData(e.getClass().getName() + ":" + e.getMessage());
            r.setMsg(Constant.TOKEN_ERROR.getMsg());
            r.setSuccess(false);
            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
    }

    @ApiOperation(value = "更新地址接口", notes = "更新地址")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "地址id", required = true, dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "uname", value = "姓名", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "tel", value = "电话", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "pro_city", value = "省市区", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "detail_addr", value = "详细地址", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "isdefault", value = "是否设为默认地址", required = true, dataType = "Integer",paramType = "query"),
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String",paramType = "header")
    })
    @RequestMapping(value = "updateaddr",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JsonResult> insertSysUserAddr(@RequestParam(value="id") long id,@RequestParam(value="uname") String uname, @RequestParam(value="tel")String tel, @RequestParam(value="pro_city")String pro_city,
                               @RequestParam(value="detail_addr")String detail_addr, @RequestParam(value="isdefault")int isdefault, HttpServletRequest request){
        String token = request.getHeader("token");
        JsonResult r = new JsonResult();
        String tokenvalue = null;
        try {
            tokenvalue = redisService.get(token);
        } catch (Exception e) {
            r.setCode(Constant.Redis_TIMEDOWN.getCode()+"");
            r.setData(e.getClass().getName() + ":" + e.getMessage());
            r.setMsg(Constant.Redis_TIMEDOWN.getMsg());
            r.setSuccess(false);
            e.printStackTrace();
            return ResponseEntity.ok(r);
        }
        try {
            if(tokenvalue != null){
                redisService.expire(token, Constant.expire.getExpirationTime());
                String openid = tokenvalue.split("\\|")[0];
                String session_key = tokenvalue.split("\\|")[1];

                SysUserAddr sysUserAddr = sysUserAddrService.getAddrById(id);
                sysUserAddr.setUname(uname);
                sysUserAddr.setTel(tel);
                sysUserAddr.setPro_city(pro_city);
                sysUserAddr.setDetail_addr(detail_addr);
                sysUserAddr.setIsdefault(isdefault==1?1:0);

                boolean flag = sysUserAddrService.updateSysUserAddr(sysUserAddr)==0?false:true;
                if(flag) {
                    r.setCode("200");
                    r.setMsg("更新地址成功！");
                    r.setData(null);
                    r.setSuccess(true);
                }
            }else{
                r.setCode(Constant.TOKEN_ERROR.getCode()+"");
                r.setData(null);
                r.setMsg(Constant.TOKEN_ERROR.getMsg());
                r.setSuccess(false);
            }
        } catch (Exception e) {
            r.setCode(Constant.TOKEN_ERROR.getCode()+"");
            r.setData(e.getClass().getName() + ":" + e.getMessage());
            r.setMsg(Constant.TOKEN_ERROR.getMsg());
            r.setSuccess(false);
            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
    }

    @ApiOperation(value = "删除地址接口", notes = "删除地址")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "地址id", required = true, dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String",paramType = "header")
    })
    @RequestMapping(value = "deleteaddr",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JsonResult> deleteSysUserAddr(@RequestParam(value="id") long id,HttpServletRequest request){
        String token = request.getHeader("token");
        JsonResult r = new JsonResult();
        String tokenvalue = null;
        try {
            tokenvalue = redisService.get(token);
        } catch (Exception e) {
            r.setCode(Constant.Redis_TIMEDOWN.getCode()+"");
            r.setData(e.getClass().getName() + ":" + e.getMessage());
            r.setMsg(Constant.Redis_TIMEDOWN.getMsg());
            r.setSuccess(false);
            e.printStackTrace();
            return ResponseEntity.ok(r);
        }
        try {
            if(tokenvalue != null){
                redisService.expire(token, Constant.expire.getExpirationTime());

                boolean flag = sysUserAddrService.deleteAddrById(id)==0?false:true;
                if (flag) {
                    r.setCode("200");
                    r.setMsg("删除地址成功！");
                    r.setData(null);
                    r.setSuccess(true);
                }
            }else{
                r.setCode(Constant.TOKEN_ERROR.getCode()+"");
                r.setData(null);
                r.setMsg(Constant.TOKEN_ERROR.getMsg());
                r.setSuccess(false);
            }
        } catch (Exception e) {
            r.setCode(Constant.TOKEN_ERROR.getCode()+"");
            r.setData(e.getClass().getName() + ":" + e.getMessage());
            r.setMsg(Constant.TOKEN_ERROR.getMsg());
            r.setSuccess(false);
            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
    }
}
