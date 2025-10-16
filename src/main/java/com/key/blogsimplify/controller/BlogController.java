package com.key.blogsimplify.controller;

import com.key.blogsimplify.dto.MessageResponse;
import com.key.blogsimplify.entity.Blog;
import com.key.blogsimplify.exception.BusinessException;
import com.key.blogsimplify.exception.NotFoundException;
import com.key.blogsimplify.service.BlogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@Tag(name = "博客接口")
@SecurityRequirement(name = "BearerAuth")
public class BlogController {

    private final BlogService blogService;

    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    /** 获取全部博客文章，对应 GET /blog/list。 */
    @GetMapping("/list")
    @Operation(summary = "查询全部已发布文章")
    public ResponseEntity<List<Blog>> list() {
        return ResponseEntity.ok(blogService.findAll());
    }

    /** 根据主键查询博客详情，对应 GET /blog/{id}。 */
    @GetMapping("/{id}")
    @Operation(summary = "查询文章详情")
    public ResponseEntity<Blog> findOne(@PathVariable Long id) {
        Blog blog = blogService.findById(id);
        if (blog == null || Boolean.FALSE.equals(blog.getPublished())) {
            throw new NotFoundException("文章不存在");
        }
        return ResponseEntity.ok(blog);
    }

    /**
     * 新增博客文章，对应 POST /blog/add。
     *
     * @param blog 前端传入的博客实体
     * @return 操作是否成功的提示语
     */
    @PostMapping("/add")
    @Operation(summary = "新增文章")
    public ResponseEntity<MessageResponse> add(@RequestBody Blog blog) {
        boolean success = blogService.add(blog);
        if (!success) {
            throw new BusinessException("添加文章失败");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("添加成功"));
    }

    /**
     * 删除博客文章，对应 DELETE /blog/delete/{id}。
     *
     * @param id 博客主键
     * @return 删除结果提示
     */
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "删除文章")
    public ResponseEntity<MessageResponse> delete(@PathVariable Long id) {
        return blogService.delete(id)
                ? ResponseEntity.ok(new MessageResponse("删除成功"))
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("文章不存在"));
    }
}
