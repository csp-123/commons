package com.commons.blog.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.commons.blog.convert.ArticleConvert;
import com.commons.blog.enums.SourceTypeEnum;
import com.commons.blog.model.dto.ArticleDetailDTO;
import com.commons.blog.model.dto.ArticleEditDTO;
import com.commons.blog.model.dto.ArticlePageDTO;
import com.commons.blog.model.entity.Article;
import com.commons.blog.mapper.ArticleMapper;
import com.commons.blog.model.vo.ArticleDetailVO;
import com.commons.blog.model.vo.ArticlePageVO;
import com.commons.blog.service.ArticleService;
import com.commons.blog.utils.ParamAssertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 文章 服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2023-07-04
 */
@Service
public class ArticleServiceImpl extends BaseSourceServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Resource
    private ArticleConvert articleConvert;

    /**
     * 文章保存
     * @param articleEditDTO 文章
     * @return id
     */
    @Override
    public Long saveArticle(ArticleEditDTO articleEditDTO) {
        Assert.isNull(articleEditDTO.getId(), "创建文章不可指定id");
        Article article = articleConvert.editTO2Entity(articleEditDTO);
        SourceTypeEnum typeEnum = SourceTypeEnum.of(articleEditDTO.getSourceType());
        ParamAssertUtil.assertEqual(typeEnum, SourceTypeEnum.ARTICLE, "类型错误");
        return saveReturnId(article);
    }

    /**
     * 文章更新
     * @param articleEditDTO 文章
     * @return id
     */
    @Override
    public Long updateArticle(ArticleEditDTO articleEditDTO) {
        Assert.notNull(articleEditDTO.getId(), "编辑文章需指定id");
        Assert.isTrue(exist(articleEditDTO.getId()), "文章不存在");
        Article article = articleConvert.editTO2Entity(articleEditDTO);
        SourceTypeEnum typeEnum = SourceTypeEnum.of(articleEditDTO.getSourceType());
        ParamAssertUtil.assertEqual(typeEnum, SourceTypeEnum.ARTICLE, "类型错误");
        updateById(article);
        return article.getId();
    }

    /**
     * 分页查询
     * @param articlePageDTO 分页查询参数
     * @return 分页vo
     */
    @Override
    public Page<ArticlePageVO> pageArticle(ArticlePageDTO articlePageDTO) {
        LambdaQueryChainWrapper<Article> pageWrapper = new LambdaQueryChainWrapper<>(this.getBaseMapper());
        pageWrapper.like(StringUtils.hasText(articlePageDTO.getTitle()), Article::getTitle, articlePageDTO.getTitle());
        Page<Article> page = getBaseMapper().selectPage(new Page<>(), pageWrapper);
        return articleConvert.entityPage2VOPage(page);
    }

    /**
     * 详情
     * @param articleDetailDTO 详情查询参数
     * @return 详情vo
     */
    @Override
    public ArticleDetailVO detailArticle(ArticleDetailDTO articleDetailDTO) {
        LambdaQueryChainWrapper<Article> wrapper = lambdaQuery();
        wrapper.eq(Article::getId, articleDetailDTO.getId());
        return this.getObj(wrapper, article -> articleConvert.entity2DetailVO(article));
    }


}
