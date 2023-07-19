package com.commons.blog.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.commons.blog.model.entity.BaseSourceEntity;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author baomidou
 * @since 2023-07-10
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("blog_user")
public class User extends BaseSourceEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 用户编码
     */
    @TableField("user_code")
    private Long userCode;

    /**
     * 用户名，同用户账号
     */
    @TableField("username")
    private String username;

    /**
     * 真实姓名
     */
    @TableField("true_name")
    private String trueName;

    /**
     * 密码
     */
    @TableField("password")
    private String password;

    /**
     * 密码盐
     */
    @TableField("salt")
    private String salt;

    /**
     * 手机号
     */
    @TableField("phone")
    private String phone;

    /**
     * 邮箱
     */
    @TableField("email")
    private String email;

    /**
     * 性别，0：男，1：女，默认男
     */
    @TableField("gender")
    private Boolean gender;

    /**
     * 出生日期
     */
    @TableField("birthday")
    private LocalDateTime birthday;

    /**
     * Gravatar头像地址
     */
    @TableField("gravatar_url")
    private String gravatarUrl;

    /**
     * 社交信息json
     */
    @TableField("social_info")
    private String socialInfo;

    /**
     * 地址
     */
    @TableField("address")
    private String address;

    /**
     * 个人描述
     */
    @TableField("description")
    private String description;

    /**
     * 昵称
     */
    @TableField("nickname")
    private String nickname;
}
