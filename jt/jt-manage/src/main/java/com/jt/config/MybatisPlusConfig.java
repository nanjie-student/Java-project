package com.jt.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration   //bean标签使用
public class MybatisPlusConfig {

    //将分页拦截器交了Spring容器管理 MP是Mybatis的增强工具
    @Bean
    public PaginationInterceptor paginationInterceptor(){

        return new PaginationInterceptor();
    }
}
