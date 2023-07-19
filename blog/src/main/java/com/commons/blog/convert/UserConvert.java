package com.commons.blog.convert;

import com.commons.blog.model.dto.user.LoginUserDTO;
import com.commons.blog.model.dto.user.UserRegisterDTO;
import com.commons.blog.model.entity.User;
import org.mapstruct.Mapper;

/**
 * 用户转换器
 *
 * @author chishupeng
 * @date 2023/7/4 12:38 AM
 */
@Mapper(componentModel = "spring")
public abstract class UserConvert {


    /**
     * 注册dto 2 user
     * @param loginUserDTO
     * @return
     */
    public abstract User registerDTO2User(LoginUserDTO loginUserDTO);
}