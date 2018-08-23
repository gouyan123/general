package com.dongnao.jack.redis;

import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;

public class RedisApi {

    private static JedisPool pool;
    private static Properties prop = null;
    private static JedisPoolConfig config = null;
    static {
        File file = new File("D:/ideaproject/general/dn-jack-soa/src/main/java/com/dongnao/jack/redis/redis.properties");
        InputStream in = null;
        try {
            in = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
            prop = new Properties();
        try {
            prop.load(in);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        config = new JedisPoolConfig();
        config.setMaxTotal(Integer.valueOf(prop.getProperty("MAX_TOTAL")));
        config.setMaxIdle(Integer.valueOf(prop.getProperty("MAX_IDLE")));
        config.setMaxWaitMillis(Integer.valueOf(prop.getProperty("MAX_WAIT_MILLIS")));
        config.setTestOnBorrow(Boolean.valueOf(prop.getProperty("TEST_ON_BORROW")));
        config.setTestOnReturn(Boolean.valueOf(prop.getProperty("TEST_ON_RETURN")));
        config.setTestWhileIdle(Boolean.valueOf(prop.getProperty("TEST_WHILE_IDLE")));
        
    }
    
    public static void createJedisPool(String address) {
        pool = new JedisPool(config, address.split(":")[0],
                Integer.valueOf(address.split(":")[1]), 100000);
    }

    public static JedisPool getPool() {
        
        if (pool == null) {
            
            JedisPoolConfig config = new JedisPoolConfig();

            config.setMaxTotal(Integer.valueOf(prop.getProperty("MAX_TOTAL")));
            config.setMaxIdle(Integer.valueOf(prop.getProperty("MAX_IDLE")));
            config.setMaxWaitMillis(Integer.valueOf(prop.getProperty("MAX_WAIT_MILLIS")));
            config.setTestOnBorrow(Boolean.valueOf(prop.getProperty("TEST_ON_BORROW")));
            config.setTestOnReturn(Boolean.valueOf(prop.getProperty("TEST_ON_RETURN")));
            config.setTestWhileIdle(Boolean.valueOf(prop.getProperty("TEST_WHILE_IDLE")));
            pool = new JedisPool(config, prop.getProperty("REDIS_IP"),
                    Integer.valueOf(prop.getProperty("REDIS_PORT")));
        }
        
        return pool;
    }
    
    public static void returnResource(JedisPool pool, Jedis jedis) {
        if (jedis != null) {
            pool.returnResource(jedis);
        }
    }

    public static void publish(String channel, String msg) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.publish(channel, msg);
        }
        catch (Exception e) {
            
        }
        finally {
            returnResource(pool, jedis);
        }
    }
    
    public static void subsribe(String channel, JedisPubSub ps) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.subscribe(ps, channel);
        }
        catch (Exception e) {
            
        }
        finally {
            returnResource(pool, jedis);
        }
    }
    
    public static Long hdel(String key, String key1) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.hdel(key, key1);
        }
        catch (Exception e) {
            
        }
        finally {
            returnResource(pool, jedis);
        }
        return null;
    }

    public static String get(String key) {
        Jedis jedis = null;
        String value = null;
        try {
            jedis = pool.getResource();
            value = jedis.get(key);
        }
        catch (Exception e) {
            
        }
        finally {
            returnResource(pool, jedis);
        }
        return value;
    }
    
    public static boolean exists(String key) {
        Jedis jedis = null;
        boolean value = false;
        try {
            jedis = pool.getResource();
            value = jedis.exists(key);
        }
        catch (Exception e) {
            
        }
        finally {
            returnResource(pool, jedis);
        }
        return value;
    }

    public static String set(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.set(key, value);
        }
        catch (Exception e) {
            
            return "0";
        }
        finally {
            returnResource(pool, jedis);
        }
    }
    
    public static String set(String key, String value, int expire) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.expire(key, expire);
            return jedis.set(key, value);
        }
        catch (Exception e) {
            
            return "0";
        }
        finally {
            returnResource(pool, jedis);
        }
    }
    
    public static Long del(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.del(key);
        }
        catch (Exception e) {
            return null;
        }
        finally {
            returnResource(pool, jedis);
        }
    }
    
    public static Long lpush(String key, String... strings) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.lpush(key, strings);
        }
        catch (Exception e) {
            
            return 0L;
        }
        finally {
            returnResource(pool, jedis);
        }
    }
    
    public static List<String> lrange(String key) {
        Jedis jedis = null;
        String value = null;
        try {
            jedis = pool.getResource();
            return jedis.lrange(key, 0, -1);
        }
        catch (Exception e) {
            return null;
        }
        finally {
            returnResource(pool, jedis);
        }
    }
    
    public static String hmset(String key, Map map) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.hmset(key, map);
        }
        catch (Exception e) {
            
            return "0";
        }
        finally {
            returnResource(pool, jedis);
        }
    }
    
    public static List<String> hmget(String key, String... strings) {
        Jedis jedis = null;
        String value = null;
        try {
            jedis = pool.getResource();
            return jedis.hmget(key, strings);
        }
        catch (Exception e) {
            
        }
        finally {
            returnResource(pool, jedis);
        }
        return null;
    }

    public static void main(String[] args) {
        Jedis jedis = new Jedis("101.132.109.12",6379);
        jedis.auth(String.valueOf(123456));
        System.out.println(jedis);
        jedis.set("gy", String.valueOf(123));
    }
}
