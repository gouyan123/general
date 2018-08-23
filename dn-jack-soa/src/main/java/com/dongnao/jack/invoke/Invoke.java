package com.dongnao.jack.invoke;

/*返回String，用json的方式进行通信*/
public interface Invoke {
    /*invocation就是对要传递参数(Object proxy, Method method, Object[] args)的一个封装*/
    public abstract String invoke(Invocation invocation) throws Exception;
}
