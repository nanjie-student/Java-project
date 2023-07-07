package com.jt.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jt.mapper.OrderItemMapper;
import com.jt.mapper.OrderMapper;
import com.jt.mapper.OrderShippingMapper;
import com.jt.pojo.Order;
import com.jt.pojo.OrderItem;
import com.jt.pojo.OrderShipping;

@Service
public class OrderServiceImpl implements DubboOrderService {
	
	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private OrderShippingMapper orderShippingMapper;
	@Autowired
	private OrderItemMapper orderItemMapper;


	@Override
	@Transactional
	public String saveOrder(Order order) {
		//字符串拼接
		String orderId = "" + order.getUserId() + System.currentTimeMillis() + "";
		//1.完成订单的入库操作
		order.setOrderId(orderId).setStatus(1);//未付款状态
		orderMapper.insert(order);
		//2.入库订单物流信息
		OrderShipping orderShipping = order.getOrderShipping();
		orderShipping.setOrderId(orderId);
		orderShippingMapper.insert(orderShipping);
		//3.完成订单商品入库操作
		List<OrderItem> orderItems = order.getOrderItems();
		for(OrderItem orderItem : orderItems){
			orderItem.setItemId(orderId);
			orderItemMapper.insert(orderItem);
		}
		System.out.println("订单入库完成！！！！");
		return orderId;
	}

	@Override
	public Order findOrderById(String orderId) {
		//1.查询订单数据
		Order order = orderMapper.selectById(orderId);
		//2.查询订单物流信息
		OrderShipping orderShipping = orderShippingMapper.selectById(orderId);
		//3.查询订单商品信息
		QueryWrapper<OrderItem> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("order_id", orderId);
		List<OrderItem> orderItems = orderItemMapper.selectList(queryWrapper);
		//查询数据之后封装返回
		order.setOrderItems(orderItems).setOrderShipping(orderShipping);
		return order;
	}
}
