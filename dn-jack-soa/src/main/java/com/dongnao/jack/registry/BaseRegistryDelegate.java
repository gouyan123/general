package com.dongnao.jack.registry;

import java.util.List;
import org.springframework.context.ApplicationContext;
import com.dongnao.jack.configBean.Registry;

public class BaseRegistryDelegate {
    public static void registry(String ref,ApplicationContext applicationContext){
        /*根据类型获取spring容器中registry对象*/
        Registry registry = applicationContext.getBean(Registry.class);
        String protocol = registry.getProtocol();
        /*注册中心有多个，该如何处理*/
        /**根据protocol在registryMap中获取 注册中心处理类*/
        BaseRegistry baseRegistry = Registry.getRegistryMap().get(protocol);
        /**注册中心处理类 将 注册内容 注册到 注册中心*/
        baseRegistry.registry(ref, applicationContext);
    }
    public static List<String> getRegistry(String id,
                                           ApplicationContext applicationContext) {
        Registry registry = applicationContext.getBean(Registry.class);
        String protocol = registry.getProtocol();
        BaseRegistry registryBean = registry.getRegistryMap().get(protocol);
        return registryBean.getRegistry(id, applicationContext);
    }
}
