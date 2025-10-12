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

@Service
public class CommentServiceImpl implements CommentService {

    private CommentMapper commentMapper;
    private RedisTemplate<String, Object> redisTemplate;

    public CommentServiceImpl(CommentMapper commentMapper, RedisTemplate<String, Object> redisTemplate) {
        this.commentMapper = commentMapper;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean addComment(Comment comment, String username) {
        comment.setCreatedTime(LocalDateTime.now());
        comment.setUserName(username);
        boolean success = commentMapper.insert(comment) > 0;
        if (success) {
            //清理redis缓存
            redisTemplate.delete("BLOG_COMMENT::" + comment.getBlogId());
        }
        return success;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Page<Comment> listByBlogId(Long blogId, int page, int size){
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

        //缓存30秒
        redisTemplate.opsForValue().set(cacheKey, result, 30, TimeUnit.SECONDS);
        return result;
    }
}
