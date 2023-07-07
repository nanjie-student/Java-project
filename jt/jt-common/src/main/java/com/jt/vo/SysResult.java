package com.jt.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class SysResult {

    private Integer status;  //200表示成功   201表示失败
    private String  msg;     //服务器给用户的提示信息
    private Object  data;    //服务器返回给用户的数据

    //封装工具API
    public static SysResult fail(){

        return new SysResult(201, "服务器调用异常", null);
    }

    public static SysResult success(){

        return new SysResult(200, "业务执行成功!", null);
    }

    public static SysResult success(Object data){

        return new SysResult(200, "业务执行成功!", data);
    }

    public static SysResult success(String msg,Object data){

        return new SysResult(200, msg, data);
    }
}
