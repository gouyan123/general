package com.bjpowernode.aop.introduction;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

//cglig原理，可以改变返回值
public class MyMethodInterceptor implements MethodInterceptor {
    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        System.out.println("目标方法执行之前");

        Object result = methodInvocation.proceed();

        System.out.println("目标方法执行之后");
        if (result != null){
            result = ((String)result).toUpperCase();
        }
        return result;
    }
}
