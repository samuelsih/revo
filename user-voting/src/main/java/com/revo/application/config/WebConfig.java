package com.revo.application.config;

import com.revo.application.interceptor.GetCurrentUserInterceptor;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {
    @Bean
    public GetCurrentUserInterceptor getCurrentUserInterceptor() {
        return new GetCurrentUserInterceptor();
    }

    @Bean
    public WebMvcConfigurer registerInterceptors() {
        return new WebMvcConfigurer() {
            @Override
            public void addInterceptors(@NonNull InterceptorRegistry registry) {
                registry.addInterceptor(getCurrentUserInterceptor())
                        .excludePathPatterns("/warmup", "_ah/warmup");
            }
        };
    }
}
