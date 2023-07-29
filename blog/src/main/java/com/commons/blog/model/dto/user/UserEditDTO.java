package com.commons.blog.model.dto.user;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 用户信息dto
 *
 * @author chishupeng
 * @date 2023/7/29 9:47 PM
 */
@Getter
@Setter
@Accessors(chain = true)
@ApiModel("编辑用户")
public class UserEditDTO {

    /**
     * id
     */
    @NotNull
    @ApiModelProperty("id")
    private Long id;

    /**
     * 真实姓名
     */
    @ApiModelProperty("真实姓名")
    private String trueName;

    /**
     * 邮箱
     */
    @ApiModelProperty("邮箱")
    private String email;

    /**
     * 性别，0：男，1：女，默认男
     */
    @ApiModelProperty("性别，0：男，1：女，默认男")
    private Boolean gender;

    /**
     * 出生日期
     */
    @ApiModelProperty("出生日期")
    private LocalDateTime birthday;

    /**
     * Gravatar头像地址
     */
    @ApiModelProperty("Gravatar头像地址")
    private String gravatarUrl;

    /**
     * 社交信息json
     */
    @ApiModelProperty("社交信息json")
    private String socialInfo;

    /**
     * 地址
     */
    @ApiModelProperty("地址")
    private String address;

    /**
     * 个人描述
     */
    @ApiModelProperty("个人描述")
    private String description;

    /**
     * 昵称
     */
    @ApiModelProperty("昵称")
    private String nickname;

}
