package com.key.blogsimplify.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.key.blogsimplify.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
