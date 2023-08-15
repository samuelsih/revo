package com.revo.application.config;

import com.revo.application.interceptor.GetCurrentUserInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final GetCurrentUserInterceptor currentUserInterceptor;

    @Autowired
    public WebConfig(GetCurrentUserInterceptor currentUser) {
        this.currentUserInterceptor = currentUser;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.currentUserInterceptor);
    }
}
