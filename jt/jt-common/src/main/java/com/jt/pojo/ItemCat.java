package com.jt.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

@TableName("tb_item_cat")
@Data
@Accessors(chain = true)
public class ItemCat extends BasePojo{

    @TableId(type = IdType.AUTO)
    private Long id;        //主键
    private Long parentId;  //父级ID  一般适用于有父子级关系的数据
    private String name;    //名称
    private Integer status; //状态信息
    private Integer sortOrder;  //排序号
    private Boolean isParent;   //是否为父级


    /*
    * create table tb_item_cat
(
   id                   bigint not null auto_increment,
   parent_id            bigint comment '父ID=0时，代表一级类目',
   name                 varchar(150),
   status               int(1) default 1 comment '默认值为1，可选值：1正常，2删除',
   sort_order           int(4) not null,
   is_parent            tinyint(1),
   created              datetime,
   updated              datetime,
   primary key (id)
);

    *
    *
    *
    *
    * */
}
