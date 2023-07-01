package com.commons.core.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Date;

/**
 * Description:
 *      基类
 * Project: commons
 * Author: cccsp
 * Create Time:2022/9/19 16:08
 */
@Data
public class BasePOJO<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private T id;

    /**
     * 创建人ID
     */
    @TableField(value = "create_user_id")
    private Integer createUserId;

    /**
     * 创建人编码
     */
    @TableField(value = "create_user_code")
    private Integer createUserCode;


    /**
     * 创建人用户名
     */
    @TableField(value = "create_user_name")
    private String createUserName;


    /**
     * 修改人ID
     */
    @TableField(value = "update_user")
    private Integer updateUser;

    /**
     * 修改人编码
     */
    @TableField(value = "update_user_code")
    private String updateUserCode;

    /**
     * 修改人用户名
     */
    @TableField(value = "update_user_name")
    private String updateUserName;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 删除标识 0-未删除  1-已删除
     */
    @TableField(value = "is_deleted")
    private Boolean isDeleted;

    /**
     * 删除时间
     */
    @TableField(value = "delete_time")
    private Boolean deleted_time;

    /**
     * 版本
     */
    @TableField(value = "version")
    private Long version;

}
