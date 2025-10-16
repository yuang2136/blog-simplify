package com.key.blogsimplify.exception;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

@Schema(description = "统一错误响应")
public record ApiError(
        @Schema(description = "HTTP 状态码") int status,
        @Schema(description = "错误简述") String error,
        @Schema(description = "详细错误描述") String message,
        @Schema(description = "时间戳") Instant timestamp
) {
    public static ApiError of(int status, String error, String message) {
        return new ApiError(status, error, message, Instant.now());
    }
}
