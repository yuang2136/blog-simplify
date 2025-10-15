package com.key.blogsimplify.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "通用成功消息响应")
public record MessageResponse(@Schema(description = "提示信息") String message) {
}
