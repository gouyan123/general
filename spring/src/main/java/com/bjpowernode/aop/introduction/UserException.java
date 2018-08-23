package com.bjpowernode.aop.introduction;

/*自定义异常*/
/*异常分类：①运行时异常,即继承RunTimeException；②编译时异常，即Exception*/
public class UserException extends Exception {
    public UserException() {
    }

    public UserException(String message) {
        super(message);
    }
}
