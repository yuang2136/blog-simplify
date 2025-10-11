package com.key.blogsimplify.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
//拦截请求验证 Token
@Component
public class JwtInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");
        if (token == null || JwtUtil.validateToken(token) == null) {
            response.setStatus(401);
            response.getWriter().write("未授权或Token无效");
            return false;
        }
        return true;
    }
}
