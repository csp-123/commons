package com.commons.blog.model.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 用户信息
 *
 * @author chishupeng
 * @date 2023/7/4 12:41 AM
 */
@Data
@Accessors(chain = true)
public class UserDTO {

    /**
     * 用户id
     */
    private Long id;

    /**
     * 用户编码
     */
    private String code;

    /**
     * 用户名称
     */
    private String name;

}
