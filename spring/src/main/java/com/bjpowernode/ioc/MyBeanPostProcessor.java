package com.bjpowernode.ioc;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class MyBeanPostProcessor implements BeanPostProcessor {
    /*Object o：当前正在初始化的Bean
     *String s：当前正在初始化的Bean的id
    * */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        //该方法在任意一个Bean初始化之前执行
        System.out.println("step5：bean后处理器执行before()方法");
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        //该方法在任意一个Bean初始化之后执行
        System.out.println("step8：bean后处理器执行after()方法");
        Object obj = Proxy.newProxyInstance(bean.getClass().getClassLoader(),
                bean.getClass().getInterfaces(),
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        method.invoke(bean,args);
                        return null;
                    }
                });
        return obj;
    }
}
