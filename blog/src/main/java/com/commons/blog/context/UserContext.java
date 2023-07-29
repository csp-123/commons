package com.commons.blog.context;

import com.alibaba.fastjson.JSON;
import com.commons.blog.convert.UserConvert;
import com.commons.blog.model.constant.BlogRequestConstant;
import com.commons.blog.model.dto.user.LoginDTO;
import com.commons.blog.model.dto.user.LoginUserInfo;
import com.commons.blog.model.entity.User;
import com.commons.blog.service.UserService;
import com.commons.blog.utils.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * 用户上下文 todo
 *
 * @author chishupeng
 * @date 2023/7/4 12:35 AM
 */
@Component
@RequiredArgsConstructor
public class UserContext {

    private static final String DEV = "dev";

    private static final ThreadLocal<LoginUserInfo> userContext = new ThreadLocal<>();

    /**
     * 用户信息 todo
     * @param userInfo
     */
    public void set(LoginUserInfo userInfo) {
        userContext.set(userInfo);
    }

    @Value("spring.profiles.active")
    private String env;


    /**
     * 获取登录用户
     * @return 用户信息
     */
    public LoginUserInfo getCurrentUser() {
        LoginUserInfo userInfo = userContext.get();
        if (needMock() && Objects.isNull(userInfo)) {
            userInfo = new LoginUserInfo();
            userInfo.setId(1L)
                    .setUsername("mock用户")
                    .setUserCode("mockUserCode");
        }
        return userInfo;
    }

    /**
     * 是否需要mock：
     * @return true or false
     */
    private boolean needMock() {
        return !DEV.equals(env);
    }


    public void clear() {
        userContext.remove();
    }
}
