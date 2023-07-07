package com.jt.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jt.mapper.ItemDescMapper;
import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.vo.EasyUITable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jt.mapper.ItemMapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {
	
	@Autowired
	private ItemMapper itemMapper;
	@Autowired
	private ItemDescMapper itemDescMapper;

	/**
	 * 分页Sql:
	 * 		查询第一页:
		 * 		select * from tb_item limit 起始位置,查询条数
		 * 	   	select * from tb_item limit 0,20    共21个数  index[0,19]
	 * 	   	查询第二页:
	 * 	   		select * from tb_item limit 20,20    index[20,39]
	 * 	   	查询第三页:
	 * 	 	   	select * from tb_item limit 40,20
	 * 	 	查询第N页:
	 * 	 * 	 	select * from tb_item limit (page-1)rows,20
	 * @param page
	 * @param rows
	 * @return
	 */
	@Override
	public EasyUITable findItemByPage(int page, int rows) {
		/*long total = itemMapper.selectCount(null);
		//1.手写分页
		int startIndex = (page - 1) * rows;
		List<Item> itemList = itemMapper.findItemByPage(startIndex,rows);*/

		//2.利用MP方式实现分页
		IPage mpPage = new Page(page,rows);
		QueryWrapper<Item> queryWrapper = new QueryWrapper<>();
		queryWrapper.orderByDesc("updated");
		mpPage = itemMapper.selectPage(mpPage,queryWrapper);
		long total = mpPage.getTotal();	//获取记录总数
		List<Item> itemList = mpPage.getRecords();	//获取查询当前页
		return new EasyUITable(total, itemList);
	}
/*
* */
	@Override
	@Transactional	//开启事务控制
	public void saveItem(Item item, ItemDesc itemDesc) {
		//Date date = new Date();
		//item.setStatus(1).setCreated(date).setUpdated(date);
		item.setStatus(1);
		//如何实现主键的回显
		//mp用法:如果完成主键的自增，则主动的实现数据的回显
		itemMapper.insert(item);
		//获取主键信息
		itemDesc.setItemId(item.getId());
		itemDescMapper.insert(itemDesc);
		//int a = 1/0;
	}


	@Override
	@Transactional	//控制事务
	public void updateItem(Item item, ItemDesc itemDesc) {

		itemMapper.updateById(item);
		//补全数据
		itemDesc.setItemId(item.getId());
		itemDescMapper.updateById(itemDesc);
	}

	//sql: delete from tb_item where id in (100,101...)
	@Override
	@Transactional
	public void deleteItems(Long[] ids) {
		//方式1:利用MP方式实现
		/*List<Long> longIds = Arrays.asList(ids);
		itemMapper.deleteBatchIds(longIds);*/

		//方式2:手写Sql完成数据删除.
		itemMapper.deleteItems(ids);
		List<Long> longIds = Arrays.asList(ids);
		itemMapper.deleteBatchIds(longIds);
	}

	@Override
	@Transactional
	public void updateStatus(Long[] ids, Integer status) {
		//1.mp方式操作数据库,只修改状态码
		Item item = new Item();
		item.setStatus(status);
		QueryWrapper<Item> queryWrapper = new QueryWrapper<>();
		queryWrapper.in("id",ids);
		itemMapper.update(item,queryWrapper);
	}

	@Override
	public ItemDesc findItemById(Long itemId) {
		return itemDescMapper.selectById(itemId);
	}
	//	@Override
//	public void updateStatus(Long[] ids, Integer status) {
//		//方法二sql
//		itemMapper.updateStatus(ids,status);
//	}




}
