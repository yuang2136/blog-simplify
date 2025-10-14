package com.key.blogsimplify.service;

import com.key.blogsimplify.entity.Blog;
import java.util.List;

/**
 * 博客业务接口，定义博客模块提供的核心能力。
 * 具体实现见 {@link com.key.blogsimplify.service.impl.BlogServiceImpl}。
 */
public interface BlogService {

    /** 查询全部博客文章。 */
    List<Blog> findAll();

    /**
     * 根据主键查询博客。
     *
     * @param id 博客主键
     * @return 查询结果，可能为 null
     */
    Blog findById(Long id);

    /**
     * 新增博客。
     *
     * @param blog 待保存的博客实体
     * @return 是否保存成功
     */
    boolean add(Blog blog);

    /**
     * 根据主键删除博客。
     *
     * @param id 博客主键
     * @return 是否删除成功
     */
    boolean delete(Long id);
}
