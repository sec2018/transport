package com.example.transport.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;


@Configuration
@EnableWebMvc
public class CustomWebMvcConfigurerAdapter extends WebMvcConfigurerAdapter {

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //拦截所有的controller
        registry.addInterceptor(loginInterceptor).addPathPatterns("/*")
                .excludePathPatterns("/login")
                .excludePathPatterns("/sublogin*");
        super.addInterceptors(registry);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/adminlte/**").addResourceLocations("classpath:/adminlte/");
        registry.addResourceHandler("/js/**").addResourceLocations("classpath:/js/");
        registry.addResourceHandler("/layui/**").addResourceLocations("classpath:/layui/");
        registry.addResourceHandler("/img/**").addResourceLocations("classpath:/img/");
        super.addResourceHandlers(registry);
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        registry.jsp("/templates/",".html");
        super.configureViewResolvers(registry);
    }


}
