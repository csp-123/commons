package com.commons.blog.model.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;


import java.util.Collection;
import java.util.List;

/**
 * 登录用户
 *
 * @author chishupeng
 * @date 2023/7/11 7:21 PM
 */
@Getter
@Setter
@Accessors(chain = true)
@ApiModel("登录用户")
public class LoginUserDTO {

    /**
     * 用户id
     */
    @ApiModelProperty("id")
    private Long id;

    /**
     * 用户名
     */
    @ApiModelProperty("用户名")
    private String username;

    /**
     * 密码
     */
    @ApiModelProperty("密码")
    private String password;

    /**
     * userCode
     */
    @ApiModelProperty("用户编码")
    private String userCode;

    /**
     * 权限
     */
    @ApiModelProperty("权限")
    String authorities;



}
