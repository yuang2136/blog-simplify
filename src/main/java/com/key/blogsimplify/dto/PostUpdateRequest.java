package com.key.blogsimplify.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "后台文章更新请求")
public record PostUpdateRequest(
        @NotBlank
        @Size(max = 255)
        @Schema(description = "文章标题") String title,
        @NotBlank
        @Schema(description = "文章正文") String content,
        @Schema(description = "文章作者") String author,
        @Schema(description = "是否发布") Boolean published
) {
}
