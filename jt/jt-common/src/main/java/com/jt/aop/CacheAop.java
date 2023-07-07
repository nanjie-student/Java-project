package com.jt.aop;

import com.jt.annotation.CacheFind;
import com.jt.util.ObjectMapperUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.ShardedJedis;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Aspect //标示我是一个切面
@Component //将切面交给spring容器管理
public class CacheAop {
    @Autowired(required = false) //类似于懒加载
    //private Jedis jedis;//redis单台测试
    //private ShardedJedis jedis;//redis分片操作
    //private JedisSentinelPool sentinelPool;//注入池对象
    private JedisCluster jedis;//注入集群对象
    /*
    * 实现思路：
    * 1.动态获取key 用户自定义的前缀+用户的参数[0,xx]
    * 2.判断key是否存在
    *       存在:直接从redis中获取数据json，需要将json转化为具体对象，按照返回值类型进行转化
    *       不存在:执行目标方法，获得返回值数据，将返回值的结果转化为json格式，之后保存到缓存中
    * @param joinpoint
    * */

    @Around("@annotation(cacheFind)")
    public Object cacheAround(ProceedingJoinPoint joinPoint,CacheFind cacheFind) throws NoSuchMethodException {
        //Jedis jedis =sentinelPool.getResource();

        Object result = null;
        //0.找到类，找到方法名，找到方法的类型(参数)
        //1.首先获取方法对象
        //2.从方法对象中获取指定的注解
        //String key =joinPoint.getClass().getMethod("aaa", null)
        //        .getAnnotation(CacheFind.class).key();//反射得到的key
        //1.获取key的前缀
        String key = cacheFind.key();
        //2.获取key的参数
        String argString = Arrays.toString(joinPoint.getArgs());
        key = key +"::" + argString;
        //System.out.println(key);
        //System.out.println("环绕通知开始");
        try{
            //3.判断缓存中是否有数据
            //3.1缓存中有数据
            if(jedis.exists(key)){
                //获取key
                String json = jedis.get(key);
                //5.如何获取返回值类型
                MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
                result = ObjectMapperUtil.toObject(json,methodSignature.getReturnType());
                System.out.println("AOP执行缓存调用");
            }else{
                //3.2表示缓存中没有数据
                result = joinPoint.proceed();
                String json = ObjectMapperUtil.toJson(result);
                //4.判断数据中是否有超时时间
                if(cacheFind.seconds()>0){
                    jedis.setex(key, cacheFind.seconds(), json);
                }else{
                    jedis.set(key, json);
                }
                System.out.println("数据库调用！！！！");

            }
            result = joinPoint.proceed();
        }catch(Throwable throwable){
            throwable.printStackTrace();
            throw new RuntimeException(throwable);
        }
        System.out.println("环绕通知结束");
       // jedis.close();//还池操作
        return result;
    }
    //切面=切入点表达式+通知
    //切入点表达式不需要返回值
    //表达式1:bean(ItemCatServiceImpl)
//    @Pointcut("bean(itemCatServiceImpl)")
//    public void pointcut(){
//    }
    //@Pointcut("within(com.jt.service.*)")
    //.*一级包下的类 ..*所有的子孙后代的包和类
    //返回值类型任意，com.jt.service包下的所有类的add方法参数类型任意类型
    //写参数类型时得注意大小写
    //@Pointcut("execution(* com.jt.service.*)")
    //@Pointcut("bean(itemCatServiceImpl)")
    //public void pointcut(){ }
    /*
    * joinPoint代表连接点对象,  程序执行的方法
    * 客人                        路人
    * 进入切面才变成连接点，没进入就是一个普通方法
    * joinPoint代表连接点对象，一般适用于除around以外的四大通知*/
    /*@Before("pointcut()")
    public void before(JoinPoint joinPoint){
        //1.获取目标对象
        Object target = joinPoint.getTarget();
        //2.获取目标对象的路径 包名.类名.方法名
        String className =joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        System.out.println("目标方法路径"+(className +"."+ methodName));
        //3.获取参数对象
        System.out.println(Arrays.toString(joinPoint.getArgs()));
    }*/
    /*@Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint){
        System.out.println("环绕通知执行");
        Object data = null;
        try{
            data = joinPoint.proceed();
        }catch(Throwable throwable){
            throwable.printStackTrace();
        }
        return data;
    }
    */
    /*@Pointcut("@annotation(com.jt.annotation.CacheFind)")
    public void  cache(){}

    @Around("@annotation(com.jt.annotation.CacheFind)")
    public List cacheAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        List treeList = new ArrayList();

        Class<?> targetCls=joinPoint.getTarget().getClass();
        System.out.println("targetCls="+targetCls.getName());
        //获取目标方法对象
        MethodSignature ms=(MethodSignature) joinPoint.getSignature();//获取方法签名(存储了方法信息的一个对象)
        Method targetMethod=
                targetCls.getDeclaredMethod(ms.getName(),ms.getParameterTypes());
        CacheFind cacheFind=targetMethod.getAnnotation(CacheFind.class);
        String key = cacheFind.key();
        Integer seconds = cacheFind.seconds();
        Long parentId = (Long) joinPoint.getArgs()[0];
        //1。定义key
        key = key+parentId;
        //2.在缓存中获取对象
        if(jedis.exists(key)){
            //存在 直接获取缓存数据，之后转化为对象
            String json = jedis.get(key);
            treeList = ObjectMapperUtil.toObject(json,treeList.getClass());
            System.out.println("查询redis缓存，获取数据");
            long endTime = System.currentTimeMillis();
            System.out.println("耗时是"+(endTime-startTime)+"毫秒");
        }else{
            //不存在，应该先查询数据库，之后把数据保存在redis中
            treeList = (List) joinPoint.proceed();
            String json = ObjectMapperUtil.toJson(treeList);
            jedis.setex(key,seconds,json);
            System.out.println("查询数据库获取结果");
            long endTime = System.currentTimeMillis();
            System.out.println("耗时是"+(endTime-startTime)+"毫秒");
        }
        return treeList;
    }*/

}
