package com.jt.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jt.mapper.CartMapper;
import com.jt.pojo.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DubboCartServiceImpl implements DubboCartService{
    @Autowired
    private CartMapper cartMapper;

    @Override
    public List<Cart> findCartList(Long userId) {
        QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
        return cartMapper.selectList(queryWrapper);
    }
    /*
    * 实现数量的更新操作
    * sql:update tb_cart set num = #{num},updated = #{updated}
    * where user_id = #{userId} and item_id = #{itemId}
    * @param cart*/
    @Override
    public void updateCartNum(Cart cart) {//itemId,userId,num
        Cart tempCart = new Cart();
        tempCart.setNum(cart.getNum());
        UpdateWrapper<Cart> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("item_id", cart.getItemId());
        updateWrapper.eq("user_id", cart.getUserId());
        cartMapper.update(tempCart,updateWrapper);
    }
    //如果用户重复加购，则只做数量的修改
    //根据userId和itemId,判断数据是否有值
    @Transactional
    @Override
    public void saveCart(Cart cart) {
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", cart.getUserId());
        queryWrapper.eq("item_id", cart.getItemId());
        Cart cartDB = cartMapper.selectOne(queryWrapper);//cartDB是个普通对象，可以为null,绝不能是空串""
        //if(stringUtil ==null)stringutil可以为空，也可以为空字符串
        if(cartDB == null){//数据库没有直接入库
            cartMapper.insert(cart);
        }else{//数据库中有记录，只改数量
            int num = cart.getNum()+cartDB.getNum();
            //1.手写sql 2.简洁封装
            Cart cartTemp = new Cart();
            cartTemp.setId(cartDB.getId()).setNum(num);
            cartMapper.updateById(cartTemp);
        }
    }

    @Override
    public void deleteCart(Cart cart) {
//        QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("user_id", cart.getUserId());
//        queryWrapper.eq("item_id",cart.getItemId());
//        cartMapper.delete(queryWrapper);
        QueryWrapper<Cart> queryWrapper = new QueryWrapper<>(cart);
        cartMapper.delete(queryWrapper);
    }
}
