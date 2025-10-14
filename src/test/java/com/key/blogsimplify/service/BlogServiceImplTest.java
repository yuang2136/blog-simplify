package com.key.blogsimplify.service;

import com.key.blogsimplify.entity.Blog;
import com.key.blogsimplify.mapper.BlogMapper;
import com.key.blogsimplify.service.impl.BlogServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 针对 {@link BlogServiceImpl} 的单元测试，聚焦核心业务行为。
 */
@org.junit.jupiter.api.extension.ExtendWith(MockitoExtension.class)
class BlogServiceImplTest {

    @Mock
    private BlogMapper blogMapper;

    private BlogServiceImpl blogService;

    @BeforeEach
    void setUp() {
        blogService = new BlogServiceImpl(blogMapper);
    }

    @Test
    void findAll_shouldDelegateToMapper() {
        List<Blog> blogs = List.of(new Blog());
        when(blogMapper.selectList(any())).thenReturn(blogs);

        List<Blog> result = blogService.findAll();

        assertThat(result).isEqualTo(blogs);
        verify(blogMapper).selectList(any());
    }

    @Test
    void findById_shouldReturnMapperResult() {
        Blog blog = new Blog();
        when(blogMapper.selectById(1L)).thenReturn(blog);

        Blog result = blogService.findById(1L);

        assertThat(result).isSameAs(blog);
        verify(blogMapper).selectById(1L);
    }

    @Test
    void add_shouldPopulateCreatedTimeAndInsert() {
        Blog blog = new Blog();
        when(blogMapper.insert(blog)).thenReturn(1);

        boolean inserted = blogService.add(blog);

        assertThat(inserted).isTrue();
        assertThat(blog.getCreatedTime()).isNotNull();
        assertThat(blog.getCreatedTime()).isBeforeOrEqualTo(LocalDateTime.now());
        verify(blogMapper).insert(blog);
    }

    @Test
    void delete_shouldCallMapperDelete() {
        when(blogMapper.deleteById(1L)).thenReturn(1);

        boolean deleted = blogService.delete(1L);

        assertThat(deleted).isTrue();
        verify(blogMapper).deleteById(1L);
    }
}
