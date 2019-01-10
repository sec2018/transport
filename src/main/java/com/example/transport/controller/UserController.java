package com.example.transport.controller;

import com.example.transport.pojo.Constant;
import com.example.transport.pojo.User;
import com.example.transport.pojo.UserToken;
import com.example.transport.service.SysUserTokenService;
import com.example.transport.service.UserService;
import com.example.transport.util.AESUtil;
import com.example.transport.util.JsonResult;
import com.example.transport.util.R;
import com.example.transport.util.redis.RedisService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.Date;

/**
 * Created by WangZJ on 2018/8/12.
 */
@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private SysUserTokenService sysUserTokenService;

//    @RequestMapping(value = "/sublogin",method = RequestMethod.POST)
//    @ResponseBody
//    public String subLogin(@RequestParam(value = "u") String loginname, @RequestParam(value = "p") String pwd,HttpServletRequest request,Model model){
//
//        Subject subject = SecurityUtils.getSubject();
//        try {
//            pwd = AESUtil.encryptData(loginname + pwd);
//            UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(loginname,pwd);
////            usernamePasswordToken.setRememberMe(true);
//            subject.login(usernamePasswordToken);//验证角色和权限
//            if (subject.isAuthenticated()){
//                HttpSession session = request.getSession();
//                //用户信息
//                User user = userService.getUserByLoginName(loginname);
//                session.setAttribute("currentUser",user);
//
//                //生成token，并保存数据库
//                R r = sysUserTokenService.createToken(user.getWxuser_id());
//                session.setAttribute("currentUserToken",r);
//
//                System.out.println(loginname+" success login");
//                model.addAttribute("status", "success");
//                model.addAttribute("url", "index");
//                return JSONObject.fromObject(model).toString();
//            }else{
//                usernamePasswordToken.clear();
//            }
//        }catch(UnknownAccountException uae){
//            model.addAttribute("message_login", "未知账户");
//        }catch(IncorrectCredentialsException ice){
//            model.addAttribute("message_login", "密码不正确");
//        }catch(LockedAccountException lae){
//            model.addAttribute("message_login", "账户已锁定");
//        }catch(ExcessiveAttemptsException eae){
//            model.addAttribute("message_login", "用户名或密码错误次数过多");
//        }catch(AuthenticationException ae){
//            model.addAttribute("message_login", "用户名或密码不正确");
//        }catch(Exception ex){
//            System.out.println("【shio其他错误】 "+ex);
//        }
//        model.addAttribute("status", "failed");
//        model.addAttribute("url", "login");
//        return JSONObject.fromObject(model).toString();
//    }

    @RequestMapping(value = "/sublogin",method = RequestMethod.POST)
    @ResponseBody
    public JsonResult subLogin(@RequestParam(value = "u") String loginname, @RequestParam(value = "p") String pwd,HttpServletRequest request,Model model){
        //判断登录
        try{
            pwd = AESUtil.encryptData(loginname + pwd);
            //userService为自己定义的Service类
            User user = userService.userLogin(loginname, pwd);
            if(user!=null){
                //PassHandle为自己定义的一个生成Token的类，可以根据自己喜好来改
                //生成token，并保存数据库
                R r = sysUserTokenService.createToken(user.getWxuser_id());
                user.setPassword("皮一下逗逗你");
                com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
                jsonObject.put("token", r.get("token"));     //此data为token
                jsonObject.put("data",loginname);
                return new JsonResult(Constant.SUCCESS.getCode()+"", "登录"+Constant.SUCCESS.getMsg(),true, jsonObject);
            } else
                return new JsonResult(Constant.LOGIN_EXCEPTION.getCode()+"", Constant.LOGIN_EXCEPTION.getMsg(),false, null);
        }catch (Exception e){
            return new JsonResult(Constant.LOGIN_EXCEPTION.getCode()+"", Constant.LOGIN_EXCEPTION.getMsg(),false, null);
        }
    }

    @RequestMapping(value = "/index")
    public String index(HttpServletRequest request, Model model){
        //此处可以访问到
        HttpSession session=request.getSession();
        if(session.getAttribute("currentUser")==null){
            return "redirect:/adminlte/pages/login.html";
        }
        model.addAttribute("hello","Hello, Spring Boot!");
        return "index";
    }

    @RequestMapping(value = "/test")
    @ResponseBody
    public String Test(){
    	System.out.println(10/0);
        return "okkkk！";
    }

//    @RequestMapping(value = "/logout")
//    public String logout(HttpServletRequest request){
//        Subject subject = SecurityUtils.getSubject();
//
//        HttpSession session=request.getSession();
//        //这里并没有session.getAttribute("currentUserToken")。。。。。（待核查）
//        if(session.getAttribute("currentUserToken")!=null){;
//            String token = ((R)session.getAttribute("currentUserToken")).get("token").toString();
//            UserToken userToken = sysUserTokenService.queryByToken(token);
//            userToken.setExpire_time(new Date());
//            sysUserTokenService.updateToken(userToken);
//        }
//        subject.logout();
//        System.out.println("logout...");
////        return "redirect:/login.html";
//        return "redirect:/adminlte/pages/login.html";
//    }


    @RequestMapping(value = "/logout")
    @ResponseBody
    public String logout(HttpServletRequest request){
        String token = request.getHeader("token");
        if(token!=null && !token.equals("")){
            UserToken userToken = sysUserTokenService.queryByToken(token);
            userToken.setExpire_time(new Date());
            sysUserTokenService.updateToken(userToken);
        }
        System.out.println("logout...");
        return "success";
    }

    @GetMapping(value = "/login")
    public void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect(request.getContextPath()+"/adminlte/pages/login.html");
    }
}
