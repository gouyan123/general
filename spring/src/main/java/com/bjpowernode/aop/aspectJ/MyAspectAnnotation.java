package com.bjpowernode.aop.aspectJ;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

@Aspect     /*表示当前类为切面*/
public class MyAspectAnnotation {
    /*将doFirst作为切入点，织入前置通知*/
    @Before(value = "execution(* *..ISomeService.doFirst(..))")/*前置通知*/
    public void before(){
        System.out.println("执行前置通知方法");
    }
    @Before(value = "execution(* *..ISomeService.doFirst(..))")
    public void before(JoinPoint jp){
        System.out.println("执行前置通知方法 jp = " + jp);
    }
    @AfterReturning(value = "execution(* *..ISomeService.doSecond(..))",returning="result")/*后置通知，方法无返回值，因此不可以修改目标方法返回值*/
    public void afterReturning(Object result){
        System.out.println("执行后置通知 result = " + result);
    }
    @Around(value = "execution(* *..ISomeService.doThird(..))")/*环绕通知，方法有返回值，可以修改返回值*/
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
    @AfterThrowing(value = "execution(* *..ISomeService.login(..))",throwing = "ex")
    public void afterThrowing(Exception ex){
        System.out.println("执行异常通知 ex = " + ex.getMessage());
    }
//    @AfterThrowing(value = "execution(* *..ISomeService.login(..))",throwing = "ex")
//    public void afterThrowing(UsernameException ex){
//        System.out.println("执行异常通知 ex = " + ex.getMessage());
//    }
//    @AfterThrowing(value = "execution(* *..ISomeService.login(..))",throwing = "ex")
//    public void afterThrowing(PasswordException ex){
//        System.out.println("执行异常通知 ex = " + ex.getMessage());
//    }
    @After(value = "execution(* *..ISomeService.login(..))")
    public void after(){
        System.out.println("执行最终通知");
    }

    /*定义一个切入点，叫doThirdPointcut()*/
    @Pointcut("execution(* *..ISomeService.doThird(..))")
    public void doThirdPointcut(){}
    /*使用切入点*/

    @After("doThirdPointcut()")
    public void after02(){
        System.out.println("执行最终通知");
    }
}
