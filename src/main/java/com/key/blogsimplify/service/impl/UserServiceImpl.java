package com.key.blogsimplify.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.key.blogsimplify.config.JwtUtil;
import com.key.blogsimplify.dto.AuthResponse;
import com.key.blogsimplify.entity.User;
import com.key.blogsimplify.mapper.UserMapper;
import com.key.blogsimplify.service.UserService;
import com.key.blogsimplify.exception.BusinessException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
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
        long count = userMapper.selectCount(new QueryWrapper<User>().eq("username", user.getUsername()));
        if (count > 0) {
            throw new BusinessException("用户名已存在");
        }
        user.setRole("USER");
        return userMapper.insert(user) > 0;
    }

    /**
     * 登录流程：
     * 1. 根据用户名和密码查询数据库（示例项目使用明文，实际应加密处理）；
     * 2. 未查询到则直接返回 null，由控制层反馈失败；
     * 3. 查询到则生成 JWT，写入 Redis 并设置 1 小时过期。
     */
    @Override
    public AuthResponse login(User user) {
        User dbUser = userMapper.selectOne(
                new QueryWrapper<User>()
                        .eq("username", user.getUsername())
                        .eq("password", user.getPassword())
        );
        if (dbUser == null) {
            throw new BusinessException("用户名或密码错误");
        }

        String token = JwtUtil.generateToken(dbUser.getUsername(), dbUser.getRole());
        redisTemplate.opsForValue().set("TOKEN:" + dbUser.getUsername(), token, 1, TimeUnit.HOURS);
        return new AuthResponse(token, dbUser.getUsername(), dbUser.getRole(), JwtUtil.getExpirationSeconds());
    }

    @Override
    public List<User> findAll() {
        List<User> users = userMapper.selectList(new QueryWrapper<>());
        users.forEach(user -> user.setPassword(null));
        return users;
    }

    @Override
    public boolean updateRole(Long userId, String role) {
        if (role == null) {
            throw new BusinessException("角色不能为空");
        }
        String normalizedRole = role.trim().toUpperCase();
        if (!normalizedRole.equals("USER") && !normalizedRole.equals("ADMIN")) {
            throw new BusinessException("不支持的角色类型: " + role);
        }
        User existing = userMapper.selectById(userId);
        if (existing == null) {
            return false;
        }
        existing.setRole(normalizedRole);
        boolean updated = userMapper.updateById(existing) > 0;
        if (updated) {
            redisTemplate.delete("TOKEN:" + existing.getUsername());
        }
        return updated;
    }

    @Override
    public boolean deleteUser(Long userId) {
        User existing = userMapper.selectById(userId);
        if (existing == null) {
            return false;
        }
        boolean deleted = userMapper.deleteById(userId) > 0;
        if (deleted) {
            redisTemplate.delete("TOKEN:" + existing.getUsername());
        }
        return deleted;
    }

    @Override
    public long countUsers() {
        return userMapper.selectCount(new QueryWrapper<>());
    }
}
