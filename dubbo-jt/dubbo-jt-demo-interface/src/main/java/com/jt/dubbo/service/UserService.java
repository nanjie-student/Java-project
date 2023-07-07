package com.jt.dubbo.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.jt.dubbo.pojo.User;

public interface UserService {
	
	//查询全部的用户信息
	List<User> findAll();
	//新增用户入库操作.
	@Transactional
	void saveUser(User user);
}
