package com.key.blogsimplify.controller;

import com.key.blogsimplify.entity.Blog;
import com.key.blogsimplify.service.BlogService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 博客模块的 REST 接口。
 * <p>
 * 控制层只负责处理 HTTP 请求与响应，核心业务逻辑交给 {@link BlogService}，
 * 既保持代码清晰，又方便后续编写单元测试。
 */
@RestController
@RequestMapping("/blog")
public class BlogController {

    private final BlogService blogService;

    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    /** 获取全部博客文章，对应 GET /blog/list。 */
    @GetMapping("/list")
    public List<Blog> list() {
        return blogService.findAll();
    }

    /** 根据主键查询博客详情，对应 GET /blog/{id}。 */
    @GetMapping("/{id}")
    public Blog findOne(@PathVariable Long id) {
        return blogService.findById(id);
    }

    /**
     * 新增博客文章，对应 POST /blog/add。
     *
     * @param blog 前端传入的博客实体
     * @return 操作是否成功的提示语
     */
    @PostMapping("/add")
    public String add(@RequestBody Blog blog) {
        return blogService.add(blog) ? "添加成功" : "添加失败";
    }

    /**
     * 删除博客文章，对应 DELETE /blog/delete/{id}。
     *
     * @param id 博客主键
     * @return 删除结果提示
     */
    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        return blogService.delete(id) ? "删除成功" : "删除失败";
    }
}
