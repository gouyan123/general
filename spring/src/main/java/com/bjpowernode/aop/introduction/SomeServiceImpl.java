package com.bjpowernode.aop.introduction;

public class SomeServiceImpl implements ISomeService{
    /*主业务逻辑：接口方法*/
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
    public void doThird() {
        System.out.println("执行doThrid方法");
    }

    @Override
    public boolean login(String name, String passwd) throws UserException {

        if (!name.equals("beijing")){
            throw new UsernameException("用户名错误");
        }
        if (!passwd.equals("beijing")){
            throw new UsernameException("密码错误");
        }
        return true;
    }


}
