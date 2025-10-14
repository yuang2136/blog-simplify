package com.key.blogsimplify.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.key.blogsimplify.entity.Comment;
import com.key.blogsimplify.mapper.CommentMapper;
import com.key.blogsimplify.service.impl.CommentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * {@link CommentServiceImpl} 的关键业务逻辑单元测试。
 */
@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @Mock
    private CommentMapper commentMapper;

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private ValueOperations<String, Object> valueOperations;

    private CommentServiceImpl commentService;

    @BeforeEach
    void setUp() {
        commentService = new CommentServiceImpl(commentMapper, redisTemplate);
    }

    @Test
    void addComment_shouldSetMetadataAndInvalidateCache() {
        Comment comment = new Comment();
        comment.setBlogId(10L);
        when(commentMapper.insert(comment)).thenReturn(1);

        boolean success = commentService.addComment(comment, "tester");

        assertThat(success).isTrue();
        assertThat(comment.getUserName()).isEqualTo("tester");
        assertThat(comment.getCreatedTime()).isBeforeOrEqualTo(LocalDateTime.now());
        verify(commentMapper).insert(comment);
        verify(redisTemplate).delete("BLOG_COMMENT::10");
    }

    @Test
    void listByBlogId_shouldReturnCacheHitWhenExists() {
        String cacheKey = "BLOG_COMMENT::10::1";
        Page<Comment> cachedPage = new Page<>();
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(cacheKey)).thenReturn(cachedPage);

        Page<Comment> result = commentService.listByBlogId(10L, 1, 5);

        assertThat(result).isSameAs(cachedPage);
        verify(commentMapper, never()).selectPage(any(), any());
    }

    @Test
    void listByBlogId_shouldQueryDatabaseAndCacheWhenMiss() {
        String cacheKey = "BLOG_COMMENT::10::2";
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(cacheKey)).thenReturn(null);
        Page<Comment> dbPage = new Page<>();
        when(commentMapper.selectPage(any(Page.class), any())).thenReturn(dbPage);

        Page<Comment> result = commentService.listByBlogId(10L, 2, 5);

        assertThat(result).isSameAs(dbPage);
        verify(commentMapper).selectPage(any(Page.class), any());
        verify(valueOperations).set(eq(cacheKey), eq(dbPage), eq(30L), eq(java.util.concurrent.TimeUnit.SECONDS));
    }
}
