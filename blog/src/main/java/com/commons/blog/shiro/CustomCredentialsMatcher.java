package com.commons.blog.shiro;

import com.commons.blog.context.UserContext;
import com.commons.blog.convert.UserConvert;
import com.commons.blog.model.entity.User;
import com.commons.blog.service.UserService;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * shiro 自定义匹配器
 * 已废弃，是用redis实现
 */
@Deprecated
//@Component
public class CustomCredentialsMatcher extends SimpleCredentialsMatcher {

    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Override
    public boolean doCredentialsMatch(AuthenticationToken authcToken, AuthenticationInfo info) {
        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
        String password = String.valueOf(token.getPassword());
        String dbPassword = String.valueOf(info.getCredentials());
        boolean matches = bCryptPasswordEncoder.matches(password, dbPassword);
        return matches;
    }
}