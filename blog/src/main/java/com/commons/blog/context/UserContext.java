package com.commons.blog.context;

import com.commons.blog.model.dto.user.LoginUserDTO;
import com.commons.blog.model.dto.user.UserRegisterDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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

    private static final ThreadLocal<LoginUserDTO> userContext = new ThreadLocal<>();

    /**
     * 用户信息 todo
     * @param dto
     */
    public void set(LoginUserDTO dto) {
        userContext.set(dto);
    }

    @Value("spring.profiles.active")
    private String env;


    /**
     * 获取登录用户
     * @return 用户信息
     */
    public LoginUserDTO getCurrentUser() {
        LoginUserDTO loginUserDTO = userContext.get();
        if (needMock() && Objects.isNull(loginUserDTO)) {
            loginUserDTO = new LoginUserDTO();
            loginUserDTO.setId(1L)
                    .setUsername("mock用户")
                    .setUserCode("mockUserCode");
        }
        return loginUserDTO;
    }

    /**
     * 是否需要mock：
     * @return true or false
     */
    private boolean needMock() {
        return !DEV.equals(env);
    }

}
