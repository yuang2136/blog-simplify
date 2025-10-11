package com.key.blogsimplify.controller;

import com.key.blogsimplify.entity.Blog;
import com.key.blogsimplify.service.BlogService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/blog")
public class BlogController {

    private final BlogService blogService;

    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    @GetMapping("/list")
    public List<Blog> list() {
        return blogService.findAll();
    }

    @GetMapping("/{id}")
    public Blog findOne(@PathVariable Long id) {
        return blogService.findById(id);
    }

    @PostMapping("/add")
    public String add(@RequestBody Blog blog) {
        return blogService.add(blog) ? "添加成功" : "添加失败";
    }

    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        return blogService.delete(id) ? "删除成功" : "删除失败";
    }
}
