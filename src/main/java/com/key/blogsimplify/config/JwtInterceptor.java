package com.key.blogsimplify.config;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.util.Optional;

/**
 * JWT 拦截器：在请求进入控制器之前校验 Token。
 * <p>
 * 通过 {@link WebConfig} 注册到拦截器链中，可对受保护的接口统一做鉴权。
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {

    private final StringRedisTemplate redisTemplate;

    public JwtInterceptor(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 控制器方法执行前触发，校验请求头中的 Authorization。
     *
     * @return true 表示放行；false 表示拦截并直接返回 401/403
     */
    @Override
    public boolean preHandle(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler
    ) throws Exception {
        String token = JwtUtil.resolveToken(request.getHeader("Authorization"));
        if (token == null) {
            writeError(response, HttpServletResponse.SC_UNAUTHORIZED, "未授权或Token无效");
            return false;
        }

        Optional<Claims> claimsOptional = JwtUtil.parseToken(token);
        if (claimsOptional.isEmpty()) {
            writeError(response, HttpServletResponse.SC_UNAUTHORIZED, "未授权或Token无效");
            return false;
        }

        Claims claims = claimsOptional.get();
        String username = claims.getSubject();
        String cachedToken = redisTemplate.opsForValue().get("TOKEN:" + username);
        if (cachedToken == null || !cachedToken.equals(token)) {
            writeError(response, HttpServletResponse.SC_UNAUTHORIZED, "Token 已失效，请重新登录");
            return false;
        }

        String role = JwtUtil.extractRole(claims);
        request.setAttribute("username", username);
        request.setAttribute("role", role);

        if (request.getRequestURI().startsWith("/admin") && (role == null || !"ADMIN".equalsIgnoreCase(role))) {
            writeError(response, HttpServletResponse.SC_FORBIDDEN, "需要管理员权限");
            return false;
        }
        return true;
    }

    private void writeError(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json;charset=UTF-8");
        String sanitizedMessage = message.replace("\"", "\\\"");
        String body = String.format("{\"status\":%d,\"message\":\"%s\"}", status, sanitizedMessage);
        response.getWriter().write(body);
    }
}
