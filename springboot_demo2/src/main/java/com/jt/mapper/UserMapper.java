package com.jt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jt.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper //将mapper接口交给Spring容器管理.并且为其创建代理对象 CGLIB代理方式
public interface UserMapper extends BaseMapper<User> {

    //查询数据库中所有的用户数据
    //@Select("select * from user")
    List<User> findAll();





}
