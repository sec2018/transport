package com.example.transport.config;

import com.example.transport.pojo.Constant;
import com.example.transport.pojo.User;
import com.example.transport.service.SysUserTokenService;
import com.example.transport.service.UserService;
import com.example.transport.util.JSONUtil;
import com.example.transport.util.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private RedisService redisService;

    @Autowired
    private UserService userService;

    @Autowired
    private SysUserTokenService sysUserTokenService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String token = request.getHeader("token");
        String roleid = request.getHeader("roleid");

        // 初始化拦截器，设置不拦截路径
        String[] noMatchPath = Constant.NO_MATCH_PATHLIST;
        String path = request.getServletPath();

        System.out.println("资源请求路径："+path);

        //判断不拦截路径是否包含当前路径
        boolean flag = false;
        for(String t : noMatchPath){
            if(path.matches(t)){
                flag = true;
                break;
            }
        }
        if(flag){
            // 授权路径，不拦截
            return  true;
        } else if(null == token || "".equals(token)) {
            // 找不到用户Token，重定位到登录
            response.sendRedirect(request.getContextPath()+"/login");
            return false;
        } else {
            //设置其他地方登录后，本次登录失效
            try {
                if(roleid.equals("0")){
                    User user = userService.getUserByLoginName("system");
                    if(token.equals(sysUserTokenService.getToken(user.getId()))){
                        return true;
                    }else{
                        response.sendRedirect(request.getContextPath() + "/login");
                        return false;
                    }
                }else{
                    //将redis缓存中的用户信息取出
                    String userstr = redisService.get("token:" + token);
                    String username = ((User) JSONUtil.JSONToObj(userstr, User.class)).getLoginname();
                    String value_token = redisService.get("token:" + username);
                    System.out.println(username);
                    if (token.equals(value_token)) {
                        return true;
                    } else {
                        System.out.println("用户已在其他地方登录！");
                        response.sendRedirect(request.getContextPath() + "/login");
                        return false;
                    }
                }
            }catch (Exception e){
                System.out.println("获取redis缓存中的token失败");
                response.sendRedirect(request.getContextPath()+"/login");
                return false;
            }
        }
    }
}
