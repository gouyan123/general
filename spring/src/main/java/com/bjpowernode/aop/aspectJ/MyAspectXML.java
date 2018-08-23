package com.bjpowernode.aop.aspectJ;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;

public class MyAspectXML {

    public void before(){
        System.out.println("执行前置通知方法");
    }

    public void before(JoinPoint jp){
        System.out.println("执行前置通知方法 jp = " + jp);
    }

    public void afterReturning(Object result){
        if (result != null){
            result = result.toString().toUpperCase();
        }
        System.out.println("执行后置通知 result = " + result);
    }

    public Object around(ProceedingJoinPoint pjp){
        Object result = null;

        System.out.println("环绕通知，目标方法执行前");
        try {
            result = pjp.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        System.out.println("环绕通知，目标方法执行后");
        if (result != null){
            result = ((String)result).toUpperCase();
        }
        return result;
    }

    public void afterThrowing(){
        System.out.println("执行异常通知");
    }

    public void afterThrowing(UsernameException ex){
        System.out.println("执行异常通知 ex = " + ex.getMessage());
    }

    public void afterThrowing(PasswordException ex){
        System.out.println("执行异常通知 ex = " + ex.getMessage());
    }

    public void after(){
        System.out.println("执行最终通知");
    }



}
