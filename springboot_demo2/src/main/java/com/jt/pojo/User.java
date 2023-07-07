package com.jt.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data   //get/set/toString(只能打印自己的属性,不能输出父类的属性,如想获取通过getXX方法获取)/equals等方法
@NoArgsConstructor  //无参构造
@AllArgsConstructor //全参构造
@Accessors(chain = true)  //链式加载规则  重写了set方法

//1.实现对象与表的映射
@TableName("user")          //如果表名与对象名称一致可以省略
public class User{
    //POJO实体对象中属性类型必须使用包装类型
    @TableId(type = IdType.AUTO)  //设定主键自增
    private Integer id;
    @TableField("name")  //如果数据库字段与属性名称一致则可以省略不写
    private String name;
    @TableField("age")
    private Integer age;
    @TableField("sex")
    private String sex;

   /* public User setId(Integer id){

        this.id = id;
        return this;
    }

    public User setName(String name){

        this.name = name;
        return this;
    }*/

}
