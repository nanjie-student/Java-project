package com.jt.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.*;
import redis.clients.jedis.params.SetParams;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SpringBootTest
public class TestRedis {
    @Autowired
    private Jedis jedis;
    //链接服务：IP地址：端口
    @Test
    public void testSetGet() throws InterruptedException {
        //Jedis jedis = new Jedis("192.168.126.129",6379);
        jedis.flushAll(); //先清空缓存
        //1.存取redis
        jedis.set("key1","天天向上");
        String value = jedis.get("key1");
        //判断key是否存在
        if(jedis.exists("key1")){
            jedis.set("key1","好好学习，天天向上");
        }else{
            jedis.set("key1","天天开心");
        }
        //3.为key添加超市时间
        jedis.expire("key1",10);
        Thread.sleep(2000);
        System.out.println("剩余存活时间"+jedis.ttl("key1"));
        //4.撤销超时时间
        jedis.persist("key1");
        System.out.println("撤销成功！！！");
    }
    /*
    * 需求:如果数据不存在，则将数据修改
    * */
    @Test
    public void testSetNx(){
        Jedis jedis = new Jedis("192.168.126.129",6379);
        jedis.set("key1","aaa");
        //如果key不存在，则赋值
       jedis.setnx("key1","测试方法");
        System.out.println(jedis.get("key1"));
    }
    /*
    * 需求：实现超时时间的设定与赋值操作的原子性
    * 考点：为数据设定超时时间时，切记满足原子性不能再次分割的要求操作，否则会出现key值永远不能删除*/
    @Test
    public void testSetEx(){
        Jedis jedis = new Jedis("192.168.126.129",6379);
        //jedis.set("key2","bbb");
        //jedis.expire("key2", 10);
        //数据超时后一定会被删除吗？错的
        jedis.setex("key2", 10, "bbb");
    }
    /*
    * 需求：如果数据存在时才会修改数据，并且为数据添加超时时间10秒
    * 参数说明：
    * nx:只有数据不存在时，才会赋值
    * xx:只有数据存在时，才会赋值
    * ex:秒
    * px:毫秒*/
    @Test
    public void testSet(){
        Jedis jedis = new Jedis("192.168.126.129",6379);
//        if(jedis.exists("key3")){
//            jedis.setex("key3",10,"ccc");
//        }
        SetParams setParams = new SetParams();
        setParams.xx().ex(10);
        //将所有的操作采用原子性的方式进行控制
        jedis.set("key3", "ccc",setParams);
    }
    @Test
    public void testHash(){
        Jedis jedis = new Jedis("192.168.126.129",6379);
        jedis.hset("person","id","100");
        jedis.hset("person","name","tomcat猫");
        System.out.println("person");
    }
    /*list,方向相反为队列
    * 方向相同为栈*/
    @Test
    public void testList(){
        Jedis jedis =new Jedis("192.168.126.129",6379);
        jedis.lpush("list","1,2,3,4,5");
        String value = jedis.rpop("list");
        System.out.println(value);
    }
    /*
    * set集合测试
    * sadd 新增元素
    * scard 获取元素的数量
    * sinter key1 key2 获取元素的交集
    * smerbers +集合名称 获取集合重的元素
    * 这个test自己写*/
    /*sortedset
    * */
    @Test
    public void testMulti(){
        Jedis jedis = new Jedis("192.168.126.129",6379);
        Transaction transaction = jedis.multi();
        try{
            transaction.set("a","a");
            transaction.set("b", "b");
            //提交事物
            transaction.exec();
        }catch(Exception e){
            transaction.discard();
        }
    }
    /*
    * 测试案例
    * 要求用户通过客户端程序，动态链接3台redis服务器
    * */
    @Test
    public void testShareds(){
        List<JedisShardInfo> shareds = new ArrayList<>();
        shareds.add(new JedisShardInfo("192.168.126.129",6379));
        shareds.add(new JedisShardInfo("192.168.126.129",6380));
        shareds.add(new JedisShardInfo("192.168.126.129",6381));
        ShardedJedis shardedJedis = new ShardedJedis(shareds);
        shardedJedis.set("shards","redis分片操作");
        System.out.println(shardedJedis.get("shards"));

    }
    /*测试哨兵*/
    @Test
    public void testSentinel(){
        Set<String> set = new HashSet<>();
        set.add("192.168.126.129:26379");
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(1000);
        poolConfig.setMaxIdle(40);
        poolConfig.setMinIdle(10);
        JedisSentinelPool sentinelPool = new JedisSentinelPool("mymaster", set,poolConfig);
        Jedis jedis = sentinelPool.getResource();
        jedis.set("sentinel", "哨兵测试机制");
        System.out.println(jedis.get("sentinel"));
        jedis.close();
    }
    @Test
    public void testCluster(){
        Set<HostAndPort> nodes = new HashSet<>();
        nodes.add(new HostAndPort("192.168.126.129", 7000));
        nodes.add(new HostAndPort("192.168.126.129", 7001));
        nodes.add(new HostAndPort("192.168.126.129", 7002));
        nodes.add(new HostAndPort("192.168.126.129", 7003));
        nodes.add(new HostAndPort("192.168.126.129", 7004));
        nodes.add(new HostAndPort("192.168.126.129", 7005));
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(1000);
        poolConfig.setMaxIdle(40);
        poolConfig.setMinIdle(20);
        JedisCluster jedisCluster = new JedisCluster(nodes);
        jedisCluster.set("jedisCluster","redis集群搭建");
        System.out.println(jedisCluster.get("jedisCluster"));
    }
}
