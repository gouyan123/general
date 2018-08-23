package com.bjpowernode.ioc;

public class ServiceFactory {
    public com.bjpowernode.ioc.ISomeService getSomeService(){
        return new com.bjpowernode.ioc.SomeServiceImpl();
    }
}
