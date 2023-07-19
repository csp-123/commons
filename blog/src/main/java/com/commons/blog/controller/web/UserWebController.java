package com.commons.blog.controller.web;

import com.commons.blog.constant.BlogRequestConstant;
import com.commons.blog.model.dto.user.LoginUserDTO;
import com.commons.blog.model.dto.user.UserRegisterDTO;
import com.commons.blog.service.UserService;
import com.commons.core.pojo.Response;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

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
@Api(tags = "用户")
public class UserWebController {

    @Resource
    private UserService userService;

    @PostMapping("/login")
    public Response login(@RequestBody LoginUserDTO loginUserDTO) {
        String loginResult = "登录失败";
        //获取主题对象
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(new UsernamePasswordToken(loginUserDTO.getUsername(), loginUserDTO.getPassword()));
            log.info("用户{}登录成功", loginUserDTO.getUsername());
            return Response.ok();
        } catch (UnknownAccountException e) {
            loginResult = "用户名错误";
            log.error(loginResult);
        } catch (IncorrectCredentialsException e) {
            loginResult = "密码错误";
            log.error(loginResult);
        }
        return Response.fail(loginResult);
    }


    @GetMapping("logout")
    public Response logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return Response.ok();
    }

    @PostMapping("/register")
    public Response register(@RequestBody LoginUserDTO loginUserDTO) {
        userService.register(loginUserDTO);
        return Response.ok();
    }


    @PostMapping("/quickRegister")
    public Response quickRegister(@RequestBody LoginUserDTO loginUserDTO) {
        userService.quickRegister(loginUserDTO);
        return Response.ok();
    }


}
