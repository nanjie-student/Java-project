package com.jt.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtil {
    //1.获取cookie对象
    public static Cookie getCookie(HttpServletRequest request,String cookieName){
        Cookie[] cookies = request.getCookies();
        if(cookies !=null && cookies.length > 0){
            for(Cookie cookie : cookies){
                if(cookieName.equals(cookie.getName())){
                    return cookie;
                }
            }
        }
        return null;
    }
    //2.获取Cookie的值
    public static String getCookieValue(HttpServletRequest request,String cookieName){
        Cookie cookie = getCookie(request, cookieName);
        return cookie == null?null :cookie.getValue();
    }
    //3.新增cookie操作/删除操作
    public static void addCookie(HttpServletResponse response,String cookieName,String cookieValue,String path,String domain,Integer second){
        Cookie cookie = new Cookie(cookieName, cookieValue);
        cookie.setMaxAge(second);
        cookie.setDomain(domain);
        cookie.setPath(path);
        response.addCookie(cookie);
    }

}
