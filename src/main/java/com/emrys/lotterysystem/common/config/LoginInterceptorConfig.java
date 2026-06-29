package com.emrys.lotterysystem.common.config;

import com.emrys.lotterysystem.common.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 *
 * @author Emrys
 * content:
 */
@Configuration
public class LoginInterceptorConfig implements WebMvcConfigurer {
    @Autowired
    LoginInterceptor loginInterceptor;
    private final List<String> excludePaths = List.of(
            "/**/*.html",
            "/css/**",
            "/js/**",
            "/pic/**",
            "/*.jpg",
            "/*.png",
            "/favicon.ico",
            "/user/**",
            "/**/login",
            "/prize/create",  //
            "/verification-code/send",
            "/winning-records/show"); // 排除登录和注册请求
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**") // 拦截所有请
                .excludePathPatterns(excludePaths); // 排除登录和注册请求
    }
}
