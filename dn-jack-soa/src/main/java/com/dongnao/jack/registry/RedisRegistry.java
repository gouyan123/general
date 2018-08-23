package com.dongnao.jack.registry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.alibaba.fastjson.JSONObject;
import com.dongnao.jack.configBean.Protocol;
import com.dongnao.jack.configBean.Registry;
import com.dongnao.jack.configBean.Service;
import com.dongnao.jack.redis.RedisApi;
import org.springframework.context.ApplicationContext;

/**这个是redis的注册中心处理类*/
public class RedisRegistry implements BaseRegistry {
    @Override
    public boolean registry(String ref, ApplicationContext applicationContext) {
        try {
            Protocol protocol = applicationContext.getBean(Protocol.class);
            /**Service类创建多个对象保存在spring容器中；getBeansOfType(Service.class)获得
             * Service类的所有实例*/
            Map<String, Service> services = applicationContext.getBeansOfType(Service.class);
            Registry registry = applicationContext.getBean(Registry.class);
            /*获取注册标签registry的属性address*/
            /*需要redis客户端连接redis服务端，并操作服务端*/
            RedisApi.createJedisPool(registry.getAddress());
            for (Map.Entry<String, Service> entry : services.entrySet()) {
                if (entry.getValue().getRef().equals(ref)) {
                    JSONObject jo = new JSONObject();
                    jo.put("protocol", JSONObject.toJSONString(protocol));
                    /*service将bean对象转换成JSON字符串，保存到redis中*/
                    jo.put("service", JSONObject.toJSONString(entry.getValue()));
                    JSONObject ipport = new JSONObject();
                    ipport.put(protocol.getHost() + ":" + protocol.getPort(),
                            jo);
                    //                RedisApi.lpush(ipport, ref);
                    /*定义lpush()方法，目的：去重*/
                    lpush(ipport, ref);
                }
            }
            return true;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    @Override
    public List<String> getRegistry(String id, ApplicationContext applicationContext) {
        try {
            Registry registry = applicationContext.getBean(Registry.class);
            RedisApi.createJedisPool(registry.getAddress());
            if (RedisApi.exists(id)) {
                //拿key对应的list
                return RedisApi.lrange(id);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void lpush(JSONObject ipport, String key) {
        if (RedisApi.exists(key)) {
            Set<String> keys = ipport.keySet();
            String ipportStr = "";
            //这个循环里面只会循环一次
            for (String kk : keys) {
                ipportStr = kk;
            }

            //拿redis对应key里面的 内容
            List<String> registryInfo = RedisApi.lrange(key);
            List<String> newRegistry = new ArrayList<String>();

            boolean isold = false;

            for (String node : registryInfo) {
                JSONObject jo = JSONObject.parseObject(node);
                if (jo.containsKey(ipportStr)) {
                    newRegistry.add(ipport.toJSONString());
                    isold = true;
                }
                else {
                    newRegistry.add(node);
                }
            }

            if (isold) {
                //这里是老机器启动去重
                if (newRegistry.size() > 0) {
                    RedisApi.del(key);
                    String[] newReStr = new String[newRegistry.size()];
                    for (int i = 0; i < newRegistry.size(); i++) {
                        newReStr[i] = newRegistry.get(i);
                    }
                    RedisApi.lpush(key, newReStr);
                }
            }
            else {
                //这里是加入新启动的机器
                RedisApi.lpush(key, ipport.toJSONString());
            }
        }
        else {
            //所有的都是第一次啟動
            RedisApi.lpush(key, ipport.toJSONString());
        }
    }
}
