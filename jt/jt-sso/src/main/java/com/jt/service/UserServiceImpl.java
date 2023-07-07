package com.jt.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.UserMapper;
import com.jt.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService{
    private static Map<Integer,String> paramMap = new HashMap<>();
    static {
        paramMap.put(1,"username");
        paramMap.put(2,"phone");
        paramMap.put(3,"email");

    }
    @Autowired
    private UserMapper userMapper;

    /*
    * 根据用户传递的参数，获取数据库记录
    * @param param
    * @param type
    * @return */
    @Override
    public boolean checkUser(String param, Integer type) {
        String column = paramMap.get(type);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(column,param);
        int count = userMapper.selectCount(queryWrapper);//queryWrapper是条件构造器
        return count>0;
    }

    @Override
    public List<User> findAll() {
        return userMapper.selectList(null);
    }
}
