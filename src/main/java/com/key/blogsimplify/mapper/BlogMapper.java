package com.key.blogsimplify.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.key.blogsimplify.entity.Blog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 博客表的 Mapper，继承 {@link BaseMapper} 后即可使用通用 CRUD 方法。
 */
@Mapper
public interface BlogMapper extends BaseMapper<Blog> {
}
