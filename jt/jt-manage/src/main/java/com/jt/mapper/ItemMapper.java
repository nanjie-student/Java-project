package com.jt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jt.pojo.Item;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ItemMapper extends BaseMapper<Item>{

    @Select("select * from tb_item order by updated desc limit #{startIndex},#{rows}")
    //将多值封装成单值  Map集合   key=xxx value 0,   key=yyy  value=20
    List<Item> findItemByPage(int startIndex,int rows);

    void deleteItems(Long[] ids);
    void updateStatus(@Param("ids") Long[] ids,@Param("status") Integer status);
}
