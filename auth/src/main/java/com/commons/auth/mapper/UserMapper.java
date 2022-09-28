package com.commons.auth.mapper;

import com.commons.auth.pojo.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author baomidou
 * @since 2022-09-26
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
