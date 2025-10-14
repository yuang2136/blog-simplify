package com.key.blogsimplify.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.key.blogsimplify.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户表 Mapper，直接继承 {@link BaseMapper} 获得通用方法。
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
