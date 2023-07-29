package com.commons.blog.service;

import com.commons.blog.model.dto.user.LoginDTO;
import com.commons.blog.model.dto.user.LoginUserInfo;
import com.commons.blog.model.dto.user.UserQuickRegisterDTO;
import com.commons.blog.model.dto.user.UserRegisterDTO;
import com.commons.blog.model.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author baomidou
 * @since 2023-07-10
 */
public interface UserService extends IService<User> {

    /**
     * 通过用户名查找
     * @param username
     * @return
     */
    User findByUsername(String username);

    /**
     * 用户注册
     *
     * @param registerDTO
     */
    void register(UserRegisterDTO registerDTO);

    /**
     * 手机号快速注册
     * @param quickRegisterDTO
     */
    void quickRegister(UserQuickRegisterDTO quickRegisterDTO);

    /**
     * 登录
     *
     * @param loginDTO
     * @param request
     * @return
     */
    String login(LoginDTO loginDTO);

    /**
     * 登出
     * @param request
     */
    void logout(HttpServletRequest request);

    /**
     * token换user信息
     * @param token
     * @return
     */
    LoginUserInfo getCurUser(String token);


    /**
     * 刷新用户token
     * @param jwtToken
     * @return
     */
    String refreshToken(String jwtToken);
}
