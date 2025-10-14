package com.key.blogsimplify.controller;

import com.key.blogsimplify.entity.User;
import com.key.blogsimplify.service.UserService;
import org.springframework.web.bind.annotation.*;

/**
 * 用户注册与登录相关的 REST 接口。
 * <p>
 * 核心业务逻辑由 {@link UserService} 负责，控制层负责接收参数、返回结果。
 */
@RestController
@RequestMapping("/user")
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
    public String register(@RequestBody User user) {
        return userService.register(user) ? "注册成功" : "注册失败";
    }

    /**
     * 用户登录，对应 POST /user/login。
     *
     * @param user 登录凭证
     * @return 成功返回 JWT 字符串，失败时返回提示语
     */
    @PostMapping("/login")
    public String login(@RequestBody User user) {
        String token = userService.login(user);
        return token != null ? token : "用户名或密码错误";
    }
}
