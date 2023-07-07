package com.jt.controller;

import com.jt.pojo.ItemCat;
import com.jt.service.ItemCatService;
import com.jt.vo.EasyUITree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/item/cat")
public class ItemCatController {

    @Autowired
    private ItemCatService itemCatService;

    /**
     * 需求: 根据itemCatId查询商品分类名称
     * url:  http://localhost:8091/item/cat/queryItemName?itemCatId=163
     * 参数: itemCatId=163
     * 返回值: 商品分类名称
     */
    @RequestMapping("/queryItemName")
    public String queryItemName(Long itemCatId){

        ItemCat itemCat = itemCatService.findItemCatById(itemCatId);
        return itemCat.getName();
    }

    /**
     * 业务需求: 实现商品分类树形结构展现
     * url地址:   http://localhost:8091/item/cat/list
     * 参数:      id= 父级节点的ID
     * 返回值:    List<EasyUITree>
     */
    @RequestMapping("/list")
    public List<EasyUITree> findItemCatList(Long id){
        //暂时只查询一级商品分类信息
        long parentId = (id==null?0:id);
        return itemCatService.findItemCatList(parentId);//使用了aop不需要改代码
        //return itemCatService.findItemCatCache(parentId);
    }




}
