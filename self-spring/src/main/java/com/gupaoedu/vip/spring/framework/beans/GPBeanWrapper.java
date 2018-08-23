package com.gupaoedu.vip.spring.framework.beans;

import com.gupaoedu.vip.spring.framework.aop.GPAopConfig;
import com.gupaoedu.vip.spring.framework.aop.GPAopProxy;
import com.gupaoedu.vip.spring.framework.core.GPFactoryBean;

/**GPBeanWrapper封装 原始类对象 GPAopProxy动态代理对象 原始类包装后的对象*/
public class GPBeanWrapper extends GPFactoryBean {
    /*GPAopProxy类封装getProxy(originInstance)方法，取得原始类对象的代理类对象*/
    private GPAopProxy aopProxy = new GPAopProxy();
    /*原始的实例化对象，即直接由 类全名 反射出来的*/
    private Object originalInstance;
    /*包装后的实例化对象*/
    private Object wrapperInstance;
    /*事件监听类对象：监听方法 handleEvent(Event event)，以事件为参数*/
    private GPBeanPostProcessor postProcessor;
    public GPBeanWrapper(Object instance) {
        this.originalInstance = instance;
        this.wrapperInstance = this.aopProxy.getProxy(instance);
    }
    /*返回代理以后的实例对象的Class，可能会是这个 $Proxy0*/
    public Class<?> getWrappedClass(){
        return this.wrapperInstance.getClass();
    }
    public Object getOriginalInstance() {
        return originalInstance;
    }
    public void setOriginalInstance(Object originalInstance) {
        this.originalInstance = originalInstance;
    }
    public Object getWrapperInstance() {
        return this.wrapperInstance;
    }
    public void setWrapperInstance(Object wrapperInstance) {
        this.wrapperInstance = wrapperInstance;
    }
    public GPBeanPostProcessor getPostProcessor() {
        return postProcessor;
    }
    public void setPostProcessor(GPBeanPostProcessor postProcessor) {
        this.postProcessor = postProcessor;
    }
    public void setAopConfig(GPAopConfig aopConfig){
        this.aopProxy.setConfig(aopConfig);
    }
}
