package com.commons.blog.model.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

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
public class LoginDTO {

    /**
     * 用户id
     */
    @ApiModelProperty("id")
    private Long id;

    /**
     * 用户名
     */
    @ApiModelProperty("用户名")
    @Size(max = 20, message = "用户名超过最大长度{max}")
    @NotBlank
    private String username;

    /**
     * 密码
     */
    @ApiModelProperty("密码")
    @Size(max = 20, message = "密码超过最大长度{max}")
    @NotBlank
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
