package com.key.blogsimplify.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.key.blogsimplify.entity.Blog;
import com.key.blogsimplify.mapper.BlogMapper;
import com.key.blogsimplify.service.BlogService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * {@link BlogService} 的默认实现，基于 MyBatis-Plus 提供的通用 Mapper。
 * 负责处理博客的核心业务，例如补齐创建时间、调用 Mapper 执行数据库操作等。
 */
@Service
public class BlogServiceImpl implements BlogService {

    private final BlogMapper blogMapper;

    public BlogServiceImpl(BlogMapper blogMapper) {
        this.blogMapper = blogMapper;
    }

    /** 查询全部博客，不附加任何条件。 */
    @Override
    public List<Blog> findAll() {
        return blogMapper.selectList(new QueryWrapper<>());
    }

    /** 根据主键查询博客。 */
    @Override
    public Blog findById(Long id) {
        return blogMapper.selectById(id);
    }

    /**
     * 新增博客时补齐创建时间，然后调用 MyBatis-Plus 完成持久化。
     */
    @Override
    public boolean add(Blog blog) {
        blog.setCreatedTime(LocalDateTime.now());
        return blogMapper.insert(blog) > 0;
    }

    /** 根据主键删除博客。 */
    @Override
    public boolean delete(Long id) {
        return blogMapper.deleteById(id) > 0;
    }
}
