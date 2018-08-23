package com.proxy;

import java.lang.reflect.Method;
/*目标类的代理类实现的接口*/
public interface InvocationHandler {
    /*Method m为目标方法，invoke()方法中利用反射触发 m 方法，并对其进行增强*/
    /*obj为目标类代理类对象，没用*/
    /*args为方法 m 的参数*/
    public abstract void invoke(Method m, Object obj, Object[] args) throws Exception;
}
