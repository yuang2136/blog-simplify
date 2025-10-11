package com.key.blogsimplify.controller;

import com.key.blogsimplify.entity.User;
import com.key.blogsimplify.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public String register(@RequestBody User user) {
        return userService.register(user) ? "注册成功" : "注册失败";
    }

    @PostMapping("/login")
    public String login(@RequestBody User user) {
        String token = userService.login(user);
        return token != null ? token : "用户名或密码错误";
    }
}
