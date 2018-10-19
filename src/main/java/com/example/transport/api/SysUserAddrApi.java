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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    @Transactional
    @RequestMapping(value = "addaddr",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JsonResult> insertSysUserAddr(@RequestParam(value="uname") String uname, @RequestParam(value="tel")String tel, @RequestParam(value="pro_city")String pro_city,
                       @RequestParam(value="detail_addr")String detail_addr, @RequestParam(value="isdefault")int isdefault, HttpServletRequest request){
        JsonResult r = new JsonResult();
        //手机号校验
        /* 运营商号段如下：
 * 中国联通号码：130、131、132、145（无线上网卡）、155、156、185（iPhone5上市后开放）、186、176（4G号段）、
 *               175（2015年9月10日正式启用，暂只对北京、上海和广东投放办理）
 * 中国移动号码：134、135、136、137、138、139、147（无线上网卡）、150、151、152、157、158、159、182、183、187、188、178
 * 中国电信号码：133、153、180、181、189、177、173、149 虚拟运营商：170、1718、1719 */
//        String regex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(16[0-9])|(17[013678])|(18[0,5-9]))\\d{8}$";
        String regex = "^1[3|4|5|6|7|8][0-9]\\d{8}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(tel);
        boolean isMatch = m.matches();
        if(tel.length() != 11 || !isMatch){
            r.setCode(Constant.TEL_WRONG.getCode()+"");
            r.setData(null);
            r.setMsg(Constant.TEL_WRONG.getMsg());
            r.setSuccess(false);
            return ResponseEntity.ok(r);
        }


        String token = request.getHeader("token");
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
            redisService.expire(token, Constant.expire.getExpirationTime());
            String openid = tokenvalue.split("\\|")[0];
            String session_key = tokenvalue.split("\\|")[1];
            //获取当前微信用户id
            long wxuserid = userService.getWxUserId(openid);
            //判断用户已存在地址数是否大于5
            if(sysUserAddrService.getAddrCount(wxuserid)>5){
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
            if(sysUserAddrService.getAddrCount(wxuserid)==0){
                sysUserAddr.setIsdefault(1);
            }else{
                if(isdefault==1){
                    //把原默认地址状态设为0
                    sysUserAddrService.updateSysUserAddrDefault(wxuserid);
                }
                sysUserAddr.setIsdefault(isdefault==1?1:0);
            }
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
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
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
        int retry = 1;
        while (retry<=3){
            try
            {
                //业务代码
                tokenvalue = redisService.get(token);
                break;
            }
            catch(Exception ex)
            {
                //重试
                retry++;
                if(retry == 4){
                    //记录错误
                    r.setCode(Constant.Redis_TIMEDOWN.getCode()+"");
                    r.setData(null);
                    r.setMsg(Constant.Redis_TIMEDOWN.getMsg());
                    r.setSuccess(false);
                    return ResponseEntity.ok(r);
                }
            }
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

                boolean flag = sysUserAddrService.updateSysUserAddr(sysUserAddr);
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

                boolean flag = sysUserAddrService.deleteAddrById(id);
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
