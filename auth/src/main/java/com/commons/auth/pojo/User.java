package com.commons.auth.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.commons.core.pojo.BasePOJO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author baomidou
 * @since 2022-09-26
 */
@Getter
@Setter
@TableName("commons_user")
public class User extends BasePOJO {

    private static final long serialVersionUID = 1L;

    /**
     * 用户名
     */
    @TableField("user_name")
    private String userName;

    /**
     * 用户密码
     */
    @TableField("password")
    private String password;

    /**
     * 绑定手机
     */
    @TableField("phone")
    private String phone;

    /**
     * 邮箱
     */
    @TableField("email")
    private String email;

    /**
     * 性别 1-男，2-女 
     */
    @TableField("gender")
    private Byte gender;

    /**
     * 昵称
     */
    @TableField("nick_name")
    private String nickName;

    /**
     * 出生日期
     */
    @TableField("birth")
    private LocalDate birth;

    /**
     * 住址
     */
    @TableField("address")
    private String address;
}
