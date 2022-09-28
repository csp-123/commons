package com.commons.core.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @description: 抽象POJO类
 * @author: cccsp
 * @date: 2022/9/19 16:08
 */
@Getter
@Setter
@Accessors(chain = true)
public class BasePOJO {

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    /**
     * 创建人ID
     */
    @TableField(value = "create_user")
    private Integer createBy;


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
     * 删除标识 0-未删除  1-已删除
     */
    @TableField(value = "is_deleted")
    private Boolean isDeleted;

}
