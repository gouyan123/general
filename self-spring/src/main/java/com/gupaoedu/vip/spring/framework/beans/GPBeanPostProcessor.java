package com.gupaoedu.vip.spring.framework.beans;

/*事件监听器*/
public class GPBeanPostProcessor {
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        return bean;
    }
    public Object postProcessAfterInitialization(Object bean, String beanName){
        return bean;
    }
}
