package com.commons.blog.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.commons.blog.context.UserContext;
import com.commons.blog.convert.UserConvert;
import com.commons.blog.holder.RequestHolder;
import com.commons.blog.model.annotation.SourceTypeAnno;
import com.commons.blog.model.constant.BlogRequestConstant;
import com.commons.blog.model.dto.user.*;
import com.commons.blog.model.enums.SourceTypeEnum;
import com.commons.blog.model.entity.User;
import com.commons.blog.mapper.UserMapper;
import com.commons.blog.model.exception.BlogException;
import com.commons.blog.service.UserService;
import com.commons.blog.shiro.token.JwtToken;
import com.commons.blog.utils.JwtUtil;
import com.commons.blog.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.commons.blog.model.constant.BlogRequestConstant.AUTHORIZATION_FIELD;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2023-07-10
 */
@Service
@Slf4j
@SourceTypeAnno(type = SourceTypeEnum.USER)
public class UserServiceImpl extends BaseSourceService<UserMapper, User> implements UserService {

    @Resource
    private UserConvert userConvert;

    @Resource
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Resource
    private UserContext userContext;

    @Resource
    private JwtUtil jwtUtil;

    @Resource
    private RedisUtil redisUtil;

    @Override
    public User findByUsername(String username) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        User user = this.getOne(wrapper);
        return Optional.ofNullable(user).orElseThrow(() -> new IllegalArgumentException(String.format("未查询到此用户名的数据：【%s】", username)));
    }

    @Override
    public void register(UserRegisterDTO registerDTO) {
//        assertRegisterParamValid(registerDTO);
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, registerDTO.getUsername());
        boolean exists = this.getBaseMapper().exists(wrapper);
        if (exists) {
            throw new IllegalArgumentException("用户名已存在");
        }
        User user = userConvert.registerDTO2User(registerDTO);
        if (StringUtils.hasText(registerDTO.getPassword())) {
            // 密文
            String encryption = bCryptPasswordEncoder.encode(registerDTO.getPassword());
            user.setPassword(encryption);
        }
        // todo 资源类型之后要全局统一处理
        saveReturnId(user);
    }

    /**
     * 短信快速注册
     *
     * @param quickRegisterDTO
     */
    @Override
    public void quickRegister(UserQuickRegisterDTO quickRegisterDTO) {


    }

    @Override
    public String login(LoginDTO loginDTO) {
        User user = findByUsername(loginDTO.getUsername());
        boolean matches = bCryptPasswordEncoder.matches(loginDTO.getPassword(), user.getPassword());
        if (matches) {
            String jwtToken = jwtUtil.createJwtToken(user.getUsername(), 30, TimeUnit.MINUTES);
            log.info("用户{}登录成功", loginDTO.getUsername());
            // 登录成功后 用户上下文赋值
            redisUtil.set(BlogRequestConstant.USER_TOKEN_PREFIX + jwtToken, JSON.toJSONString(userConvert.user2LoginUserInfo(user)), 1L, TimeUnit.HOURS);
            Subject subject = SecurityUtils.getSubject();
            subject.login(new JwtToken(jwtToken));
            return jwtToken;
        } else {
            throw new BlogException("用户名或密码错误");
        }
    }

    @Override
    public void logout(HttpServletRequest request) {
        userContext.clear();
        String token = request.getHeader(AUTHORIZATION_FIELD);
        redisUtil.remove(BlogRequestConstant.USER_TOKEN_PREFIX + token);
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
    }

    @Override
    public LoginUserInfo getCurUser() {
        String token = RequestHolder.getRequest().getHeader(AUTHORIZATION_FIELD);
        String loginUserInfoString = redisUtil.get(token);
        return JSON.parseObject(loginUserInfoString, LoginUserInfo.class);
    }

    @Override
    public String refreshToken(String jwtToken) {
        String username = jwtUtil.getUsername(jwtToken);
        redisUtil.remove(BlogRequestConstant.USER_TOKEN_PREFIX + jwtToken);
        String newJwtToken = jwtUtil.createJwtToken(username, 30, TimeUnit.MINUTES);
        RequestHolder.getResponse().setHeader(AUTHORIZATION_FIELD, newJwtToken);
        User user = findByUsername(username);
        LoginUserInfo userInfo = userConvert.user2LoginUserInfo(user);
        redisUtil.set(BlogRequestConstant.USER_TOKEN_PREFIX + newJwtToken, JSON.toJSONString(userInfo), 1L, TimeUnit.HOURS);
        return newJwtToken;
    }

    @Override
    public void edit(UserEditDTO userEditDTO) {
        User user = userConvert.editDTO2User(userEditDTO);
        updateById(user);
    }


//    @Override
//    public String login(LoginDTO loginDTO) {
//
//        String loginResult = "登录失败";
//        //获取主题对象
//        Subject subject = SecurityUtils.getSubject();
//        try {
//            subject.login(new UsernamePasswordToken(loginDTO.getUsername(), loginDTO.getPassword(), true));
//            log.info("用户{}登录成功", loginDTO.getUsername());
//            // 登录成功后 用户上下文赋值
//            User user = userService.findByUsername(loginDTO.getUsername());
//            String jwtToken = jwtUtil.createJwtToken(user.getUsername(), 10 * 10);
//            redisUtil.set("token_" + jwtToken, JSON.toJSONString(user), 60*10L);
////            userContext.set(userConvert.user2LoginUserInfo(user));
//            return Result.success(jwtToken);
//        } catch (UnknownAccountException e) {
//            loginResult = "用户名错误";
//            log.error(loginResult);
//        } catch (IncorrectCredentialsException e) {
//            loginResult = "密码错误";
//            log.error(loginResult);
//        } catch (Exception e) {
//            log.error(loginResult + Throwables.getStackTraceAsString(e));
//        }
//        return Result.fail(loginResult);
//    }


    /**
     * 校验注册参数是否合法
     *
     * @param registerDTO
     */
    private void assertRegisterParamValid(UserRegisterDTO registerDTO) {
        Assert.notNull(registerDTO.getUsername(), "用户名不能为空");
        Assert.notNull(registerDTO.getPassword(), "密码不能为空");
    }


}
