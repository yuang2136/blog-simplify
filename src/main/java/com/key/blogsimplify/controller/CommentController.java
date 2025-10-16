package com.key.blogsimplify.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.key.blogsimplify.entity.Comment;
import com.key.blogsimplify.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import com.key.blogsimplify.dto.MessageResponse;
import com.key.blogsimplify.exception.BusinessException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 评论模块的 REST 接口。
 * <p>
 * 控制层会从请求头中解析 JWT，获取当前登录用户并传递给业务层，
 * 业务层只关注“保存评论”“查询评论”等核心逻辑。
 */
@RestController
@RequestMapping("/comment")
@Tag(name = "评论接口")
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
    @Operation(summary = "新增评论")
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<MessageResponse> add(@RequestBody Comment comment, HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        if (username == null) {
            throw new BusinessException("未登录或Token无效");
        }
        boolean success = commentService.addComment(comment, username);
        if (!success) {
            throw new BusinessException("评论失败");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("评论成功"));
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
    @Operation(summary = "分页查询评论")
    public ResponseEntity<Page<Comment>> list(
            @PathVariable Long blogId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        return ResponseEntity.ok(commentService.listByBlogId(blogId, page, size));
    }
}
