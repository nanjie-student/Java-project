package com.jt.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.UserMapper;
import com.jt.pojo.User;
import com.jt.util.ObjectMapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import redis.clients.jedis.JedisCluster;

import java.util.UUID;

@Service(timeout = 3000)
public class DubboUserServiceImpl implements DubboUserService{
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private JedisCluster jedisCluster;
    @Override
    public void saveUser(User user) {
        //加密
        String passward = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(passward);
        userMapper.insert(user);
    }
    /*业务逻辑
    * 1.校验用户名和密码是否正确，不存在直接返回null
    * 2.动态生成密钥 将用户信息转化为json
    * 3.将数据保存到redis中7天有效
    * 4.返回密钥ticket信息
    * */
    @Override
    public String finduserByUp(User user) {
        String md5Pass = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(md5Pass);
        //1.根据对象中不为null的属性 当中where条件
        QueryWrapper<User> queryWrapper = new QueryWrapper<>(user);
        User userDB = userMapper.selectOne(queryWrapper);
        //2.判断对象是否有值
        if(userDB==null){
            return null;
        }
        //3.表示用户名和密码正确，开启单点登陆操作
        String ticket = UUID.randomUUID()
                .toString().replace("-", "");
        //在转化之前将数据脱敏
        userDB.setPassword("123456");//改的是用户数据，不是数据库的
        String userJson = ObjectMapperUtil.toJson(userDB);
        //4将数据保存到redis
        jedisCluster.setex(ticket, 7*24*60*60, userJson);
        return ticket;
    }
    /**/

}

