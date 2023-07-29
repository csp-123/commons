package com.commons.blog.controller.manage;

import com.commons.blog.model.constant.BlogRequestConstant;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author baomidou
 * @since 2023-07-10
 */
@RestController
@RequestMapping(BlogRequestConstant.REQUEST_MANAGE_PREFIX + "/user")
@Api(tags = "用户-MANAGE")
public class UserManageController {

}
