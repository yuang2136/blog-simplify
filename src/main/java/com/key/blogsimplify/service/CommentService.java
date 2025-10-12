package com.key.blogsimplify.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.key.blogsimplify.entity.Comment;

public interface CommentService {
    boolean addComment(Comment comment, String username);
    Page<Comment> listByBlogId(Long blogId, int page, int size);
}
