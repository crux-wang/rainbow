/*
 *  Copyright 2020. The Crux Authors
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package ren.crux.rainbow.test.demo.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import ren.crux.rainbow.test.demo.model.Article;

/**
 * 文章模块
 *
 * @author wangzhihui
 */
@RestController
@RequestMapping("/article")
public class ArticleController {
    /**
     * 新建文章
     *
     * @param userId  当前用户ID
     * @param article 文章详情
     * @return 文章详情
     */
    @PostMapping
    public Article create(@RequestHeader String userId, @RequestBody Article article) {
        return article;
    }

    /**
     * 更新文章详情
     *
     * @param userId  用户 ID
     * @param id      文章 ID
     * @param article 文章详情
     * @return 文章详情
     */
    @PostMapping("/{id}")
    public Article update(@RequestHeader String userId, @PathVariable String id, @RequestBody Article article) {
        article.setId(id);
        return article;
    }

    /**
     * 删除文章
     *
     * @param userId 用户 ID
     * @param id     文章 ID
     */
    @DeleteMapping("/{id}")
    public void delete(@RequestHeader String userId, @PathVariable String id) {
    }

    /**
     * 文章列表
     *
     * @param userId   用户 ID
     * @param keywords 关键词
     * @param pageable 分页信息
     * @return 文章列表
     */
    @GetMapping
    public Page<Article> article(@RequestHeader String userId, String keywords, @PageableDefault Pageable pageable) {
        return Page.empty(pageable);
    }

}
