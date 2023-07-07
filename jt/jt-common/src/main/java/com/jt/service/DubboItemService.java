package com.jt.service;

import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;

public interface DubboItemService {
    ItemDesc findItemDescById(Long itemId);

    Item findItemById(Long itemId);
}
