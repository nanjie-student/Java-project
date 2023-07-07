package com.jt.controller;

import com.jt.pojo.User;
import com.jt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/findAll")
    public String findAll(Model model){
        //查询业务数据
        List<User> userList = userService.findAll();
        //将数据保存到request对象中
        model.addAttribute("userList", userList);
        return "userList";
    }

    /**
     * 跳转到ajax.jsp页面中
     */
    @RequestMapping("toAjax")
    public String toAjax(){

        return "ajax";
    }

    /**
     * 接收ajax请求: /findAjax
     * 返回值:  List<User>
     */
    @RequestMapping("/findAjax")
    @ResponseBody //1.将返回值结果转化为JSON数据返回   2.代表ajax请求结束
    public List<User> findAjax(){
        return userService.findAll();
    }



}
