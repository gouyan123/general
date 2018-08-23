package com.gupaoedu.rpc.consumer;

import com.gupaoedu.rpc.api.IRpcHello;
import com.gupaoedu.rpc.consumer.proxy.RpcProxy;
import com.gupaoedu.rpc.provider.RpcHello;

public class RpcConsumer {
    public static void main(String[] args) {
        /*正常情况下，即在同一个 JVM 下，这样调用*/
//        IRpcHello rc =  new RpcHello();
//        String result = rc.hello("Tom");
//        System.out.println(result);
        /*RPC远程调用情况下，调用一个接口实现方法，通过动态代理方式，将请求发送给注册中心，然后获得处理结果*/
        /*将接口反射类对象传递进去，返回一个接口实现类实例*/
        IRpcHello rpcHello = RpcProxy.create(IRpcHello.class);
        String result = rpcHello.hello("James");
        System.out.println(result);
    }
}
