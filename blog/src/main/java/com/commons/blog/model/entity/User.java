package com.commons.blog.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 用户
 *
 * @author chishupeng
 * @date 2023/7/4 12:37 AM
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("blog_user")
public class User {
}
