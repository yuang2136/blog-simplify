package com.key.blogsimplify.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.key.blogsimplify.entity.Blog;
import com.key.blogsimplify.mapper.BlogMapper;
import com.key.blogsimplify.service.BlogService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BlogServiceImpl implements BlogService {

    private final BlogMapper blogMapper;

    public BlogServiceImpl(BlogMapper blogMapper) {
        this.blogMapper = blogMapper;
    }

    @Override
    public List<Blog> findAll() {
        return blogMapper.selectList(new QueryWrapper<>());
    }

    @Override
    public Blog findById(Long id) {
        return blogMapper.selectById(id);
    }

    @Override
    public boolean add(Blog blog) {
        blog.setCreatedTime(LocalDateTime.now());
        return blogMapper.insert(blog) > 0;
    }

    @Override
    public boolean delete(Long id) {
        return blogMapper.deleteById(id) > 0;
    }
}
