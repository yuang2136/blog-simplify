package com.key.blogsimplify.service;

import com.key.blogsimplify.entity.User;

/**
 * 用户模块业务接口。
 * <p>
 * 通过接口定义对外提供的功能，方便控制层与具体实现解耦。
 */
public interface UserService {

    /**
     * 注册新用户。
     *
     * @param user 注册信息
     * @return 是否注册成功
     */
    boolean register(User user);

    /**
     * 校验登录并返回 token。
     *
     * @param user 登录凭证
     * @return 成功返回 JWT token，失败返回 null
     */
    String login(User user);
}
