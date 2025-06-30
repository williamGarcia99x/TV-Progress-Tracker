package com.cognixia.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final TokenAuthInterceptor tokenAuthInterceptor;

    @Autowired
    public WebMvcConfig(TokenAuthInterceptor tokenAuthInterceptor) {
        this.tokenAuthInterceptor = tokenAuthInterceptor;
    }

    // Register the TokenAuthInterceptor to intercept requests to the /tracker endpoint
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(tokenAuthInterceptor)
                .addPathPatterns("/api/tracker/**", "api/auth/logout"); // protect POST /track and PUT /update-tracking


    }

}
