package com.example.vitalis;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 配置允许跨域的路径，允许所有路径（"/**"）
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173")  // 允许的前端域名（如你的前端运行在localhost:5173）
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // 允许的请求方法
                .allowedHeaders("*")  // 允许所有请求头
                .allowCredentials(true);  // 允许携带凭证（如cookie）
    }
}

