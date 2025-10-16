package com.key.blogsimplify.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 登录成功后返回给前端的响应。
 */
@Schema(description = "登录成功后的响应数据")
public record AuthResponse(
        @Schema(description = "JWT 访问令牌") String token,
        @Schema(description = "登录用户名") String username,
        @Schema(description = "用户角色") String role,
        @Schema(description = "token 过期时间，单位：秒") long expiresIn
) {
}
