package com.key.blogsimplify.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.key.blogsimplify.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

/**
 * 评论表 Mapper，复用 MyBatis-Plus 提供的基础 CRUD 能力。
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
}
