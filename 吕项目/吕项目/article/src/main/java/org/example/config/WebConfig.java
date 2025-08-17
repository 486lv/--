package org.example.config;

import org.example.filter.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//配置类
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private LoginInterceptor loginInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //把拦截器注册进去
        //登录和注册不拦截，要放行
        registry.addInterceptor(loginInterceptor).excludePathPatterns("/user/login","/user/register","/hdfs/*","/article/hadoop","/hadoop","/hadoop/show","/api/*");
    }

}
