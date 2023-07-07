package com.commons.blog.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.commons.blog.model.dto.ArticleDetailDTO;
import com.commons.blog.model.dto.ArticleEditDTO;
import com.commons.blog.model.dto.ArticlePageDTO;
import com.commons.blog.model.entity.Article;
import com.commons.blog.model.vo.ArticleDetailVO;
import com.commons.blog.model.vo.ArticlePageVO;

/**
 * <p>
 * 文章 服务类
 * </p>
 *
 * @author baomidou
 * @since 2023-07-04
 */
public interface ArticleService extends BaseSourceService<Article> {

    /**
     * 文章保存
     * @param articleEditDTO 文章
     * @return id
     */
    Long saveArticle(ArticleEditDTO articleEditDTO);


    /**
     * 文章编辑
     * @param articleEditDTO 文章
     * @return id
     */
    Long updateArticle(ArticleEditDTO articleEditDTO);

    /**
     * 分页查询
     * @param articlePageDTO 分页查询参数
     * @return 分页vo
     */
    Page<ArticlePageVO> pageArticle(ArticlePageDTO articlePageDTO);

    /**
     * 详情
     * @param articleDetailDTO 详情查询参数
     * @return 详情vo
     */
    ArticleDetailVO detailArticle(ArticleDetailDTO articleDetailDTO);


}
