package com.key.blogsimplify.service;

import com.key.blogsimplify.entity.User;

public interface UserService {
    boolean register(User user);
    String login(User user);
}
