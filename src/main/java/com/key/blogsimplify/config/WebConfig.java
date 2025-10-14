package com.key.blogsimplify.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web 层统一配置。
 * <p>
 * 主要职责：
 * 1. 注册 {@link JwtInterceptor}，为受保护的接口统一进行 Token 校验；
 * 2. 注册 MyBatis-Plus 的分页插件，让分页查询自动生效。
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final JwtInterceptor jwtInterceptor;

    public WebConfig(JwtInterceptor jwtInterceptor) {
        this.jwtInterceptor = jwtInterceptor;
    }

    /**
     * 配置拦截器链：对 /blog/** 路径启用鉴权，放行登录与注册接口。
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/blog/**")
                .excludePathPatterns("/user/login", "/user/register");
    }

    /**
     * 注册 MyBatis-Plus 分页插件，只要在 Mapper 方法中使用 Page 参数即可自动生成分页 SQL。
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return interceptor;
    }
}
