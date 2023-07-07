package com.jt.dubbo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.dubbo.pojo.User;
import com.jt.dubbo.service.UserService;

@RestController
public class UserController {
	
	////利用dubbo的方式为接口创建代理对象 利用rpc调用
	
	@Reference(loadbalance="leastactive")	//最不活跃，谁最空闲就让谁干
	private UserService userService; 
	
	/**
	 * Dubbo框架调用特点:远程RPC调用就像调用自己本地服务一样简单
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<User> findAll(){
		
		//远程调用时传递的对象数据必须序列化.
		return userService.findAll();
	}
	
	@RequestMapping("/saveUser/{name}/{age}/{sex}")
	public String saveUser(User user) {
		
		userService.saveUser(user);
		return "用户入库成功!!!";
	}
}
