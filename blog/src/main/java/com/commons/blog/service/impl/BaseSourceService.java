package com.commons.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.commons.blog.context.UserContext;
import com.commons.blog.model.annotation.SourceTypeAnno;
import com.commons.blog.model.dto.user.LoginUserInfo;
import com.commons.blog.model.enums.SourceStatusEnum;
import com.commons.blog.generator.RedisIdGenerator;
import com.commons.blog.model.dto.user.LoginDTO;
import com.commons.blog.model.entity.BaseSourceEntity;
import com.commons.blog.model.enums.SourceTypeEnum;
import com.commons.core.enums.CommonStatusEnum;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Date;

@Service
public abstract class BaseSourceService<M extends BaseMapper<T>, T extends BaseSourceEntity> extends ServiceImpl<M, T> {

    @Resource
    private UserContext userContext;

    @Resource
    private RedisIdGenerator redisIdGenerator;

//    @Override
    public Long saveReturnId(T t) {
        return saveReturn(t).getId();
    }

//    @Override
    public T saveReturn(T t) {

        SourceTypeEnum sourceTypeEnum = this.getClass().getAnnotation(SourceTypeAnno.class).type();

        LoginUserInfo userInfo = userContext.getCurrentUser();
        t
                .setSourceId(redisIdGenerator.redisIdGenerate(t.getSourceType()))
                .setSourceStatus(SourceStatusEnum.INITIAL.getStatus())
                .setSourceType(sourceTypeEnum.getStatus())
                .setCreateTime(new Date())
                .setCreateUserId(userInfo.getId())
                .setCreateUserCode(userInfo.getUserCode())
                .setCreateUserName(userInfo.getUsername())
                .setIsDeleted(CommonStatusEnum.getFalse())
                .setVersion(1L);
        save(t);
        return t;
    }

//    @Override
    public Long saveReturnSourceId(T t) {
        return saveReturn(t).getSourceId();
    }


//    @Override
    public T findById(Long id) {
        T t = getById(id);
        Assert.notNull(t, String.format("未查询到指定数据，id:[%s]", id));
        return t;
    }

//    @Override
    public T findBySourceId(Long sourceId) {
        LambdaQueryWrapper<T> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(T::getId, sourceId);
        T t = getOne(wrapper);
        Assert.notNull(t, String.format("未查询到指定资源，sourceId:[%s]", sourceId));
        return t;
    }

//    @Override
    public boolean exist(Long id) {
        LambdaQueryWrapper<T> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(T::getId, id);
        return this.getBaseMapper().exists(wrapper);
    }


//    @Override
    public boolean sourceExist(Long sourceId) {
        LambdaQueryWrapper<T> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(T::getSourceId, sourceId);
        return this.getBaseMapper().exists(wrapper);
    }
}
