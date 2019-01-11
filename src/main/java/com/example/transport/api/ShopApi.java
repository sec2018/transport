package com.example.transport.api;

import com.example.transport.dao.SysShopMapper;
import com.example.transport.pojo.SysShop;
import com.example.transport.service.Constant;
import com.example.transport.service.UserService;
import com.example.transport.util.JsonResult;
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

/**
 * Created by WangZJ on 2018/10/21.
 */
@RequestMapping("api")
@Controller
public class ShopApi {

    @Autowired
    private SysShopMapper sysShopMapper;

    @Autowired
    private UserService userService;


    @Autowired
    private RedisService redisService;

    //角色1,2
    @ApiOperation(value = "添加或修改商铺", notes = "添加或修改商铺")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "shopid", value = "商户店铺id", required = false, dataType = "Integer",paramType = "query"),
            @ApiImplicitParam(name = "shopname", value = "商户店铺名称", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "shop_tel", value = "商户店铺电话", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "shop_procity", value = "商户店铺省市", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "shop_detailarea", value = "商户店铺详细名称", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String",paramType = "header"),
            @ApiImplicitParam(name = "roleid", value = "用户角色", required = true, dataType = "String",paramType = "header")
    })
    @RequestMapping(value="addorupdateshop",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JsonResult> AddShop(@RequestParam(value = "shopid",required = false)Integer shopid,
            @RequestParam(value = "shopname")String shopname,@RequestParam(value = "shop_tel")String shop_tel,
            @RequestParam(value = "shop_procity")String shop_procity,@RequestParam(value = "shop_detailarea")String shop_detailarea, HttpServletRequest request){
        String roleid = request.getHeader("roleid");
        JsonResult r = new JsonResult();
        if(!roleid.equals("1") && !roleid.equals("2")){
            r = Common.RoleError();
            return ResponseEntity.ok(r);
        }

        String token = request.getHeader("token");
        r = ConnectRedisCheckToken(token);
        String tokenvalue = "";
        try{
            tokenvalue = r.getData().toString();
        }catch (Exception e) {
            r = Common.TokenError();
            e.printStackTrace();
            return ResponseEntity.ok(r);
        }
        if(shopid == null){
            //插入
            try {
                if(tokenvalue!=""){
                    SysShop sysShop = new SysShop();
                    sysShop.setShopName(shopname);
                    sysShop.setShopTel(shop_tel);
                    sysShop.setShopProcity(shop_procity);
                    sysShop.setShopDetailarea(shop_detailarea);
                    redisService.expire(token, Constant.expire.getExpirationTime());
                    String openid = tokenvalue.split("\\|")[0];
                    long wxuserid = userService.getWxUserId(openid);
                    sysShop.setWxuserId(wxuserid);
                    boolean flag = sysShopMapper.insert(sysShop)==1?true:false;
                    if(flag){
                        r.setCode("200");
                        r.setMsg("添加商铺成功！");
                        r.setData(null);
                        r.setSuccess(true);
                    }else{
                        r.setCode(Constant.SHOP_ADDFAILURE.getCode()+"");
                        r.setMsg(Constant.SHOP_ADDFAILURE.getMsg());
                        r.setData(null);
                        r.setSuccess(true);
                    }
                }else{
                    r = Common.TokenError();
                    ResponseEntity.ok(r);
                }
            } catch (Exception e) {
                r.setCode(Constant.SHOP_ADDFAILURE.getCode()+"");
                r.setData(e.getClass().getName() + ":" + e.getMessage());
                r.setMsg(Constant.SHOP_ADDFAILURE.getMsg());
                r.setSuccess(false);
                e.printStackTrace();
            }
        }else{
            //修改
            try {
                if(tokenvalue!=""){
                    redisService.expire(token, Constant.expire.getExpirationTime());
                    String openid = tokenvalue.split("\\|")[0];
                    long wxuserid = userService.getWxUserId(openid);
                    SysShop sysShop = sysShopMapper.selectByPrimaryKey(shopid);
                    sysShop.setShopName(shopname);
                    sysShop.setShopTel(shop_tel);
                    sysShop.setShopProcity(shop_procity);
                    sysShop.setShopDetailarea(shop_detailarea);
                    boolean flag = sysShopMapper.updateByPrimaryKey(sysShop)==1?true:false;
                    if(flag){
                        r.setCode("200");
                        r.setMsg("修改商铺成功！");
                        r.setData(null);
                        r.setSuccess(true);
                    }else{
                        r.setCode(Constant.SHOP_UPDATEFAILURE.getCode()+"");
                        r.setMsg(Constant.SHOP_UPDATEFAILURE.getMsg());
                        r.setData(null);
                        r.setSuccess(true);
                    }
                }else{
                    r = Common.TokenError();
                    ResponseEntity.ok(r);
                }
            } catch (Exception e) {
                r.setCode(Constant.SHOP_UPDATEFAILURE.getCode()+"");
                r.setData(e.getClass().getName() + ":" + e.getMessage());
                r.setMsg(Constant.SHOP_UPDATEFAILURE.getMsg());
                r.setSuccess(false);
                e.printStackTrace();
            }
        }
        return ResponseEntity.ok(r);
    }

    //角色1,2
    @ApiOperation(value = "查询商铺", notes = "查询商铺")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleid", value = "roleid", required = true, dataType = "String",paramType = "header"),
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String",paramType = "header")
    })
    @RequestMapping(value="searchshop",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JsonResult> SearchShop(HttpServletRequest request){
        JsonResult r = new JsonResult();
        String roleid = request.getHeader("roleid");
        if(!roleid.equals("1") && !roleid.equals("2")){
            r = Common.RoleError();
            return ResponseEntity.ok(r);
        }
        String token = request.getHeader("token");
        r = ConnectRedisCheckToken(token);
        String tokenvalue = "";
        try{
            tokenvalue = r.getData().toString();
        }catch (Exception e) {
            r = Common.TokenError();
            e.printStackTrace();
            return ResponseEntity.ok(r);
        }
        try {
            if(tokenvalue!=""){
                redisService.expire(token, Constant.expire.getExpirationTime());
                String openid = tokenvalue.split("\\|")[0];
                long wxuserid = userService.getWxUserId(openid);
                SysShop sysShop = sysShopMapper.selectByWxuserid(wxuserid);
                r.setCode("200");
                r.setMsg("查询商铺成功！");
                r.setData(sysShop);
                r.setSuccess(true);
            }else{
                r = Common.TokenError();
                ResponseEntity.ok(r);
            }
        } catch (Exception e) {
            r = Common.SearchError(e);
            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
    }


    public JsonResult ConnectRedisCheckToken(String token){
        String tokenvalue = "";
        JsonResult r = new JsonResult();
        int retry = 1;
        while (retry<=3){
            try
            {
                //业务代码
                tokenvalue = redisService.get(token);
                r.setCode(200+"");
                r.setData(tokenvalue);
                r.setMsg("连接成功！");
                r.setSuccess(true);
                break;
            }
            catch(Exception ex)
            {
                //重试
                retry++;
                if(retry == 4){
                    //记录错误
                    r.setCode(Constant.Redis_TIMEDOWN.getCode()+"");
                    r.setData("");
                    r.setMsg(Constant.Redis_TIMEDOWN.getMsg());
                    r.setSuccess(false);
                    return r;
                }
            }
        }
        return r;
    }
}
