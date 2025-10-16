package com.key.blogsimplify.controller;

import com.key.blogsimplify.dto.AuthResponse;
import com.key.blogsimplify.dto.MessageResponse;
import com.key.blogsimplify.entity.User;
import com.key.blogsimplify.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 用户注册与登录相关的 REST 接口。
 * <p>
 * 核心业务逻辑由 {@link UserService} 负责，控制层负责接收参数、返回结果。
 */
@RestController
@RequestMapping("/user")
@Tag(name = "用户接口")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 用户注册，对应 POST /user/register。
     *
     * @param user 注册信息（用户名、密码）
     * @return 注册结果提示
     */
    @PostMapping("/register")
    @Operation(summary = "注册新用户")
    public ResponseEntity<MessageResponse> register(@RequestBody User user) {
        userService.register(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("注册成功"));
    }

    /**
     * 用户登录，对应 POST /user/login。
     *
     * @param user 登录凭证
     * @return 成功返回 JWT 字符串，失败时返回提示语
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录，返回 JWT")
    public ResponseEntity<AuthResponse> login(@RequestBody User user) {
        return ResponseEntity.ok(userService.login(user));
    }
}
