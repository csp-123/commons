package com.commons.blog.shiro.realm;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.commons.blog.holder.RequestHolder;
import com.commons.blog.model.constant.BlogRequestConstant;
import com.commons.blog.model.dto.user.LoginDTO;
import com.commons.blog.model.dto.user.LoginUserInfo;
import com.commons.blog.model.entity.User;
import com.commons.blog.service.UserService;
import com.commons.blog.shiro.CustomCredentialsMatcher;
import com.commons.blog.shiro.JwtTokenCredentialsMatcher;
import com.commons.blog.shiro.token.JwtToken;
import com.commons.blog.utils.JwtUtil;
import com.commons.blog.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;


@Slf4j
public class UserRealm extends AuthorizingRealm {

    @Resource
    private UserService userService;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private JwtUtil jwtUtil;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    /**
     * 授权
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        log.info("执行了=>授权doGetAuthorizationInfo");
        //SimpleAuthorizationInfo
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        //拿到当前登录的这个对象
        Subject subject = SecurityUtils.getSubject();
        //拿到User
        LoginDTO currentUser = (LoginDTO) subject.getPrincipal();
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
        log.info("执行了=>认证doGetAuthorizationInfo");

        String jwtToken =(String) token.getCredentials();

        if (jwtUtil.isExpire(jwtToken) && redisUtil.exists(BlogRequestConstant.USER_TOKEN_PREFIX + jwtToken)) {
            jwtToken = userService.refreshToken(jwtToken);
        }

        if (!jwtUtil.verifyToken(jwtToken) || !redisUtil.exists(BlogRequestConstant.USER_TOKEN_PREFIX + jwtToken)) {
            throw new AuthenticationException("身份信息已失效，请重新登录");
        }

        String userString = redisUtil.get(BlogRequestConstant.USER_TOKEN_PREFIX + jwtToken);

        LoginUserInfo user = JSON.parseObject(userString, LoginUserInfo.class);

        //没有这个人
        if (user == null) {
            throw new UnknownAccountException("");
        }

        //密码认证，shiro做
        return new SimpleAuthenticationInfo(user.getUsername(), user, this.getName());
    }



    /**
     * 设置认证加密方式
     */
    @Override
    public void setCredentialsMatcher(CredentialsMatcher credentialsMatcher) {
        JwtTokenCredentialsMatcher jwtTokenCredentialsMatcher = new
                JwtTokenCredentialsMatcher();
        super.setCredentialsMatcher(jwtTokenCredentialsMatcher);
    }



}