package com.commons.blog.realm;

import com.commons.blog.model.dto.user.LoginUserDTO;
import com.commons.blog.model.entity.User;
import com.commons.blog.service.UserService;
import com.commons.blog.service.impl.UserServiceImpl;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//自定义的UserRealm
public class UserRealm extends AuthorizingRealm {

    @Autowired
    UserService userService;

    /**
     * 授权
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("执行了=>授权doGetAuthorizationInfo");

        //SimpleAuthorizationInfo
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        //拿到当前登录的这个对象
        Subject subject = SecurityUtils.getSubject();
        //拿到User
        LoginUserDTO currentUser = (LoginUserDTO) subject.getPrincipal();
        //设置当前用户的权限
        info.addStringPermission(currentUser.getAuthorities());

        return info;
    }

    /**
     * 认证
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("执行了=>认证doGetAuthorizationInfo");

        UsernamePasswordToken userToken = (UsernamePasswordToken) token;

        //连接真实数据库
        User user = userService.findByUsername(userToken.getUsername());

        //没有这个人
        if (user == null) {
            throw new UnknownAccountException();
        }

//        Subject currentSubject = SecurityUtils.getSubject();
//        Session session = currentSubject.getSession();
//        session.setAttribute("loginUser",user);

        //可以加密，MD5:e10adc3949ba59abbe56e057f20f883e  MD5:盐值加密
        //密码认证，shiro做
        return new SimpleAuthenticationInfo(user, user.getPassword(), this.getName());
    }


}