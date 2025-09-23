package com.ts2.centr.controllers;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class Config implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // твои загруженные файлы
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:./uploads/");

        // стандартные статики (css, js, images)
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
    }
}
