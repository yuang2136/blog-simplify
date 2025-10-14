package com.key.blogsimplify.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.key.blogsimplify.entity.Comment;

/**
 * 评论业务接口。
 * <p>
 * 描述评论模块对外提供的功能。
 */
public interface CommentService {

    /**
     * 新增评论并写入评论者用户名。
     *
     * @param comment 评论实体
     * @param username 当前登录用户
     * @return 是否新增成功
     */
    boolean addComment(Comment comment, String username);

    /**
     * 根据博客 ID 分页查询评论。
     *
     * @param blogId 博客主键
     * @param page   页码
     * @param size   每页条数
     * @return 分页对象
     */
    Page<Comment> listByBlogId(Long blogId, int page, int size);
}
