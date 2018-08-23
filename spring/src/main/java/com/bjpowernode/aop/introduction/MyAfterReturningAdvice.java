package com.bjpowernode.aop.introduction;

import org.springframework.aop.AfterReturningAdvice;
import java.lang.reflect.Method;
/*后置通知可以获取目标方法返回结果，但无法改变目标方法的返回值，因为后置方法无返回值*/
public class MyAfterReturningAdvice implements AfterReturningAdvice {
    @Override
    public void afterReturning(Object returnValue, Method method, Object[] objects, Object target) throws Throwable {
        System.out.println("执行后置通知");
        if (returnValue != null){
            returnValue = ((String)returnValue).toUpperCase();
        }
    }
}
