package com.gupaoedu.vip.spring.framework.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.regex.Pattern;
//默认就用JDK动态代理
public class GPAopProxy implements InvocationHandler{
    /**GPAopConfig：封装配置文件中 切入点方法对象 切面类对象*/
    private GPAopConfig aopConfig;
    public void setConfig(GPAopConfig aopConfig){
        this.aopConfig = aopConfig;
    }
    /*代理类实现代理接口 InvocationHandler的代理方法 invoke(proxy,method,args)其中method为目标类方法对象*/
    /*代理类实现代理接口 InvocationHandler的代理方法invoke()，并持有目标类对象*/
    private Object target;
    /*对目标方法进行增强*/
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        /**method为目标接口的方法，由该方法得到其实现类即目标类的方法对象*/
        Method m = this.target.getClass().getMethod(method.getName(),method.getParameterTypes());
        //作业：利用AOP思想，自己去实现一个TransactionManager
        //你们需要补充：把Method的异常拿到，把Method的方法拿到
        //把Method的参数拿到
        //args的值就是实参
        //我们今天所讲的所有的内容只是客户端的操作API
        //明天我们讲数据库Server端的实现原理
//        method.getExceptionTypes();
        //根据正则匹配
//        String methodName = method.getName();
//        Pattern p = null;
//        if(p.matcher(methodName)){
//            conn.setReadOnly(true);
//        }
        //在原始方法调用以前要执行增强的代码
        //这里需要通过原生方法去找，通过代理方法去Map中是找不到的
        if(this.aopConfig.contains(m)){
            /*由切入点方法对象获得对应的切面类对象和切面类中切面方法对象的封装*/
            GPAopConfig.GPAspect aspect = this.aopConfig.get(m);
            /*获得切面方法对象，并触发切面类中的切面方法*/
            aspect.getAspectMethods()[0].invoke(aspect.getAspect());
        }
        // try {
        //反射调用目标方法即需要增强的方法
        Object obj = method.invoke(this.target, args);
        System.out.println(args);
        //}catch (Exception e){
        //e.getClass();
//            if(e instanceof  Exception){
//                con.rollback();
//            }
        //}
        //在目标方法调用以后要执行增强的代码
        if(this.aopConfig.contains(m)){
            GPAopConfig.GPAspect aspect = this.aopConfig.get(m);
            aspect.getAspectMethods()[1].invoke(aspect.getAspect());
        }
        //将最原始的返回值返回出去
        return obj;
    }
    //把原生的对象传进来，并获得代理对象
    public Object getProxy(Object instance){
        this.target = instance;
        Class<?> clazz = instance.getClass();
        return Proxy.newProxyInstance(clazz.getClassLoader(),clazz.getInterfaces(),this);
    }
}
