package com.jt.aop;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jt.vo.SysResult;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;

@RestControllerAdvice   //AOP+异常通知
@Slf4j
public class SystemException {
    //确定该请求是一个跨域的jsonp请求时，才按照指定的格式返回！！！callback(json)
    //利用参数callback判断是否为jsonp跨域，如何获取？
    //当遇到某种类型的异常时才会执行
    @ExceptionHandler({RuntimeException.class})
    public Object exception(Exception e, HttpServletRequest request, HttpServletResponse response){
        //log.info(e.getMessage(), e);    //异常输出
        log.error(e.getMessage());
        e.printStackTrace();//输出异常信息
        //如果出错,返回系统级别的报错数据即可
        //1.获取callback请求参数
        String callback = request.getParameter("callback");
        //2。判断是否为json跨域请求
        if(StringUtils.hasLength(callback)){//如果有值则返回callback(json)
            JSONPObject jsonpObject = new JSONPObject(callback, SysResult.fail());
            return jsonpObject;
        }

        return SysResult.fail();
    }
}
