package com.commons.auth.controller;

import com.commons.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author baomidou
 * @since 2022-09-26
 */
@RestController
@RequestMapping("/auth/user")
public class UserController {
    @Autowired
    private UserService userService;


}
