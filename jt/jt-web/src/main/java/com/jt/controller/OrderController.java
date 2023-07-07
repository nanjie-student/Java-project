package com.jt.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.Cart;
import com.jt.pojo.Order;
import com.jt.service.DubboCartService;
import com.jt.service.DubboOrderService;
import com.jt.util.UserThreadLocal;
import com.jt.vo.SysResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("order")
public class OrderController {
    @Reference(check=false)
    private DubboOrderService orderService;
    @Reference(check=false)
    private DubboCartService cartService;
    /*
    * 1.实现订单页面的跳转
    * url地址：http://www/jt.com/order/create.html
    * 参数说明：获取userId
    * 返回值结果：订单确认页面 order-cart.jsp
    * 页面取值方式：${carts}*/
    @RequestMapping("/create")
    public String orderCart(Model model){
        long userId = UserThreadLocal.getUser().getId();
        List<Cart> cartList = cartService.findCartList(userId);
        model.addAttribute("carts",cartList);
        return "order-cart";
    }
    /*
    * 业务需求：实现订单的入库操作
    * url:http://www.jt.com/order/submit
    * 参数：整个表单序列化 order对象的表单
    * 返回值：SysResult对象(orderId)
    * */
    @RequestMapping("/submit")
    @ResponseBody
    public SysResult saveOrder(Order order){
        Long userId = UserThreadLocal.getUser().getId();
        order.setUserId(userId);

        String orderId = orderService.saveOrder(order);
        if(StringUtils.hasLength(orderId)){
            return SysResult.success(orderId);
        }
        return SysResult.fail();
    }
    /*
    * 业务需求：实现订单页面查询
    * URL：http://www.jt.com/order/success.html?id=71608193830612
    * 参数：id = xxxx
    * 返回值结果:要求跳转到成功页面
    * 页面取值： order对象及封装的2个属性*/
    @RequestMapping("/success")
    public String success(@RequestParam("id") String orderId,Model model){
        Order order = orderService.findOrderById(orderId);
        model.addAttribute("order",order);
        return "success";
    }
}
