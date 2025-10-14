package com.key.blogsimplify.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.key.blogsimplify.entity.Comment;
import com.key.blogsimplify.mapper.CommentMapper;
import com.key.blogsimplify.service.CommentService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * {@link CommentService} 的实现类，包含 Redis 缓存策略。
 * <p>
 * 写评论时清除缓存，查评论时优先命中缓存，降低数据库压力。
 */
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentMapper commentMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    public CommentServiceImpl(CommentMapper commentMapper, RedisTemplate<String, Object> redisTemplate) {
        this.commentMapper = commentMapper;
        this.redisTemplate = redisTemplate;
    }

    /**
     * 新增评论：补齐创建时间、记录用户名，写入成功后清理缓存。
     */
    @Override
    public boolean addComment(Comment comment, String username) {
        comment.setCreatedTime(LocalDateTime.now());
        comment.setUserName(username);
        boolean success = commentMapper.insert(comment) > 0;
        if (success) {
            // 删除缓存，让后续查询能够看到最新评论
            redisTemplate.delete("BLOG_COMMENT::" + comment.getBlogId());
        }
        return success;
    }

    /**
     * 分页查询评论：先查缓存，未命中再查数据库并写入缓存。
     * 缓存键格式：BLOG_COMMENT::{blogId}::{page}。
     */
    @Override
    @SuppressWarnings("unchecked")
    public Page<Comment> listByBlogId(Long blogId, int page, int size) {
        String cacheKey = "BLOG_COMMENT::" + blogId + "::" + page;
        Page<Comment> cachePage = (Page<Comment>) redisTemplate.opsForValue().get(cacheKey);
        if (cachePage != null) {
            return cachePage;
        }

        Page<Comment> pageInfo = new Page<>(page, size);
        Page<Comment> result = commentMapper.selectPage(
                pageInfo,
                new QueryWrapper<Comment>()
                        .eq("blog_id", blogId)
                        .orderByDesc("created_time")
        );

        redisTemplate.opsForValue().set(cacheKey, result, 30, TimeUnit.SECONDS);
        return result;
    }
}
