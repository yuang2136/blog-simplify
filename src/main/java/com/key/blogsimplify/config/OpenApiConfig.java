package com.key.blogsimplify.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

/**
 * Springdoc OpenAPI 基础配置。
 * <p>
 * 提供文档元信息，并声明一个名为 "BearerAuth" 的安全方案，便于在 Swagger UI 中携带 JWT。
 */
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Blog Simplify API",
                description = "简版博客系统后台接口文档，包含用户、博客、评论等主要模块。",
                version = "v1.0.0",
                contact = @Contact(name = "Blog Simplify Team", email = "support@example.com")
        ),
        security = @SecurityRequirement(name = "BearerAuth")
)
@SecurityScheme(
        name = "BearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        description = "在 Authorization 中以 `Bearer <token>` 形式传递登录后获得的 JWT"
)
public class OpenApiConfig {
}
