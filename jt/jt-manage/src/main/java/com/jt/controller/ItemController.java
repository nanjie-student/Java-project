package com.jt.controller;

import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.vo.EasyUITable;
import com.jt.vo.SysResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jt.service.ItemService;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController	//表示返回数据都是JSON
@RequestMapping("/item")
public class ItemController {
	
	@Autowired
	private ItemService itemService;

	/**
	 * 业务: 分页查询商品信息.
	 * url:  http://localhost:8091/item/query?page=1&rows=20
	 * 参数: page=1 页数  &rows=20  行数
	 * 返回值: EasyUITable
	 */
	@RequestMapping("/query")
	public EasyUITable findItemByPage(int page,int rows){

		return itemService.findItemByPage(page,rows);
	}

	/**
	 * 业务需求:	完成商品新增操作
	 * url:	http://localhost:8091/item/save
	 * 参数: 整个表单进行提交  使用对象接收
	 * 返回值: 系统返回值对象
	 *
	 */
	@RequestMapping("/save")
	public SysResult saveItem(Item item, ItemDesc itemDesc){//对象，详情两个参数
		itemService.saveItem(item,itemDesc);
		return SysResult.success();
		/*try {
			itemService.saveItem(item);
			return SysResult.success();
		}catch (Exception e){
			e.printStackTrace();
			return SysResult.fail();
		}*/
	}

	/**
	 * 实现商品编辑
	 * url地址: /item/update
	 * 请求参数: 整个form表单提交
	 * 返回值:	sysResult对象
	 */
	@RequestMapping("/update")
	public SysResult updateItem(Item item,ItemDesc itemDesc){
		itemService.updateItem(item,itemDesc);
		return SysResult.success(itemDesc);
	}

	/**
	 * 需求: 实现商品删除操作
	 * url: http://localhost:8091/item/delete
	 * 请求参数:  ids: 1474392029,1474392030
	 * 返回值:  SysResult对象
	 *
	 * 知识扩展:
	 * 	问题: 页面中<input name="id"  value="100"/>
	 * 	参数是如何接收的,底层实现是什么? servlet是否熟悉
	 * 	利用servlet中的request对象/response对象进行参数传递.
	 *
	 *  注意事项:方法中的参数名称,必须与页面中的name属性名称一致!!!
	 */
	@RequestMapping("/delete")
	public SysResult deleteItems(Long[] ids){

		itemService.deleteItems(ids);
		return SysResult.success();

	/*	String ids = request.getParameter("ids");
		String[] idsStr = ids.split(",");
		Long[]   idsLong = new Long[idsStr.length];
		for(int i=0;i<idsStr.length;i++){
			idsLong[i] = Long.parseLong(idsStr[i]);
		}
		idsLong*/
	}
	@RequestMapping("/updateStatus/{status}")
	public SysResult updateStatus(@PathVariable Integer status,Long... ids){
		itemService.updateStatus(ids,status);
		return SysResult.success();
	}
	/*
	* 业务说明：根据商品Id号，检索商品详情信息
	* URL：http://localhost:8091/item/query/item/desc/1473....
	* 参数风格：rest风格
	* 返回值：SysResult对象
	* */
	@RequestMapping("query/item/desc/{itemId}")
	public SysResult findItemDescById(@PathVariable Long itemId){
		ItemDesc itemDesc = itemService.findItemById(itemId);
		return SysResult.success(itemDesc);
	}














	/*@RequestMapping("/testVO")
	@ResponseBody
	public EasyUITable testVO(){
		Item item1 = new Item();
		item1.setId(1000L).setTitle("饮水机").setPrice(200L);
		Item item2 = new Item();
		item2.setId(2000L).setTitle("电冰箱").setPrice(1800L);
		List<Item> rows = new ArrayList<>();
		rows.add(item1);
		rows.add(item2);
		return new EasyUITable(2000L, rows);
	}*/
	
	
	
}
