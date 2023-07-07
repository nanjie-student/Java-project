package com.jt.interceptor;

import com.jt.pojo.User;
import com.jt.util.CookieUtil;
import com.jt.util.ObjectMapperUtil;
import com.jt.util.UserThreadLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import redis.clients.jedis.JedisCluster;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component  //将拦截器交给spring容器管理
public class UserInterceptor implements HandlerInterceptor {
    private  static final String JT_TICKET = "JT_TICKET";
    @Autowired
    private JedisCluster jedisCluster;
    /*
    * 返回值说明：
    * 1.false 表示拦截 一般都
    * 2.true
    * 如何实现业务：
    * 判断用户是否登陆：cookie数据 检查redis中的数据
    * 重定向到系统登陆页面
    * @Param request
    * @param response
    * @param handle
    * @return
    * @throws Exception*/
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,Object handler) throws IOException {
        //1.校验Cookie中是否有结果
        Cookie cookie = CookieUtil.getCookie(request,JT_TICKET);
        //2.校验Cookie是否有效
        if(cookie != null) {
            String ticket = cookie.getValue();
            if (StringUtils.hasLength(ticket)) {
                //执行后续任务 校验redis中是否有结果
                if (jedisCluster.exists(ticket)) {
                    String json = jedisCluster.get(ticket);
                    User user = ObjectMapperUtil.toObject(json, User.class);
                    //利用Request对象将数据进行传递
                    request.setAttribute("JT_USER", user);
                    UserThreadLocal.setUser(user);
                    //表示用户登陆过，直接返回true
                    return true;
                } else {
                    //没有结果，则cookie数据有误，应该删除
                    CookieUtil.addCookie(response, JT_TICKET,
                            "", "/", "jt.com", 0);
                }
            }
        }
        //3.如果数据为空，则重定向到系统首页
        response.sendRedirect("/user/login.html");
        return false;//表示拦截
    }
    //为了防止内存的泄露，将多余的数据删除
    public void afterCompletion(HttpServletRequest request,HttpServletResponse response){
        request.removeAttribute("JT_USER");
        UserThreadLocal.remove();
    }
}
