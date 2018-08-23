package com.gupaoedu.vip.spring.framework.context;

import com.gupaoedu.vip.spring.framework.beans.GPBeanDefinition;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GPDefaultListableBeanFactory extends GPAbstractApplicationContext {
    //beanDefinitionMap用来保存配置信息(key:beanName,value:beanDefinition里面封装beanName，beanClass)
    protected Map<String,GPBeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String,GPBeanDefinition>();
    protected void onRefresh(){

    }
    @Override
    protected void refreshBeanFactory() {

    }
}
