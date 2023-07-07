package com.jt.web.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.jt.mapper.ItemDescMapper;
import com.jt.mapper.ItemMapper;
import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.service.DubboItemService;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

@Service(timeout = 3000)
public class DubboITemServiceImpl implements DubboItemService {
    @Autowired
    private ItemMapper itemMapper;
    //item/itemDesc
    @Autowired
    private ItemDescMapper itemDescMapper;

    @Override
    public ItemDesc findItemDescById(Long itemId) {
        return itemDescMapper.selectById(itemId);
    }

    @Override
    public Item findItemById(Long itemId) {
        return itemMapper.selectById(itemId);
    }
}
