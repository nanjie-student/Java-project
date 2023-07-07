package com.jt.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PortController {
    @Value("${server.port}")
    private int port;
    @RequestMapping("/getPost")
    public String getPost(){
        return "当前端口号："+port;
    }
}
