package com.jt.service;

import com.baomidou.mybatisplus.core.conditions.query.Query;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.annotation.CacheFind;
import com.jt.mapper.ItemCatMapper;
import com.jt.pojo.ItemCat;
import com.jt.util.ObjectMapperUtil;
import com.jt.vo.EasyUITree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemCatServiceImpl implements ItemCatService{

    @Autowired
    private ItemCatMapper itemCatMapper;
    @Autowired(required = false) //类似于懒加载
    private Jedis jedis;
    @Override
    @CacheFind(key="ITEM_CAT")
    public ItemCat findItemCatById(Long itemCatId) {

        return itemCatMapper.selectById(itemCatId);
    }

    /**
     * 1.根据parentId 查询子级目录信息
     * 2.获取ItemCatList集合,之后将List集合转化为VOList集合
     * 3.返回数据
     * @param parentId
     * @return
     */
    @Override
    @CacheFind(key="ITEM_CAT_PARENT::",seconds = 7*24*60*60)
    public List<EasyUITree> findItemCatList(Long parentId) {
        //缓存里有就不走，缓存里没有就必须走
        QueryWrapper<ItemCat> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", parentId);
        List<ItemCat> catList = itemCatMapper.selectList(queryWrapper);
        //2 list集合转化
        List<EasyUITree> treeList = new ArrayList<>(catList.size());
        for (ItemCat itemCat : catList){
            long id = itemCat.getId();
            String text = itemCat.getName();
            String state = itemCat.getIsParent()?"closed":"open"; //是父级,默认应该closed
            EasyUITree easyUITree = new EasyUITree(id, text, state);
            treeList.add(easyUITree);
        }
        return treeList;
    }
    /*
    * 实现步骤
    * 1.先定义key   ITEM_CAT_PARENT: :0
    * 2.首先查询缓存 两种结果：有 true 获取缓存数据后，将json转化为对象之后返回
    * 没有 false 之后应该查询数据库，之后将数据保存在redis中，默认30天超时*/
    @Override
    public List<EasyUITree> findItemCatCache(Long parentId) {
        long startTime = System.currentTimeMillis();
        List<EasyUITree> treeList = new ArrayList<>();
        //1。定义key
        String key = "ITEM_CAT_PARENT::"+parentId;
        //2.在缓存中获取对象
        if(jedis.exists(key)){
            //存在 直接获取缓存数据，之后转化为对象
            String json = jedis.get(key);
            treeList = ObjectMapperUtil.toObject(json,treeList.getClass());
            System.out.println("查询redis缓存，获取数据");
            long endTime = System.currentTimeMillis();
            System.out.println("耗时是"+(endTime-startTime)+"毫秒");
        }else{
            //不存在，应该先查询数据库，之后把数据保存在redis中
            treeList =findItemCatList(parentId);
            String json = ObjectMapperUtil.toJson(treeList);
            jedis.setex(key,7*24*60*60,json);
            System.out.println("查询数据库获取结果");
            long endTime = System.currentTimeMillis();
            System.out.println("耗时是"+(endTime-startTime)+"毫秒");
        }
        return treeList;
    }
}
