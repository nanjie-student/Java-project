package com.jt.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.User;
import com.jt.service.DubboUserService;
import com.jt.service.HttpClientService;
import com.jt.util.CookieUtil;
import com.jt.vo.SysResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.JedisCluster;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/user/")
public class UserController {
    /*实现业务
    * url:*/
    private  static final String JT_TICKET ="JT_TICKET";//使用常量定义cookie的名称
    @Autowired
    private HttpClientService httpClientService;
    //为接口创建代理对象进行注入，之后通过prc
    @Reference(check = false)
    private DubboUserService userService;
    @Autowired
    private JedisCluster jedisCluster;
    @RequestMapping("{moduleName}")
    public String moduleName(@PathVariable String moduleName){
        return moduleName;
    }
    /*
     *完成httpClient业务调用
     *URL地址：http://www.jt.com/user/findUserList
     *返回值：userJson
     */
    @RequestMapping("findUserList")
    @ResponseBody
    public List<User> findUserList(){
        return httpClientService.findUserlist();
    }
    /*
    * 完成用户的注册操作
    * 1.url地址：http://www.jt.com/user/doRegister
    * 2.参数：password : admin123456
    * username :admin1233333333
    * phone :132123123123
    * 3.返回值：SysResult对象
    * */
    @RequestMapping("/doRegister")
    @ResponseBody
    public SysResult saveUser(User user){
        userService.saveUser(user);
        return SysResult.success(user);
    }
    /*
    * 业务需求，实现业务单点登陆
    * 1.URL地址:http://www.jt.com/user/doLogin
    * 2.参数：username:admin123456 password:123333333333
    * 3.返回值：SysResult对象
    *
    * 关于cookie的说明，
    * 1.cookie只能看到自己域名下的cookie信息，不能看到别的，cookie是私有的
    * 2.setPath说明
    *      setPath("/") 读取cookie权限的设定，根据目录中的请求读取cookie
    *      setPath("/user")url地址路径/user下时才能获取cookie信息
    *      url1:http://www.jt.com/findAll
    *      url2:http://www.jt.com/findAll
    * 实现Cookie数据存储
    * 1.获取用户名和密码进行数据校验
    * 2.获取后端的密钥信息(ticket) 判断是否为空
    * */
//    @RequestMapping("/doLogin")
//    @ResponseBody
//    public SysResult userLogin(User user, HttpServletResponse response){
//        String ticket = userService.finduserByUp(user);
//        if(!StringUtils.hasLength(ticket)){
//            //如果数据为null,则表示用户名和密码发生了错误
//            return SysResult.fail();
//        }
//        //需要将数据保存到cookie中
//        Cookie cookie = new Cookie("JT_TICKET",ticket);
//        cookie.setPath("/");
//        //cookie.setPath("/user");
//        cookie.setMaxAge(7*24*60*60);//设定有效期
//        cookie.setDomain("jt.com");//只有域名中带jt.com的都可以共享数据
//        response.addCookie(cookie);//通过httpServletResponse，响应到了cookie
//        return SysResult.success(user);
//    }
    @RequestMapping("doLogin")
    @ResponseBody
    public SysResult userLogin(User user,HttpServletResponse response){
        String ticket = userService.finduserByUp(user);
        if(!StringUtils.hasLength(ticket)){
            return SysResult.fail();
        }
        Cookie cookie = new Cookie("JT_TICKET", ticket);
        cookie.setPath("/");
        cookie.setDomain("jt.com");
        cookie.setMaxAge(7*24*60*60);
        response.addCookie(cookie);
        return SysResult.success();
    }
    /*
     * 完成用户退出操作
     * 1.重定向到系统首页
     * 2.要求删除redis中的数据 k-v结构
     * 3.动态的获取cookie中的数据
     * 4.删除cookie中的对象
     * url地址：http://www.jt.com/user/logout.html
     * */
    @RequestMapping("/logout")
    public String logout(HttpServletRequest request,HttpServletResponse response){
        String ticket = CookieUtil.getCookieValue(request,JT_TICKET);
        if(StringUtils.hasLength(ticket)) {
            //1.删除redis
            jedisCluster.del(ticket);
            //2.删除ticket
            CookieUtil.addCookie(response, JT_TICKET, "", "/", "jt.com", 0);
        }
        return "redirect:/";
    }
//        Cookie[] cookies = request.getCookies();
//        if(cookies!= null && cookies.length > 0){
//            for(Cookie cookie :cookies) {
//                if(JT_TICKET.equals(cookie.getName())){
//                    String ticket = cookie.getValue();
//                    //删除redis中的数据
//                    jedisCluster.del(ticket);
//                    //删除cookie中的数据
//                    cookie.setMaxAge(0);//0表示立即删除 -1表示关闭浏览器之后删除
//                    cookie.setPath("/");
//                    cookie.setDomain("jt.com");
//                    response.addCookie(cookie);
//                }
//            }
//        }
//        return "redirect:/";
//    }

}
