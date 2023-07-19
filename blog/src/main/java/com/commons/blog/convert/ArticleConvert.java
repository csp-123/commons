package com.commons.blog.convert;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.commons.blog.model.dto.article.ArticleEditDTO;
import com.commons.blog.model.entity.Article;
import com.commons.blog.model.vo.article.ArticleDetailVO;
import com.commons.blog.model.vo.article.ArticlePageVO;
import org.mapstruct.Mapper;

/**
 * 文章转换器
 *
 * @author chishupeng
 * @date 2023/7/5 7:13 PM
 */
@Mapper(componentModel = "spring")
public abstract class ArticleConvert {

    /**
     * 编辑dto 2 entity
     * @param articleEditDTO
     * @return article
     */
    public abstract Article editTO2Entity(ArticleEditDTO articleEditDTO);

    /**
     * entity 2 detailVO
     * @param article article
     * @return ArticleDetailVO
     */
    public abstract ArticleDetailVO entity2DetailVO(Article article);

    /**
     * entity 2 pageVO
     * @param article article
     * @return pageVO
     */
    public abstract ArticlePageVO entity2PageVO(Article article);

    /**
     * Page<Article> 2 Page<ArticleVO>
     * @param page articlePage
     * @return voPage
     */
    public abstract Page<ArticlePageVO> entityPage2VOPage(Page<Article> page);
}
