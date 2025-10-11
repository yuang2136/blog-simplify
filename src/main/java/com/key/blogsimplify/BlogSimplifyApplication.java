package com.key.blogsimplify;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BlogSimplifyApplication {
	public static void main(String[] args) {
		SpringApplication.run(BlogSimplifyApplication.class, args);
		System.out.println("🚀 BlogSimplify 启动成功：http://localhost:8080/swagger-ui/");
	}
}
