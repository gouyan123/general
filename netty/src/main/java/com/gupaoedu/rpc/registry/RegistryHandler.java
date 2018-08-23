package com.gupaoedu.rpc.registry;

import com.gupaoedu.rpc.core.msg.InvokerMsg;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class RegistryHandler extends ChannelInboundHandlerAdapter {
    /*注册中心需要一个容器，存放注册的服务信息*/
    public static ConcurrentHashMap<String,Object> registryMap = new ConcurrentHashMap<>();
    /*保存类全名，例如 "com.gupaoedu.rpc.provider.RpcHello" */
    private List<String> classCache = new ArrayList<String>();
    public RegistryHandler() {
        this.scannerClass("com.gupaoedu.rpc.provider");
        this.doRegister();
    }
    /*消费者来调用服务端已注册的服务(接口)中方法*/
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Object result = new Object();
        /*将客户端发送过来的 msg 转成自己需要的形式*/
        InvokerMsg request = (InvokerMsg)msg;
        /*先判断registryMap是否有这个服务即接口名称*/
        if(registryMap.containsKey(request.getClassName())){
            /*从注册中心取出 服务 即接口实现类的对象*/
            Object obj = registryMap.get(request.getClassName());
            Method method = obj.getClass().getMethod(request.getMethodName(), request.getParames());
            /*执行方法获得返回值*/
            result = method.invoke(obj, request.getValues());
        }
        /*将返回值写会给客户端即消费者*/
        ctx.writeAndFlush(result);
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

    /*dubbo是spring中的，因此简单写一下IOC容器*/
    /*约定：com.gupaoedu.rpc.provider包下面的类都认为是一个可以对外提供服务的实现类*/
    /*扫描出所有 Class*/
    private void scannerClass(String packageName){
        URL url = this.getClass().getClassLoader().getResource(packageName.replaceAll("\\.", "/"));
        File dir = new File(url.getFile());
        for (File file : dir.listFiles()) {
            //如果是一个文件夹，继续递归
            if(file.isDirectory()){
                scannerClass(packageName + "." + file.getName());
            }else{
                classCache.add(packageName + "." + file.getName().replace(".class", "").trim());
            }
        }
    }
    /*将Class注册到注册中心，即将扫描到的Class实例化，然后放到map中，注册的服务名字，叫接口名字*/
    private void doRegister(){
        if(classCache.size() == 0){ return; }
        /*classNames是一个类全名的字符串，例如 com.gupaoedu.rpc.provider.RpcHello*/
        for (String className : classCache) {
            try {
                Class<?> clazz = Class.forName(className);
                /*服务名*/
                Class<?> interfaces = clazz.getInterfaces()[0];
                /*将服务名称，服务对象引用保存到map中*/
                registryMap.put(interfaces.getName(), clazz.newInstance());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
