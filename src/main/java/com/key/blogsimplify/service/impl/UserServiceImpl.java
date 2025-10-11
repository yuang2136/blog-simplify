package com.key.blogsimplify.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.key.blogsimplify.config.JwtUtil;
import com.key.blogsimplify.entity.User;
import com.key.blogsimplify.mapper.UserMapper;
import com.key.blogsimplify.service.UserService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final StringRedisTemplate redisTemplate;

    public UserServiceImpl(UserMapper userMapper, StringRedisTemplate redisTemplate) {
        this.userMapper = userMapper;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean register(User user) {
        return userMapper.insert(user) > 0;
    }

    @Override
    public String login(User user) {
        User dbUser = userMapper.selectOne(
                new QueryWrapper<User>().eq("username", user.getUsername()).eq("password", user.getPassword())
        );
        if (dbUser == null) return null;

        String token = JwtUtil.generateToken(dbUser.getUsername());
        redisTemplate.opsForValue().set("TOKEN:" + dbUser.getUsername(), token, 1, TimeUnit.HOURS);
        return token;
    }
}
