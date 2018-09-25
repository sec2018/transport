package com.example.transport.shiro;

import com.example.transport.service.UserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by WangZJ on 2018/8/12.
 */
public class CustomRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    /**
     * 模拟数据库中取得的值
     */
    Map<String,String> userMap = new HashMap<>(16);

    {
        userMap.put("wang","123");
        super.setName("customRealm");
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String username = (String)principalCollection.getPrimaryPrincipal();
        //模拟从数据库或缓存中获取角色数据
        Set<String> roles = getRoleByUserName(username);

        Set<String> permissions = getPermissionsByUserName(username);

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.setStringPermissions(permissions);
        simpleAuthorizationInfo.setRoles(roles);

        return simpleAuthorizationInfo;
    }

    private Set<String> getPermissionsByUserName(String username) {
        Set<String> sets = new HashSet<>();
        sets.add("user:delete");
        sets.add("user:add");
        return sets;
    }

    private Set<String> getRoleByUserName(String username) {
        Set<String> sets = new HashSet<>();
        sets.add("admin");
        sets.add("user");
        return sets;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        //1,从主体传过来的认证信息中，获得用户名
        String userName = (String) authenticationToken.getPrincipal();

        //2,通过用户名到数据库中获取凭证
//        String password = getPasswordByUserName(userName);
        String password = getPasswordByUserNameFromDb(userName);
        if(password == null) {
            return null;
        }
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(userName,password,"customRealm");
        return authenticationInfo;
    }

    /*
        模拟数据库查询验证
     */
    private String getPasswordByUserName(String userName) {
        return userMap.get(userName);
    }


    /**
     * 真实数据库中取值
     */
    private String getPasswordByUserNameFromDb(String userName) {
        return userService.getPasswordByUserName(userName);
    }
}
