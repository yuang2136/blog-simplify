package com.key.blogsimplify.service;

import com.key.blogsimplify.entity.User;
import com.key.blogsimplify.mapper.UserMapper;
import com.key.blogsimplify.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * {@link UserServiceImpl} 的核心行为单元测试。
 */
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private StringRedisTemplate redisTemplate;

    @Mock
    private ValueOperations<String, String> stringValueOperations;

    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userMapper, redisTemplate);
    }

    @Test
    void register_shouldDelegateToMapper() {
        User user = new User();
        when(userMapper.insert(user)).thenReturn(1);

        boolean success = userService.register(user);

        assertThat(success).isTrue();
        verify(userMapper).insert(user);
    }

    @Test
    void login_shouldReturnTokenAndCacheWhenUserExists() {
        User input = new User();
        input.setUsername("coder");
        input.setPassword("123456");

        User dbUser = new User();
        dbUser.setUsername("coder");

        when(redisTemplate.opsForValue()).thenReturn(stringValueOperations);
        when(userMapper.selectOne(any())).thenReturn(dbUser);

        String token = userService.login(input);

        assertThat(token).isNotNull();
        verify(userMapper).selectOne(any());
        verify(stringValueOperations).set("TOKEN:coder", token, 1, java.util.concurrent.TimeUnit.HOURS);
    }

    @Test
    void login_shouldReturnNullWhenUserNotFound() {
        when(userMapper.selectOne(any())).thenReturn(null);

        String token = userService.login(new User());

        assertThat(token).isNull();
        verify(stringValueOperations, never()).set(any(), any(), anyLong(), any());
    }
}
