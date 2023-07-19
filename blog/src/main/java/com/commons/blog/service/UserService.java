package com.commons.blog.service;

import com.commons.blog.model.dto.user.LoginUserDTO;
import com.commons.blog.model.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

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
     * @param loginUserDTO
     */
    void register(LoginUserDTO loginUserDTO);

    /**
     * 手机号快速注册
     * @param loginUserDTO
     */
    void quickRegister(LoginUserDTO loginUserDTO);
}
