package com.jt.vo;

import com.jt.pojo.Item;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class EasyUITable {

    private Long total;         //记录总数
    private List<Item> rows;    // 每页展现的记录

    //1.通过对象的get方法获取JSON的属性及属性值
   /* public String  getTitle(){

        return "我是测试get方法的";
    }*/

    //2.JSON转化为对象时 ,调用对象的set方法,为属性赋值.
}
