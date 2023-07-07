package com.jt.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class EasyUITree {

    //JSON结构要求 {"id":100,"text":"tomcat","state":"open/closed"}
    private Long id;        //节点ID
    private String text;    //定义名称
    private String state;   //节点状态.

}
