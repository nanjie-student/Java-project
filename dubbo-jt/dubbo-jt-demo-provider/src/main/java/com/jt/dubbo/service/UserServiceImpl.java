package com.jt.dubbo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.jt.dubbo.mapper.UserMapper;
import com.jt.dubbo.pojo.User;
@Service(timeout=3000)	//3秒超时 内部实现了rpc
//@org.springframework.stereotype.Service//将对象交给spring容器管理
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserMapper userMapper;
	
	@Override
	public List<User> findAll() {
		
		System.out.println("我是第一个服务的提供者");
		return userMapper.selectList(null);
	}
	
	@Override
	public void saveUser(User user) {
		
		userMapper.insert(user);
	}
}
