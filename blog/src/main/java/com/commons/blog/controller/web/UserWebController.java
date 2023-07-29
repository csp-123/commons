package com.commons.blog.controller.web;

import com.commons.blog.model.constant.BlogRequestConstant;
import com.commons.blog.model.dto.user.LoginDTO;
import com.commons.blog.model.dto.user.UserQuickRegisterDTO;
import com.commons.blog.model.dto.user.UserRegisterDTO;
import com.commons.blog.service.UserService;
import com.commons.core.pojo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.commons.blog.model.constant.BlogRequestConstant.AUTHORIZATION_FIELD;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author baomidou
 * @since 2023-07-10
 */
@RestController
@Slf4j
@RequestMapping(BlogRequestConstant.REQUEST_WEB_PREFIX + "/user")
@Api(tags = "用户-WEB")
public class UserWebController {

    @Resource
    private UserService userService;

   

    @PostMapping("/login")
    @ApiOperation("登录")
    public Result login(@RequestBody LoginDTO loginDTO, HttpServletResponse response) {
        try {
            String token = userService.login(loginDTO);

            response.addHeader(AUTHORIZATION_FIELD, token);
            return Result.success(token);
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }


    @GetMapping("logout")
    @ApiOperation("登出")
    public Result logout(HttpServletRequest request) {
        userService.logout(request);
        return Result.success();
    }

    @PostMapping("/register")
    @ApiOperation("注册")
    public Result register(@RequestBody UserRegisterDTO registerDTO) {
        userService.register(registerDTO);
        return Result.success();
    }


    @PostMapping("/quickRegister")
    @ApiOperation("快速注册")
    public Result quickRegister(@RequestBody UserQuickRegisterDTO quickRegisterDTO) {
        userService.quickRegister(quickRegisterDTO);
        return Result.success();
    }


}
