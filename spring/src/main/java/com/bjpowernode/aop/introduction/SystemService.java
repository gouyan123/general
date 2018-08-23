package com.bjpowernode.aop.introduction;

public class SystemService {
    public static void doTx(){
        System.out.println("事务处理");
    }

    public static void doLog(){
        System.out.println("日志输出");
    }
}
