package com.key.blogsimplify;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 应用程序入口。
 * <p>
 * {@link SpringBootApplication} 会开启组件扫描、自动配置等 Spring Boot 基础能力，
 * 让容器在启动时自动装配项目中定义的 Bean。
 */
@SpringBootApplication
public class BlogSimplifyApplication {

	/**
	 * main 方法是 Java 程序的入口。
	 * Spring Boot 会在此处启动 IOC 容器，启动完成后打印出 Swagger UI 的访问地址。
	 */
	public static void main(String[] args) {
		SpringApplication.run(BlogSimplifyApplication.class, args);
		System.out.println("🚀 BlogSimplify 启动成功：http://localhost:8080/swagger-ui/");
	}
}
