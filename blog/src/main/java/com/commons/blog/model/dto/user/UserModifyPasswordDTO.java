package com.commons.blog.model.dto.user;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 修改密码DTO
 *
 * @author chishupeng
 * @date 2023/7/29 9:50 PM
 */
@Getter
@Setter
@Accessors(chain = true)
@ApiModel("用户更改密码")
public class UserModifyPasswordDTO {

    /**
     * 新密码
     */
    @Size(max = 20, message = "密码超过最大长度{max}")
    @NotBlank
    @ApiModelProperty("新密码")
    private String password;

    /**
     * 确认新密码
     */
    @Size(max = 20, message = "密码超过最大长度{max}")
    @NotBlank
    @ApiModelProperty("确认新密码")
    private String confirmPassword;

    /**
     * 原密码
     */
    @Size(max = 20, message = "密码超过最大长度{max}")
    @NotBlank
    @ApiModelProperty("原密码")
    private String oldPassword;

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
    @Size(min = 4, max = 6, message = "验证码长度应为{min}-{max}")
    @NotBlank
    @ApiModelProperty("验证码")
    private String captcha;

}
