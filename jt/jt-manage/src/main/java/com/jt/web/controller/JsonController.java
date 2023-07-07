package com.jt.web.controller;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jt.pojo.ItemDesc;
import com.jt.util.ObjectMapperUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JsonController {
    /*
    * 完成JSONP的跨域访问
    * URL地址："http://manage.jt.com/web/testJSONP?callback=jquery"
    *参数：callback=JQuxxxxx
    * 返回值：callback(Json)
    * */
//    @RequestMapping("/web/testJSONP")
//    public String jsonP(String callback){
//        ItemDesc itemDesc = new ItemDesc();
//        itemDesc.setItemId(101L).setItemDesc("跨域访问");
//        String json = ObjectMapperUtil.toJson(itemDesc);
//        return callback +"("+json+")";
//    }
    /*
    * 关于JSONPObject对象说明*/
    @RequestMapping("/web/testJSONP")
    public JSONPObject jsonp(String callback){
        ItemDesc itemDesc = new ItemDesc();
        itemDesc.setItemId(101L).setItemDesc("跨域访问222");
        return new JSONPObject(callback, itemDesc);
    }
}
