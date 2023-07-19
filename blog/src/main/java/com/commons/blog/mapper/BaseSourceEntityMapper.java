package com.commons.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.commons.blog.model.entity.BaseSourceEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 文章 Mapper 接口
 * </p>
 *
 * @author baomidou
 * @since 2023-07-04
 */
@Mapper
public interface BaseSourceEntityMapper extends BaseMapper<BaseSourceEntity> {

}
