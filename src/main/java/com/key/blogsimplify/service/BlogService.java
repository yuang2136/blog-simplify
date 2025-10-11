package com.key.blogsimplify.service;

import com.key.blogsimplify.entity.Blog;
import java.util.List;

public interface BlogService {
    List<Blog> findAll();
    Blog findById(Long id);
    boolean add(Blog blog);
    boolean delete(Long id);
}
