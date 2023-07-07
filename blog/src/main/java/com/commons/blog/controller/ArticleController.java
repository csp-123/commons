package com.commons.blog.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.commons.blog.model.dto.ArticleDetailDTO;
import com.commons.blog.model.dto.ArticleEditDTO;
import com.commons.blog.model.dto.ArticlePageDTO;
import com.commons.blog.model.vo.ArticleDetailVO;
import com.commons.blog.model.vo.ArticlePageVO;
import com.commons.blog.service.ArticleService;
import com.commons.core.pojo.Response;
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
@RequestMapping("/blog/article")
public class ArticleController {

    @Resource
    private ArticleService articleService;


    /**
     * 创建
     * @param articleEditDTO 入参
     * @return id
     */
    @PostMapping("/create")
    public Response<Long> createArticle(@RequestBody ArticleEditDTO articleEditDTO) {
        Long id = articleService.saveArticle(articleEditDTO);
        return Response.ok(id);
    }


    /**
     * 编辑
     * @param articleEditDTO 入参
     * @return id
     */
    @PostMapping("/update")
    public Response<Long> updateArticle(@RequestBody ArticleEditDTO articleEditDTO) {
        Long id = articleService.updateArticle(articleEditDTO);
        return Response.ok(id);
    }


    /**
     * 分页
     * @param articlePageDTO 入参
     * @return
     */
    @PostMapping("/page")
    public Response<Page<ArticlePageVO>> page(@RequestBody ArticlePageDTO articlePageDTO) {
        Page<ArticlePageVO> page = articleService.pageArticle(articlePageDTO);
        return Response.ok(page);
    }


    @PostMapping("/detail")
    public Response<ArticleDetailVO> detail(@RequestBody ArticleDetailDTO articleDetailDTO) {
        ArticleDetailVO detailVO = articleService.detailArticle(articleDetailDTO);
        return Response.ok(detailVO);
    }

}
