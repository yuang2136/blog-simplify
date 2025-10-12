package com.key.blogsimplify.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.key.blogsimplify.config.JwtUtil;
import com.key.blogsimplify.entity.Comment;
import com.key.blogsimplify.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    //添加评论：自动识别 Token
    @PostMapping("/add")
    public String add(@RequestBody Comment comment, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        String username = JwtUtil.validateToken(token);
        if(username == null) return "未登录或Token无效";
        return commentService.addComment(comment, username) ? "评论成功" : "评论失败";
    }

    //        分页查询评论
    @GetMapping("/list/{blogId}")
    public Page<Comment> list(
            @PathVariable Long blogId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        return commentService.listByBlogId(blogId, page, size);
    }
}
