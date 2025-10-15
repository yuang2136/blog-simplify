package com.key.blogsimplify.controller;

import com.key.blogsimplify.dto.AdminDashboardResponse;
import com.key.blogsimplify.dto.MessageResponse;
import com.key.blogsimplify.dto.PostStatusUpdateRequest;
import com.key.blogsimplify.dto.PostUpdateRequest;
import com.key.blogsimplify.dto.UserRoleUpdateRequest;
import com.key.blogsimplify.entity.Blog;
import com.key.blogsimplify.entity.User;
import com.key.blogsimplify.exception.NotFoundException;
import com.key.blogsimplify.service.BlogService;
import com.key.blogsimplify.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@Tag(name = "后台管理接口")
@SecurityRequirement(name = "BearerAuth")
public class AdminController {

    private final UserService userService;
    private final BlogService blogService;

    public AdminController(UserService userService, BlogService blogService) {
        this.userService = userService;
        this.blogService = blogService;
    }

    @GetMapping("/dashboard")
    @Operation(summary = "仪表盘统计信息")
    public ResponseEntity<AdminDashboardResponse> dashboard() {
        long published = blogService.countByPublished(true);
        long drafts = blogService.countByPublished(false);
        AdminDashboardResponse response = new AdminDashboardResponse(
                userService.countUsers(),
                blogService.countAll(),
                published,
                drafts
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/users")
    @Operation(summary = "查询全部用户")
    public ResponseEntity<List<User>> users() {
        return ResponseEntity.ok(userService.findAll());
    }

    @PutMapping("/users/{id}/role")
    @Operation(summary = "更新用户角色")
    public ResponseEntity<MessageResponse> updateRole(
            @PathVariable Long id,
            @Valid @RequestBody UserRoleUpdateRequest request
    ) {
        boolean updated = userService.updateRole(id, request.role());
        if (!updated) {
            throw new NotFoundException("用户不存在");
        }
        return ResponseEntity.ok(new MessageResponse("角色更新成功"));
    }

    @DeleteMapping("/users/{id}")
    @Operation(summary = "删除用户")
    public ResponseEntity<MessageResponse> deleteUser(@PathVariable Long id) {
        if (!userService.deleteUser(id)) {
            throw new NotFoundException("用户不存在");
        }
        return ResponseEntity.ok(new MessageResponse("用户已删除"));
    }

    @GetMapping("/posts")
    @Operation(summary = "查询全部文章（含草稿）")
    public ResponseEntity<List<Blog>> posts() {
        return ResponseEntity.ok(blogService.findAllForAdmin());
    }

    @PutMapping("/posts/{id}")
    @Operation(summary = "更新文章内容")
    public ResponseEntity<MessageResponse> updatePost(
            @PathVariable Long id,
            @Valid @RequestBody PostUpdateRequest request
    ) {
        Blog blog = blogService.findById(id);
        if (blog == null) {
            throw new NotFoundException("文章不存在");
        }
        blog.setTitle(request.title());
        blog.setContent(request.content());
        blog.setAuthor(request.author());
        if (request.published() != null) {
            blog.setPublished(request.published());
        }
        if (!blogService.update(blog)) {
            throw new NotFoundException("文章不存在");
        }
        return ResponseEntity.ok(new MessageResponse("文章更新成功"));
    }

    @PatchMapping("/posts/{id}/status")
    @Operation(summary = "更新文章发布状态")
    public ResponseEntity<MessageResponse> updatePostStatus(
            @PathVariable Long id,
            @Valid @RequestBody PostStatusUpdateRequest request
    ) {
        if (!blogService.updatePublishStatus(id, request.published())) {
            throw new NotFoundException("文章不存在");
        }
        return ResponseEntity.ok(new MessageResponse("状态更新成功"));
    }
}
