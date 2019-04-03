package com.example.transport.api;

import com.example.transport.dao.SysTranMapper;
import com.example.transport.pojo.SysTran;
import com.example.transport.service.Constant;
import com.example.transport.service.SysTranService;
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
 * Created by WangZJ on 2019/4/3.
 */
@RequestMapping("api")
@Controller
public class TranApi {


    @Autowired
    private RedisService redisService;

    @Autowired
    private UserService userService;

    @Autowired
    private SysTranMapper sysTranMapper;

    @Autowired
    private SysTranService sysTranService;

    //角色1,2
    @ApiOperation(value = "添加或修改承运员", notes = "添加或修改承运员")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "承运员id", required = false, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "tran_name", value = "承运员名称", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "tran_tel", value = "承运员电话", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "front_url", value = "身份证正面", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "back_url", value = "身份证背面", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "roleid", value = "用户角色", required = true, dataType = "String", paramType = "header")
    })
    @RequestMapping(value = "addorupdatetran", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JsonResult> AddShop(@RequestParam(value = "id", required = false) Integer id,
                                              @RequestParam(value = "tran_name") String tran_name, @RequestParam(value = "tran_tel") String tran_tel,
                                              @RequestParam(value = "front_url") String front_url, @RequestParam(value = "back_url") String back_url, HttpServletRequest request) {
        String roleid = request.getHeader("roleid");
        JsonResult r = new JsonResult();
        if (!roleid.equals("0") && !roleid.equals("1") && !roleid.equals("3")) {
            r = Common.RoleError();
            return ResponseEntity.ok(r);
        }

        String token = request.getHeader("token");
        r = ConnectRedisCheckToken(token);
        String tokenvalue = "";
        try {
            tokenvalue = r.getData().toString();
        } catch (Exception e) {
            r = Common.TokenError();
            e.printStackTrace();
            return ResponseEntity.ok(r);
        }
        if (id == null) {
            //插入
            try {
                if (tokenvalue != "") {
                    SysTran sysTran = new SysTran();
                    sysTran.setTranName(tran_name);
                    sysTran.setTranTel(tran_tel);
                    sysTran.setIdFrontUrl(front_url);
                    sysTran.setIdBackUrl(back_url);
                    redisService.expire(token, Constant.expire.getExpirationTime());
                    String openid = tokenvalue.split("\\|")[0];
                    long wxuserid = userService.getWxUserId(openid);
                    sysTran.setWxuserId(wxuserid);
                    boolean flag = sysTranMapper.insert(sysTran) == 1 ? true : false;
                    if (flag) {
                        r.setCode("200");
                        r.setMsg("添加承运员成功！");
                        r.setData(null);
                        r.setSuccess(true);
                    } else {
                        r.setCode(Constant.TRAN_ADDFAILURE.getCode() + "");
                        r.setMsg(Constant.TRAN_ADDFAILURE.getMsg());
                        r.setData(null);
                        r.setSuccess(true);
                    }
                } else {
                    r = Common.TokenError();
                    ResponseEntity.ok(r);
                }
            } catch (Exception e) {
                r.setCode(Constant.TRAN_ADDFAILURE.getCode() + "");
                r.setData(e.getClass().getName() + ":" + e.getMessage());
                r.setMsg(Constant.TRAN_ADDFAILURE.getMsg());
                r.setSuccess(false);
                e.printStackTrace();
            }
        } else {
            //修改
            try {
                if (tokenvalue != "" && roleid.equals("2")) {
                    redisService.expire(token, Constant.expire.getExpirationTime());
                    String openid = tokenvalue.split("\\|")[0];
                    long wxuserid = userService.getWxUserId(openid);
                    SysTran sysTran = sysTranMapper.selectByPrimaryKey(id);


                    sysTran.setTranName(tran_name);
                    sysTran.setTranTel(tran_tel);
                    sysTran.setIdFrontUrl(front_url);
                    sysTran.setIdBackUrl(back_url);



                    boolean flag = sysTranService.updateByPrimaryKey(sysTran);
                    if (flag) {
                        r.setCode("200");
                        r.setMsg("修改承运员成功！");
                        r.setData(null);
                        r.setSuccess(true);
                    } else {
                        r.setCode(Constant.SHOP_UPDATEFAILURE.getCode() + "");
                        r.setMsg(Constant.SHOP_UPDATEFAILURE.getMsg());
                        r.setData(null);
                        r.setSuccess(true);
                    }
                } else {
                    r = Common.TokenError();
                    ResponseEntity.ok(r);
                }
            } catch (Exception e) {
                r.setCode(Constant.SHOP_UPDATEFAILURE.getCode() + "");
                r.setData(e.getClass().getName() + ":" + e.getMessage());
                r.setMsg(Constant.SHOP_UPDATEFAILURE.getMsg());
                r.setSuccess(false);
                e.printStackTrace();
            }
        }
        return ResponseEntity.ok(r);
    }


    public JsonResult ConnectRedisCheckToken(String token) {
        String tokenvalue = "";
        JsonResult r = new JsonResult();
        int retry = 1;
        while (retry <= 5) {
            try {
                //业务代码
                tokenvalue = redisService.get(token);
                r.setCode(200 + "");
                r.setData(tokenvalue);
                r.setMsg("连接成功！");
                r.setSuccess(true);
                break;
            } catch (Exception ex) {
                //重试
                retry++;
                if (retry == 4) {
                    //记录错误
                    r.setCode(Constant.Redis_TIMEDOWN.getCode() + "");
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
