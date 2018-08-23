package com.dongnao.vip.redis.jedis;

import com.alibaba.fastjson.JSON;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Pipeline;

import java.util.List;

public class JedisUtils {
    /*主方法*/
    public static void main(String[] args) {
        JedisUtils jedisUtils = new JedisUtils("39.106.185.193",6379,"123456");
        /*String result = jedisUtils.set("gouyan","123");*/
        /*String result = jedisUtils.get("gouyan");*/
        /*Long result = jedisUtils.del("gouyan","James");*/
        jedisUtils.pipelineSyncAll();
    }
    /**JedisPool：jedis对象连接池，封装 jedis 对象队列 pool.getResource()从连接池中获取客户端连接
     * 服务端对象jedis*/
    private JedisPool pool;
    /**构造方法：封装连接池配置 JedisPoolConfig；创建连接池 JedisPool*/
    public JedisUtils(String ip,int port,String auth){
        /*如果没有连接池，则创建连接池 pool*/
        if (this.pool == null){
        /**JedisPoolConfig 封装 JedisPool 的配置信息*/
            JedisPoolConfig config = new JedisPoolConfig();
            /*pool连接池最大连接数*/
            config.setMaxTotal(500);
            /*pool连接池最大空闲连接数*/
            config.setMaxIdle(5);
            /*borrow(引入)一个jedis实例时，最大等待时间，如果超过等待时间，则直接抛出JedisConnectionException*/
            config.setMaxWaitMillis(100);
            /*在borrow(引入)一个jedis实例时，是否提前进行验证操作；如果为true，则得到的jedis实例均是可用的*/
            config.setTestOnBorrow(true);
            /*创建连接池*/
            this.pool = new JedisPool(config,ip,port,100000);
        }
    }
    /**向redis存入key和value,并释放连接资源 如果key已经存在 则覆盖；成功 返回OK 失败返回 0*/
    public String set(String key,String value){
        Jedis jedis = null;
        try{
            jedis = this.pool.getResource();
            String result = jedis.set(key,value);
            return result;
        }catch (Exception e){
            this.pool.returnBrokenResource(jedis);
            e.printStackTrace();
            return "0";
        }finally {
            /**返还连接池pool jedis 对象*/
            this.returnResource(this.pool, jedis);
        }
    }
    /**返还到连接池*/
    public static void returnResource(JedisPool pool, Jedis jedis) {
        if (jedis != null) {
            pool.returnResource(jedis);
        }
    }
    /**通过key获取储存在redis中的value 并释放连接；成功返回value 失败返回null*/
    public String get(String key) {
        Jedis jedis = null;
        String value = null;
        try {
            jedis = this.pool.getResource();
            value = jedis.get(key);
        } catch (Exception e) {
            pool.returnBrokenResource(jedis);
            e.printStackTrace();
            return null;
        } finally {
            /*返还连接池pool jedis 对象*/
            returnResource(pool, jedis);
        }
        return value;
    }
    public Long del(String... keys){
        Jedis jedis = null;
        try {
            jedis = this.pool.getResource();
            /*删除的数量*/
            Long num = jedis.del(keys);
            return num;
        }catch (Exception e){
            pool.returnBrokenResource(jedis);
            e.printStackTrace();
            return 0L;
        }finally {
            /*返还连接池pool jedis 对象*/
            returnResource(this.pool,jedis);
        }
    }
    /**-------------利用pipeline的事务将多个命令共同执行-------------------*/
    public void pipelineSyncAll(){
        Jedis jedis = null;
        try {
            jedis = this.pool.getResource();
            /*jedis获取多命令共同执行对象 pipeline*/
            Pipeline pipeline = jedis.pipelined();
            /**pipeline事务开始*/
            /**事务开始，事务结束，这两者中间的语句暂不执行，等事务提交时一起执行*/
            pipeline.multi();
            pipeline.set("gouyan","123");
            pipeline.incrBy("age",3);
            pipeline.get("gouyan");
            /**pipelin事务结束*/
            pipeline.exec();
            /**提交事务，list中存事务中每个语句(命令)的返回结果*/
            List<Object> list = pipeline.syncAndReturnAll();
            for (Object obj : list){
                System.out.println(JSON.toJSONString(obj));
            }
        }catch (Exception e){
            pool.returnBrokenResource(jedis);
            e.printStackTrace();
        }finally {
            /*返还连接池pool jedis 对象*/
            returnResource(this.pool,jedis);
        }
    }
}
