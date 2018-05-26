package com.jxyq.service;

import com.jxyq.commons.constants.BeanProperty;
import com.jxyq.model.MultiLoginAuthenticationToken;
import com.jxyq.model.Role;
import com.jxyq.model.user.User;
import com.jxyq.service.inf.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

public class UserRealm extends AuthorizingRealm {

    @Autowired
    protected UserService userService;

    /**
     * 认证回调函数,登录时调用.
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authToken;
        String username = token.getUsername();
        User user = userService.selUserByPhone(username);
        if (null == user) return null;
        return new SimpleAuthenticationInfo(user.getPhone(), user.getPassword(), this.getName());
    }

    /**
     * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String phone = (String) principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        User user = userService.selUserWithRolesByPhone(phone);
        for (Role role : user.getRoleList()) {
            info.addRole(role.getName());
            info.addStringPermissions(role.getPermissionList());
        }
        return info;
    }

    @PostConstruct
    public void initCredentialsMatcher() {
        setCredentialsMatcher(new CustomCredentialsMatcher());
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        if (token instanceof MultiLoginAuthenticationToken) {
            return ((MultiLoginAuthenticationToken) token).getRealmName().equalsIgnoreCase("UserRealm");
        }
        return false;
    }

    @Override
    public boolean hasRole(PrincipalCollection principal, String roleIdentifier) {
        if (principal.fromRealm(getName()).isEmpty()) {
            return false;
        } else {
            return super.hasRole(principal, roleIdentifier);
        }
    }
}
