package com.spacedog;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsMvcConfig implements WebMvcConfigurer {

    /**
     * Mvc 컨트롤러로 오는 요청에 대해 cors  설정
     * 시큐리티 필터를 타는 로그인 기능은 스프링 시큐리티 설정파일에 따로 해줘야한다 (SecruityConfig)
     *
     */

    @Override
    public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping("/**") //모든 컨트롤러 경로에 대해서
                .allowedOrigins("http://localhost:3000"); // 허용할 요청을 한 포트
    }
}
