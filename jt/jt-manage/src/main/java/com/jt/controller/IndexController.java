package com.jt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class IndexController {

	/**
	 * 需求:如果可以动态获取url中的地址当做参数,则可以自动的实现跳转
	 * restFul语法:
	 * 		1.参数与参数之间使用/分割
	 * 		2.参数使用{}包裹
	 * 		3.参数使用指定注解获取
	 * restFul风格2:
	 * 		按照不同业务逻辑,采用不同的请求方式
	 * 		1.查询业务逻辑 GET
	 * 	    2.提交操作	  POST
	 * 	    3.更新操作    PUT
	 * 	    4.删除操作    DELETE
	 */

	//@RequestMapping(value = "/page/{moduleName}",method = RequestMethod.GET)
	@GetMapping("/page/{moduleName}")
	public String itemAdd(@PathVariable String moduleName){

		return moduleName;
	}

}
