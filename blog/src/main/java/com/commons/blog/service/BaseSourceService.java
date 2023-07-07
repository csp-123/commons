package com.commons.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.commons.blog.model.entity.Article;
import com.commons.blog.model.entity.BaseSourceEntity;

/**
 * 资源服务
 *
 * @author chishupeng
 * @date 2023/7/3 11:44 PM
 */
public interface BaseSourceService<T extends BaseSourceEntity> extends IService<T> {

    /**
     * 保存并返回主键
     * @param t 数据
     * @return 主键id
     */
    Long saveReturnId(T t);

    /**
     * 保存并返回对象
     * @param t 数据
     * @return 带主键的对象
     */
    T saveReturn(T t);

    /**
     * 保存并返回资源编码
     *
     * @param t 数据
     * @return
     */
    Long saveReturnSourceId(T t);


    /**
     * 根据id查询
     * @param id id
     * @return T
     */
    T findById(Long id);

    /**
     * 根据资源id查询
     * @param sourceId id
     * @return T
     */
    T findBySourceId(Long sourceId);

    /**
     * 查询指定id是否存在
     * @param id id
     * @return true or false
     */
    boolean exist(Long id);

    /**
     * 查询指定资源id是否存在
     * @param sourceId id
     * @return true or false
     */
    boolean sourceExist(Long sourceId);
}
