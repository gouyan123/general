package com.bjpowernode.aop.aspectJ;

import com.bjpowernode.aop.introduction.UserException;
import com.bjpowernode.aop.introduction.UsernameException;

public interface ISomeService {
    /*接口里面所有方法都叫连接点，只有织入切面的叫切入点*/
    /*通知只能指定切入时间点（目标方法执行前/后/异常时），不能指定切入点*/
    /*目标方法*/
    public abstract void doFirst();
    /*目标方法*/
    public abstract String doSecond();
    /*目标方法*/
    public abstract String doThird();
    /*目标方法*/
    public abstract boolean login(String name,String passwd) throws UsernameException, UserException, PasswordException;
}
