package com.bjpowernode.aop.introduction;

import org.springframework.aop.ThrowsAdvice;
//异常通知
//ThrowsAdvice接口没有方法，是为了里面方法重载方便
public class MyThrowsAdvice implements ThrowsAdvice {
    /*当目标方法抛出UsernameException异常时，执行此方法*/
    public void afterThrowing(UsernameException e){
        System.out.println("执行异常通知方法 e = " + e.getMessage());
    }
    /*当目标方法抛出PasswordException异常时，执行此方法*/
    public void afterThrowing(PasswordException e){
        System.out.println("执行异常通知方法 e = " + e.getMessage());
    }
    /*当目标方法抛出其他异常时，执行此方法*/
    public void afterThrowing(Exception e){
        System.out.println("执行异常通知方法 e = " + e.getMessage());
    }
}
