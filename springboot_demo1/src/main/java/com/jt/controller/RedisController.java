package com.jt.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//指定配置文件进行加载
@PropertySource("classpath:/properties/redis.properties")
public class RedisController {
    /**
     * 当程序启动时,会自动的加载YML配置文件,将数据保存到Spring的内部.
     * 之后通过${key}(spel表达式)进行数据的取值.
     * 1.通过YML方式赋值
     *
     */
    @Value("${redis.host}")
    private String host;        // = "127.0.0.1";
    @Value("${redis.port}")
    private int port;           // = 6379;

    //2.通过pro方式赋值
    @Value("${redis2.host}")
    private String host2;
    @Value("${redis2.port}")
    private int port2;


    @RequestMapping("/getNode")
    public String getNode(){
        return "YML取值方式"+host + ":" + port+"| " +
                "PRO取值方式:"+host2+":"+port2;
    }

}
