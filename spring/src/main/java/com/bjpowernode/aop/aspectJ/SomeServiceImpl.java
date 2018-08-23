package com.bjpowernode.aop.aspectJ;

import com.bjpowernode.aop.introduction.UsernameException;

public class SomeServiceImpl implements ISomeService {
    /*主业务逻辑：接口方法*/
    /*将doFirst作为切入点，织入前置通知*/
    @Override
    public void doFirst() {
        System.out.println("执行doFirst方法");
    }
    /*主业务逻辑：接口方法*/
    @Override
    public String doSecond() {
        System.out.println("执行doSecond方法");
        return "abc";
    }

    @Override
    public String doThird() {
        System.out.println("执行doThrid方法");
        return "abc";
    }

    @Override
    public boolean login(String name, String passwd) throws UsernameException, PasswordException {
        if (!name.equals("beijing")){
                throw new UsernameException("用户名错误~~~~");
        }
        if (!passwd.equals("beijing")){
                throw new PasswordException("密码错误~~~~~");
        }
        return true;
    }
}
