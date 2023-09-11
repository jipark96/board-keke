package com.example.board.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {

        // 모든 경로에 앞으로 만들 모든 CORS 정보를 적용
        registry.addMapping("/**")
                // Header의 Origin에 들어있는 주소가 http://localhost:3000인 경우를 허용
                .allowedOrigins("http://localhost:3000")
                // 모든 HTTP Method를 허용
                .allowedMethods("*")
                // HTTP 요청의 Header에 어떤 값이든 들어갈 수 있도록 허용
                .allowedHeaders("*")
                // 자격증명 사용을 허용
                .allowCredentials(true);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploadedImages/**")
                .addResourceLocations("file:///C:/Users/user/IdeaProjects/board/src/main/resources/uploadedImages/");
    }
}
