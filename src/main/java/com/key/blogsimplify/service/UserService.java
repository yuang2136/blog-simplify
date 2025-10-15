package com.key.blogsimplify.service;

import com.key.blogsimplify.dto.AuthResponse;
import com.key.blogsimplify.entity.User;

import java.util.List;

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
    AuthResponse login(User user);

    /** 查询全部用户，后台管理使用。 */
    List<User> findAll();

    /** 更新用户角色。 */
    boolean updateRole(Long userId, String role);

    /** 删除用户。 */
    boolean deleteUser(Long userId);

    /** 统计用户数量。 */
    long countUsers();
}
