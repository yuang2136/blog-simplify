package com.key.blogsimplify.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.key.blogsimplify.config.JwtUtil;
import com.key.blogsimplify.entity.Comment;
import com.key.blogsimplify.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

/**
 * 评论模块的 REST 接口。
 * <p>
 * 控制层会从请求头中解析 JWT，获取当前登录用户并传递给业务层，
 * 业务层只关注“保存评论”“查询评论”等核心逻辑。
 */
@RestController
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * 新增评论，对应 POST /comment/add。
     * <p>
     * 控制层负责：
     * 1. 从请求头读取 Authorization 字段；
     * 2. 交给 {@link JwtUtil} 验证并解析用户名；
     * 3. 将用户名与评论数据一起交给业务层。
     */
    @PostMapping("/add")
    public String add(@RequestBody Comment comment, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        String username = JwtUtil.validateToken(token);
        if (username == null) {
            return "未登录或Token无效";
        }
        return commentService.addComment(comment, username) ? "评论成功" : "评论失败";
    }

    /**
     * 分页查询评论，对应 GET /comment/list/{blogId}。
     *
     * @param blogId 博客文章主键
     * @param page   页码（默认第 1 页）
     * @param size   每页记录数（默认 5 条）
     * @return MyBatis-Plus 的分页对象，包含评论集合与分页元数据
     */
    @GetMapping("/list/{blogId}")
    public Page<Comment> list(
            @PathVariable Long blogId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        return commentService.listByBlogId(blogId, page, size);
    }
}
