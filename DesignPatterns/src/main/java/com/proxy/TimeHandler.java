package com.proxy;

import java.lang.reflect.Method;

public class TimeHandler implements InvocationHandler {
    /*代理类持有目标类对象*/
    private Object target;

    public TimeHandler(Object target) {
        this.target = target;
    }
    /*Method m 为目标方法*/
    @Override
    public void invoke(Method m,Object obj,Object[] args) {
        long start = System.currentTimeMillis();
        try {
            m.invoke(this.target,args);
        } catch (Exception e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println("time = " + (end - start));
    }
}
