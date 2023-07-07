package com.jt;

import com.baomidou.mybatisplus.core.conditions.query.Query;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.Update;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jt.mapper.UserMapper;
import com.jt.pojo.User;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@SpringBootTest //写测试类时如果需要从容器中动态的获取对象时使用
class SpringbootDemo2ApplicationTests {

	@Autowired
	private UserMapper userMapper;

	@Test
	public void testMybatis01(){
		List<User> userList = userMapper.findAll();
		System.out.println(userList);
	}

	@Test
	public void testSelectList(){
		//查询所有的数据
		List<User> userList = userMapper.selectList(null);
		System.out.println(userList);
	}

	/**
	 * 1.查询用户数据
	 * 1.1 根据id=51号数据
	 * Sql: select * from user where id=51
	 */
	@Test
	public void select01(){
		User user = userMapper.selectById(51) ; //根据主键查询
		System.out.println(user);
	}

	/**
	 * 2. name="王昭君"的用户
	 * Sql: select * from user where name="xxx"
	 * 条件构造器:主要作用动态拼接where条件的
	 * 特殊符号:
	 * 	1. = eq, 2. > gt  , 3. < lt
	 * 				>= ge      <=le
	 */
	@Test
	public void select02(){
		QueryWrapper<User> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("name", "王昭君");
		List<User> userList = userMapper.selectList(queryWrapper);
		System.out.println(userList);
	}

	/**
	 * 查询性别为男性,要求按照年龄降序排列
	 * Sql: select * from user where sex="男" order by age desc
	 */
	@Test
	public void select03(){
		QueryWrapper<User> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("sex", "男")
					.orderByDesc("age");
		List<User> userList = userMapper.selectList(queryWrapper);
		System.out.println(userList);
	}

	/**
	 * 查询name中包含 "精"用户,按照年龄升序排序
	 * Sql: select * from user where name like "%精%"  order by age asc
	 * 										   "%精"
	 * 										   "精%"
	 */
	@Test
	public void select04(){
		QueryWrapper<User> queryWrapper = new QueryWrapper<>();
		queryWrapper.like("name", "精")
				.orderByAsc("age");
		List<User> userList = userMapper.selectList(queryWrapper);
		System.out.println(userList);
	}


	/**
	 * 查询age>=18 and age<100 and sex=男性用户
	 * 默认的逻辑运算符and
	 */
	@Test
	public void select05(){
		QueryWrapper<User> queryWrapper = new QueryWrapper<>();
		queryWrapper.between("age", 18, 100)
					.eq("sex", "男");
		/*queryWrapper.ge("age", 18)
				    .lt("age", 100)
					.eq("sex", "男");*/
		List<User> userList = userMapper.selectList(queryWrapper);
		System.out.println(userList);
	}

	/**
	 * 查询id= 1,3,5,6,7的用户
	 * 查询name="黑熊精/白龙驴/大乔"
	 */
	@Test
	public void select06(){
		Integer[] ids = {1,3,5,6,7};
		List idList = Arrays.asList(ids);	//转化时需要使用包装类型
		List<User> userList = userMapper.selectBatchIds(idList);
		System.out.println(userList);
		QueryWrapper queryWrapper = new QueryWrapper();
		queryWrapper.in("name", "黑熊精","白龙驴","大乔");
		List<User> userList2 = userMapper.selectList(queryWrapper);
		System.out.println(userList2);
	}

	/**
	 * 查询 id,name 全部用户信息
	 */
	@Test
	public void select07(){
		QueryWrapper<User> queryWrapper = new QueryWrapper<>();
		queryWrapper.select("id","name");
		List<User> userList = userMapper.selectList(queryWrapper);
		System.out.println(userList);

		List<Map<String,Object>> userMap = userMapper.selectMaps(queryWrapper);
		System.out.println(userMap);
	}

	@Test
	public void insert(){
		User user = new User();
		user.setId(null).setName("嫦娥5号")
		    .setAge(5).setSex("女");
		userMapper.insert(user);
	}


	/**
	 * 要求将id=67号的数据的名字改为火星五号
	 */
	@Test
	public void update(){
		User user = new User();
		user.setName("火星五号").setId(67);
		//根据对象中不为null的属性当做set条件,主键是更新条件
		userMapper.updateById(user);
	}

	/**
	 * 要求将火星五号的数据改为 太阳五号 age改为1000
	 * 参数说明:
	 * 	1.实体对象   需要修改的数据采用实体对象进行封装
	 * 	2.修改条件   修改条件采用Wrapper形式进行构建
	 */
	@Test
	public void update02(){
		User user = new User();
		user.setName("太阳五号").setAge(1000);
		UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
		updateWrapper.eq("name", "火星五号");
		userMapper.update(user,updateWrapper);
	}









}
