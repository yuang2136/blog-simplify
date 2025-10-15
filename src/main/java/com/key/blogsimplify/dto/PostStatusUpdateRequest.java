package com.key.blogsimplify.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "后台文章状态更新请求")
public record PostStatusUpdateRequest(
        @NotNull
        @Schema(description = "是否发布") Boolean published
) {
}
