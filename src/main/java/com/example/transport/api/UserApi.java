package com.example.transport.api;

import com.example.transport.pojo.*;
import com.example.transport.service.Constant;
import com.example.transport.service.SysUserTokenService;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("api")
@Controller
public class UserApi {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private SysUserTokenService sysUserTokenService;

    @ApiOperation(value = "获取用户信息", notes = "获取用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "username", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String",paramType = "header")
    })
    @RequestMapping(value="getuserinfo",method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<JsonResult> GetUserInfo(@RequestParam(value = "username") String username,HttpServletRequest request){
        JsonResult r = new JsonResult();
        String token = request.getHeader("token");
        try {
            User user = userService.getUserByLoginName(username);

            if(token.equals(sysUserTokenService.getToken(user.getId()))){
                r.setCode("200");
                r.setMsg("获取用户信息成功！");
                r.setData(user);
                r.setSuccess(true);
            }else{
                r.setCode(Constant.PERSONINFO_FAILURE.getCode()+"");
                r.setData(null);
                r.setMsg(Constant.PERSONINFO_FAILURE.getMsg());
                r.setSuccess(false);
            }
        } catch (Exception e) {
            r.setCode(Constant.PERSONINFO_FAILURE.getCode()+"");
            r.setData(e.getClass().getName() + ":" + e.getMessage());
            r.setMsg(Constant.PERSONINFO_FAILURE.getMsg());
            r.setSuccess(false);
            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
    }


    //角色0,1
    @ApiOperation(value = "查询所有用户", notes = "查询所有用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleid", value = "roleid", required = true, dataType = "String",paramType = "header"),
            @ApiImplicitParam(name = "token", value = "用户token", required = true, dataType = "String",paramType = "header")
    })
    @RequestMapping(value="getalluser",method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<JsonResult> GetUsers(HttpServletRequest request){
        JsonResult r = new JsonResult();
        String roleid = request.getHeader("roleid");
        if(!roleid.equals("0") && !roleid.equals("1")){
            r = Common.RoleError();
            return ResponseEntity.ok(r);
        }
        String token = request.getHeader("token");
        r = ConnectRedisCheckToken(token);

        //超级管理员,PC端
        if (roleid.equals("0")) {
            token = getAdminToken();
            if (!token.equals(token)) {
                r = Common.TokenError();
                return ResponseEntity.ok(r);
            } else {
                r = GetAllUsers();
            }
        } else {
            r = ConnectRedisCheckToken(token);
            String tokenvalue = "";
            try {
                tokenvalue = r.getData().toString();
                if (tokenvalue != null) {
                    r = GetAllUsers();
                }
            } catch (Exception e) {
                r = Common.TokenError();
                e.printStackTrace();
                return ResponseEntity.ok(r);
            }
        }
        return ResponseEntity.ok(r);
    }


    public JsonResult GetAllUsers(){
        JsonResult r = new JsonResult();
        try {
            List<WxUser>  userlist = userService.getAllWxUser();
            List<Map<String,Object>> umaplist = new ArrayList<>();
            Map<String,Object> umap = null;
            for(WxUser wu : userlist){
                umap = new HashMap<String,Object>();
                umap.put("id",wu.getId());
                umap.put("nickname",wu.getNickname());
                umap.put("gender",wu.getGender()==1?"男":"女");
                umap.put("city",wu.getCity());
                umap.put("province",wu.getProvince());
                umap.put("country",wu.getCountry());
                umap.put("avatarurl",wu.getAvatarurl());
                umap.put("timestamp",wu.getTimestamp());
                umaplist.add(umap);
            }
            r.setCode("200");
            r.setMsg("获取所有用户信息成功！");
            r.setData(umaplist);
            r.setSuccess(true);
        } catch (Exception e) {
            r.setCode(Constant.PERSONINFO_FAILURE.getCode()+"");
            r.setData(e.getClass().getName() + ":" + e.getMessage());
            r.setMsg(Constant.PERSONINFO_FAILURE.getMsg());
            r.setSuccess(false);
            e.printStackTrace();
        }
        return  r;
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

    public String getAdminToken(){
        User user = userService.getUserByLoginName("system");
        String admintoken  = sysUserTokenService.getToken(user.getId());
        return admintoken;
    }
}
