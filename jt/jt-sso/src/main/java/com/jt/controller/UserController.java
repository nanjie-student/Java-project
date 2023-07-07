package com.jt.controller;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jt.pojo.User;
import com.jt.service.UserService;
import com.jt.vo.SysResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisCluster;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private JedisCluster jedisCluster;
    @RequestMapping("/test")
    public String testuser(){
        return "单点测试系统";
    }
    /*jsonp
    *实现用户数据校验
    * url:http://sso.jt.com/user/check/{param}/{type}
    * 参数：/{param} 用户需要校验的数据/
    * {type} 校验的字段
    * 返回值类型：sysResult对象*/
    @RequestMapping("/check/{param}/{type}")
    public JSONPObject checkUser(@PathVariable String param,@PathVariable Integer type,String callback){
        //查询数据库获取响应信息
        boolean flag = userService.checkUser(param,type);
        SysResult sysResult = SysResult.success(flag);
        return new JSONPObject(callback, sysResult);
        //callback(json结构)
    }
    /*
    *完成httpClient业务调用
    *URL地址：http://sso.jt.com/user/findUserList
    *返回值：userJson
   */
    @RequestMapping("/findUserList")
    public List<User> findUserList(){
        List<User> userList = userService.findAll();
        return userList;
    }
    /*
    * 跨域请求:完成用户信息获取
    * URL网址：http://sso.jt.com/user/query/676e4d2d813f4b8bb227e163f99f7f2e?callback=jsonp1608021986010&_=1608021986052
    * 参数:参数1：ticket信息 参数2：callback
    * 返回值：SysResult对象(用户数据......)
    * */
//    @RequestMapping("/query/{ticket}")
//    public JSONPObject findUserByTicket(@PathVariable String ticket,String callback){
//        //如何获取用户信息？从redis中获取数据
//        if(jedisCluster.exists(ticket)){
//            String userJson = jedisCluster.get(ticket);
//            SysResult sysResult = SysResult.success(userJson);
//            return new JSONPObject(callback , sysResult);
//        }else{
//            return new JSONPObject(callback, SysResult.fail());
//        }
//    }
    @RequestMapping("/query/{ticket}")
    public JSONPObject findUserByTicket(@PathVariable String ticket,String callback){
        if(jedisCluster.exists(ticket)){
            String userJson = jedisCluster.get(ticket);
            SysResult sysResult = SysResult.success(userJson);
            return  new JSONPObject(callback, sysResult);
        }else {
            return  new JSONPObject(callback, SysResult.fail());
        }
    }

}
