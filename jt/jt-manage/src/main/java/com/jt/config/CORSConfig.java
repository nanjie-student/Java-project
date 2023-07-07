package com.jt.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CORSConfig implements WebMvcConfigurer {
    /*添加一个资源共享的策略
    * 参数说明：
    * 1.addMapping() 什么样的路径允许跨域
    * 2.allowedOrigins() 设定允许访问的网址
    * allowCredentials()是否允许携带cookie
    * allowedMethods()*/
    @Override
    public void addCorsMappings(CorsRegistry registry){
        registry.addMapping("/**")//设定允许访问的网址
        //.allowedOriginPatterns("*.jt.com")
        //.allowedOrigins("http://www.jt.com","http://manage.jt.com");
        //.allowedOrigins("*")//设定允许访问的网址
        //如果设定为true，则必须设定允许访问的网址，不可以用"*"
        .allowedOrigins("*")
        .allowCredentials(false);
        //.maxAge();30分钟
        //.allowedMethods("*");默认get方法，也可以是post，head
    }
}
