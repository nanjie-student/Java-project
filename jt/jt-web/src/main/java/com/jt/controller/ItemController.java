package com.jt.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.service.DubboItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ItemController {


    @Reference(check = false)//check校验时，是否校验有服务的提供者
    private DubboItemService dubboItemService;
    /*
    * 根据商品ID查询商品信息
    *1.URL地址：http://www.jt.com/item/562379.html
    *2.参数：562379 restFul风格
    *3返回值：String
     4.页面取值操作 ${item.title}
     * ${itemDesc.itemDesc}*/
    @RequestMapping("/items/{itemId}")
    public String findItemById(@PathVariable Long itemId, Model model){
        //1.获取商品相关信息
       Item item =dubboItemService.findItemById(itemId);
        ItemDesc itemDesc = dubboItemService.findItemDescById(itemId);
        //2.将数据传递到页面
        model.addAttribute("item",item);
        model.addAttribute("itemDesc",itemDesc);
        return "item";
    }
}
