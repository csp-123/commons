package com.commons.blog.controller.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.commons.blog.constant.BlogRequestConstant;
import com.commons.blog.model.dto.article.ArticleDetailDTO;
import com.commons.blog.model.dto.article.ArticleEditDTO;
import com.commons.blog.model.dto.article.ArticlePageDTO;
import com.commons.blog.model.vo.article.ArticleDetailVO;
import com.commons.blog.model.vo.article.ArticlePageVO;
import com.commons.blog.service.ArticleService;
import com.commons.core.pojo.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 文章 前端控制器
 * </p>
 *
 * @author baomidou
 * @since 2023-07-04
 */
@RestController
@RequestMapping(BlogRequestConstant.REQUEST_WEB_PREFIX + "/article")
@Api(tags = "文章")
public class ArticleWebController {

    @Resource
    private ArticleService articleService;


    /**
     * 分页
     * @param articlePageDTO 入参
     * @return
     */
    @PostMapping("/page")
    @ApiOperation("分页")
    public Response<Page<ArticlePageVO>> page(@RequestBody ArticlePageDTO articlePageDTO) {
        Page<ArticlePageVO> page = articleService.pageArticle(articlePageDTO);
        return Response.ok(page);
    }


    @PostMapping("/detail")
    @ApiOperation("详情")
    public Response<ArticleDetailVO> detail(@RequestBody ArticleDetailDTO articleDetailDTO) {
        ArticleDetailVO detailVO = articleService.detailArticle(articleDetailDTO);
        return Response.ok(detailVO);
    }

}