package com.key.blogsimplify.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.key.blogsimplify.entity.Blog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BlogMapper extends BaseMapper<Blog> {
}
