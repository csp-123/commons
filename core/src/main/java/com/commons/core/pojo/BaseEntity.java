package com.commons.core.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import lombok.experimental.Accessors;

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
@Accessors(chain = true)
public class BaseEntity<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.INPUT)
    private T id;

    /**
     * 创建人ID
     */
    @TableField(value = "create_user_id")
    private Long createUserId;

    /**
     * 创建人编码
     */
    @TableField(value = "create_user_code")
    private String createUserCode;


    /**
     * 创建人用户名
     */
    @TableField(value = "create_user_name")
    private String createUserName;


    /**
     * 修改人ID
     */
    @TableField(value = "update_user_id")
    private Long updateUserId;

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
    @TableLogic(value = "0", delval = "1")
    private byte isDeleted;

    /**
     * 删除时间
     */
    @TableField(value = "delete_time")
    private Date deleted_time;

    /**
     * 版本
     */
    @TableField(value = "version")
    private Long version;

}
