package com.jt.util;

import com.jt.pojo.User;

public class UserThreadLocal {//线程传参
    private static ThreadLocal<User> threadLocal = new ThreadLocal<>();
    public static void setUser(User user){
        threadLocal.set(user);//将user赋值给线程
    }
    public static User getUser(){
        return  threadLocal.get();//从线程中取到user，返回值是user
    }
    public static void remove(){
        threadLocal.remove();
    }
}
