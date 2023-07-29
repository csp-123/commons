package com.commons.blog.model.dto.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 用户信息
 *
 * @author chishupeng
 * @date 2023/7/4 12:41 AM
 */
@Data
@Accessors(chain = true)
public class UserQuickRegisterDTO {

    /**
     * 用户id
     */
    @ApiModelProperty("id")
    private Long id;

    /**
     * 用户名称
     */
    @Size(max = 20, message = "用户名超过最大长度{max}")
    @ApiModelProperty("用户名")
    private String username;

    /**
     * 手机号
     */
    @Size(min = 11, max = 11, message = "手机号长度应为{max}")
    @NotBlank
    @ApiModelProperty("手机号")
    private String phone;

    /**
     * 验证码
     */
    @Size(max = 10, message = "验证码超过最大长度{max}")
//    @NotBlank
    @ApiModelProperty("验证码")
    private String captcha;

}
