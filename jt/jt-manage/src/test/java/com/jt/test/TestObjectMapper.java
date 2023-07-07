package com.jt.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.pojo.ItemDesc;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TestObjectMapper {
    //object的入门案例
    //1，将对象转化为json
    //2.将json转化为对象
    @Test
    public void testobject() throws JsonProcessingException {
        //1.将对象转化为json
        ItemDesc itemDesc = new ItemDesc();
        itemDesc.setItemId(100L)
                .setItemDesc("我是一个测试数据")
                .setCreated(new Date())
                .setUpdated(itemDesc.getCreated());

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(itemDesc);
        System.out.println(json);
        //2.将json转化为对象
        ItemDesc desc = objectMapper.readValue(json, ItemDesc.class);
        System.out.println(desc);
    }
    @Test
    public void testlist() throws JsonProcessingException {
        List<ItemDesc> list = new ArrayList<>();
        ItemDesc itemDesc = new ItemDesc();
        itemDesc.setItemId(100L)
                .setItemDesc("我是一个测试数据")
                .setCreated(new Date())
                .setUpdated(itemDesc.getCreated());
        ItemDesc itemDesc2 = new ItemDesc();
        itemDesc.setItemId(200L)
                .setItemDesc("我是一个测试数据")
                .setCreated(new Date())
                .setUpdated(itemDesc.getCreated());
        list.add(itemDesc);
        list.add(itemDesc2);
        //1.将对象转化为json
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(list);
        System.out.println(json);
        //2.将json转化为对象 注意linkedhashmap
        List<ItemDesc> list2 = objectMapper.readValue(json, list.getClass());
        System.out.println(list2);
    }
}
