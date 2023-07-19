package com.commons.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.commons.blog.convert.UserConvert;
import com.commons.blog.model.dto.user.LoginUserDTO;
import com.commons.blog.model.dto.user.UserRegisterDTO;
import com.commons.blog.model.entity.User;
import com.commons.blog.mapper.UserMapper;
import com.commons.blog.service.UserService;
import com.commons.blog.utils.SaltUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.Optional;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2023-07-10
 */
@Service
public class UserServiceImpl extends BaseSourceService<UserMapper, User> implements UserService {

    @Resource
    private UserConvert userConvert;

    @Override
    public User findByUsername(String username) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        User user = this.getOne(wrapper);
        return Optional.ofNullable(user).orElseThrow(() -> new IllegalArgumentException(String.format("未查询到此用户名的数据：【%s】", username)));
    }

    @Override
    public void register(LoginUserDTO loginUserDTO) {
        assertRegisterParamValid(loginUserDTO);
        User dbUser = findByUsername(loginUserDTO.getUsername());
        if (Objects.nonNull(dbUser)) {
            throw new IllegalArgumentException("用户名已存在");
        }
        User user = userConvert.registerDTO2User(loginUserDTO);
        // 5位随机盐
        user.setSalt(SaltUtil.getSalt());
        if (StringUtils.hasText(loginUserDTO.getPassword())) {

        }
    }

    @Override
    public void quickRegister(LoginUserDTO loginUserDTO) {


    }


    /**
     * 校验注册参数是否合法
     *
     * @param loginUserDTO
     */
    private void assertRegisterParamValid(LoginUserDTO loginUserDTO) {
        Assert.notNull(loginUserDTO.getUsername(), "用户名不能为空");
        Assert.notNull(loginUserDTO.getPassword(), "密码不能为空");
    }
}
