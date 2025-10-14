package com.key.blogsimplify.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * JWT 拦截器：在请求进入控制器之前校验 Token。
 * <p>
 * 通过 {@link WebConfig} 注册到拦截器链中，可对受保护的接口统一做鉴权。
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {

    /**
     * 控制器方法执行前触发，校验请求头中的 Authorization。
     *
     * @return true 表示放行；false 表示拦截并直接返回 401
     */
    @Override
    public boolean preHandle(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler
    ) throws Exception {
        String token = request.getHeader("Authorization");
        if (token == null || JwtUtil.validateToken(token) == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("未授权或Token无效");
            return false;
        }
        return true;
    }
}
