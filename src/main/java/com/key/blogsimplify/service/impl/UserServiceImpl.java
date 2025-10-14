package com.key.blogsimplify.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.key.blogsimplify.config.JwtUtil;
import com.key.blogsimplify.entity.User;
import com.key.blogsimplify.mapper.UserMapper;
import com.key.blogsimplify.service.UserService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 用户业务实现类：处理注册与登录。
 * <p>
 * 登录成功后会生成 JWT，并将 token 缓存到 Redis，方便后续拦截器校验与统一失效。
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final StringRedisTemplate redisTemplate;

    public UserServiceImpl(UserMapper userMapper, StringRedisTemplate redisTemplate) {
        this.userMapper = userMapper;
        this.redisTemplate = redisTemplate;
    }

    /** 调用 MyBatis-Plus 的 insert 方法完成注册。 */
    @Override
    public boolean register(User user) {
        return userMapper.insert(user) > 0;
    }

    /**
     * 登录流程：
     * 1. 根据用户名和密码查询数据库（示例项目使用明文，实际应加密处理）；
     * 2. 未查询到则直接返回 null，由控制层反馈失败；
     * 3. 查询到则生成 JWT，写入 Redis 并设置 1 小时过期。
     */
    @Override
    public String login(User user) {
        User dbUser = userMapper.selectOne(
                new QueryWrapper<User>()
                        .eq("username", user.getUsername())
                        .eq("password", user.getPassword())
        );
        if (dbUser == null) {
            return null;
        }

        String token = JwtUtil.generateToken(dbUser.getUsername());
        redisTemplate.opsForValue().set("TOKEN:" + dbUser.getUsername(), token, 1, TimeUnit.HOURS);
        return token;
    }
}
