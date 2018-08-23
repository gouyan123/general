package com.bjpowernode.aop.aspectJ;

public class UsernameException extends UserException {
    public UsernameException() {
        super();
    }

    public UsernameException(String message) {
        System.out.println("用户名异常");
    }
}
