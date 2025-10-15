package com.key.blogsimplify.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "后台仪表盘统计信息")
public record AdminDashboardResponse(
        @Schema(description = "用户总数") long totalUsers,
        @Schema(description = "文章总数") long totalPosts,
        @Schema(description = "已发布文章数") long publishedPosts,
        @Schema(description = "未发布文章数") long draftPosts
) {
}
