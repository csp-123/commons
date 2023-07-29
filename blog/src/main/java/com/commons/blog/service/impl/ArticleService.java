package com.commons.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.commons.blog.convert.ArticleConvert;
import com.commons.blog.model.annotation.SourceTypeAnno;
import com.commons.blog.model.dto.OrderCondition;
import com.commons.blog.model.enums.SourceTypeEnum;
import com.commons.blog.model.dto.article.ArticleDetailDTO;
import com.commons.blog.model.dto.article.ArticleEditDTO;
import com.commons.blog.model.dto.article.ArticlePageDTO;
import com.commons.blog.model.entity.Article;
import com.commons.blog.mapper.ArticleMapper;
import com.commons.blog.model.vo.article.ArticleDetailVO;
import com.commons.blog.model.vo.article.ArticlePageVO;
import com.commons.core.util.ParamAssertUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 文章 服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2023-07-04
 */
@Service
@SourceTypeAnno(type = SourceTypeEnum.ARTICLE)
public class ArticleService extends BaseSourceService<ArticleMapper, Article> implements com.commons.blog.service.ArticleService {

    @Resource
    private ArticleConvert articleConvert;

    /**
     * 文章保存
     *
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
     *
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
     *
     * @param articlePageDTO 分页查询参数
     * @return 分页vo
     */
    @Override
    public Page<ArticlePageVO> pageArticle(ArticlePageDTO articlePageDTO) {
        // todo
        LambdaQueryWrapper<Article> pageWrapper = new LambdaQueryWrapper<>();
        pageWrapper.like(StringUtils.hasText(articlePageDTO.getTitle()), Article::getTitle, articlePageDTO.getTitle());
        Page<Article> articlePage = new Page<>();
        List<OrderItem> orderItems = articlePageDTO.getOrderBy()
                .stream()
                .sorted((o1, o2) -> o2.getPriority().compareTo(o1.getPriority()))
                .collect(Collectors.toList());
        articlePage.addOrder(orderItems);
        Page<Article> page = baseMapper.selectPage(articlePage, pageWrapper);
        return articleConvert.entityPage2VOPage(page);
    }

    /**
     * 详情
     *
     * @param articleDetailDTO 详情查询参数
     * @return 详情vo
     */
    @Override
    public ArticleDetailVO detailArticle(ArticleDetailDTO articleDetailDTO) {
        Article article = findById(articleDetailDTO.getId());
        return articleConvert.entity2DetailVO(article);
    }


}
