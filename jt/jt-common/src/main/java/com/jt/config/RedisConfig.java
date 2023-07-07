package com.jt.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import redis.clients.jedis.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Configuration //表示我是一个配置类 一般和@bean注解连用
@PropertySource("classpath:/properties/redis.properties")
public class RedisConfig {
    @Value(("${redis.nodes}"))
    private String nodes;
    @Bean
    public JedisCluster jedisCluster(){
        Set<HostAndPort> nodesSet = new HashSet<>();
        String[] nodeArray = nodes.split(",");
        for(String node:nodeArray){
            String host = node.split(":")[0];
            int port = Integer.parseInt(node.split(":")[1]);
            HostAndPort hostAndPort = new HostAndPort(host,port);
            nodesSet.add(hostAndPort);
        }
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(1000);
        poolConfig.setMaxIdle(60);
        poolConfig.setMinIdle(20);
        return new JedisCluster(nodesSet,poolConfig);
    }
//    @Value("${redis.node}")
//    private String node;
//    @Bean
//    public JedisSentinelPool jedisSentinelPool(){
//        Set<String> sentinel = new HashSet<>();
//        sentinel.add(node);
//        JedisPoolConfig poolConfig = new JedisPoolConfig();
//        poolConfig.setMaxTotal(1000);
//        poolConfig.setMaxIdle(40);
//        poolConfig.setMinIdle(60);
//        return new JedisSentinelPool("mymaster", sentinel,poolConfig);
//    }
//    @Value("${redis.nodes}")
//    private String nodes;//
//
//    @Bean
//    public ShardedJedis shardedJedis() {
//        List<JedisShardInfo> shards = new ArrayList<>();
//        String[] nodeArray = nodes.split(",");
//        for (String node : nodeArray) { //node=host:port
//            String host = node.split(":")[0];
//            int port = Integer.parseInt(node.split(":")[1]);
//            JedisShardInfo info = new JedisShardInfo(host,port);
//            shards.add(info);
//        }
//        return new ShardedJedis(shards);
//    }
}
    //redis单台操作
//    @Value("${redis.host}")
//    private String host;
//    @Value("${redis.port}")
//    private Integer port;
//    @Bean
//    public Jedis jedis(){
//        return new Jedis(host,port);
//    }
