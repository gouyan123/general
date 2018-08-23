package com.bjpowernode.aop.aspectJ;

public class PasswordException extends UserException {
    public PasswordException() {
        super();
    }

    public PasswordException(String message) {
        System.out.println("用户密码异常");;
    }
}
