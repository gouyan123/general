package com.dongnao.jack.configBean;

import com.dongnao.jack.registry.BaseRegistry;
import com.dongnao.jack.registry.RedisRegistry;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Registry extends BaseConfigBean implements ApplicationContextAware {
    private String protocol;
    private String address;
    private ApplicationContext applicationContext;
    private static Map<String,BaseRegistry> registryMap = new HashMap<String,BaseRegistry>();
    static {
        registryMap.put("redis",new RedisRegistry());
    }
    public Registry() {
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getAddress() {
        return address;
    }

    public static Map<String, BaseRegistry> getRegistryMap() {
        return Registry.registryMap;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
