package com.jt.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.Cart;
import com.jt.pojo.User;
import com.jt.service.DubboCartService;
import com.jt.util.UserThreadLocal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {
    @Reference(check = false)
    private DubboCartService cartService;
    /*
    * 业务需求
    * 展现购物车列表数据
    * URL地址：http://www.jt.com/cart/show.html
    * 参数：必须获取userId=7L
    * 返回值：cart.jsp
    * 页面的取值参数：${cartList}
    * */
    @RequestMapping("/show")
    public String findCartList(Model model,HttpServletRequest request) {
        User user = (User)request.getAttribute("JT_USER");
        Long userId = user.getId();
        //Long userId = 7L;//暂时写死，之后维护
        List<Cart> cartList = cartService.findCartList(userId);
        model.addAttribute("cartList", cartList);
        return "cart";
    }
   /*业务需求:实现商品数量的更新操作
   *url：http://www.jt.com/cart/update/num/1474391990/10
   *参数：userId=7L/itemId/num
   *返回值：void
    * 如果restful参数名称与对象的属性名称一致，则可以使用对象的方式接收*/
    @RequestMapping("/update/num/{itemId}/{num}")
    @ResponseBody//两重含义：1.json返回2.表示ajax请求结束
    public void updateCartNum(Cart cart){

        Long userId = UserThreadLocal.getUser().getId();
        cart.setUserId(userId);
        cartService.updateCartNum(cart);
    }
    /*
    * 完成购物车入库操作
    * url:http://www.jt.com/cart/add/1474391990/10
    * 参数：cart form 表单提交
    * 返回值：重定向到购物车列表页面
    * */
    @RequestMapping("/add/{itemId}")
    public String saveCart(Cart cart){
        long userId = UserThreadLocal.getUser().getId();
        cart.setUserId(userId);
        cartService.saveCart(cart);
        return "redirect:/cart/show.html";
    }
    /*完成删除操作
    * 1.url:http://www.jt.com/cart/delete/562379.html
    * 2.请求参数：562379 item/userId
    * 3.返回值结果：重定向到系统首页*/
    @RequestMapping("/delete/{itemId}")
    public String deleteCart(Cart cart){
        Long userId = UserThreadLocal.getUser().getId();
        cart.setUserId(userId);
        cartService.deleteCart(cart);
        return "redirect:/cart/show.html";
    }
}
