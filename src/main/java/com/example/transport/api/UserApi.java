package com.example.transport.api;

import com.example.transport.dao.SysRoleMapper;
import com.example.transport.model.SysRoleExample;
import com.example.transport.pojo.SysRole;
import com.example.transport.pojo.User;
import com.example.transport.service.Constant;
import com.example.transport.service.SysUserTokenService;
import com.example.transport.service.UserService;
import com.example.transport.util.JsonResult;
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

@RequestMapping("api")
@Controller
public class UserApi {

    @Autowired
    private UserService userService;

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
}
