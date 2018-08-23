package com.bjpowernode.aop.introduction;

import org.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;

public class MyMethodBeforeAdvice implements MethodBeforeAdvice {
    /*在目标方法执行之前执行*/
    @Override        // 目标方法      参数              目标对象
    public void before(Method method, Object[] objects, Object o) throws Throwable {
        /*目标方法的增强代码应该写在这里*/
        System.out.println("执行前置通知");
    }
}
