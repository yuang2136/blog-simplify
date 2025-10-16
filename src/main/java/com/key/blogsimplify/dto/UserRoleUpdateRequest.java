package com.key.blogsimplify.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "后台用户角色更新请求")
public record UserRoleUpdateRequest(
        @NotBlank
        @Schema(description = "新的角色值，例如 USER 或 ADMIN") String role
) {
}
