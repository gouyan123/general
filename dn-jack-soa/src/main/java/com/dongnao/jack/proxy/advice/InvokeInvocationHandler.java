package com.dongnao.jack.proxy.advice;

import com.dongnao.jack.cluster.Cluster;
import com.dongnao.jack.configBean.Reference;
import com.dongnao.jack.invoke.Invocation;
import com.dongnao.jack.invoke.Invoke;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/*InvokeInvocationHandler是一个 advice增强 在这个advice里面进行了rpc远程调用：http，netty，rmi
因此，采用策略模式*/
public class InvokeInvocationHandler implements InvocationHandler {
    private Invoke invoke;
    /*①reference里面封装了调用者的所有信息；②该对象由xml配置文件解析而来*/
    private Reference reference;
    public InvokeInvocationHandler() {
    }
    /**Invoke是一个接口，传哪个实现类，就运行哪个实现类的方法*/
    public InvokeInvocationHandler(Invoke invoke,Reference reference) {
        this.reference = reference;
        this.invoke = invoke;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("已经进入了代理");
        /*在这个invoke()里面最终要调用多个远程的provider*/
        /*reference已经从registry中获取到了相应的JSON字符串，那么怎么调用呢*/
        /*invocation对象主要用来封装 invoke()方法的method和args*/
        Invocation invocation = new Invocation();
        invocation.setMethod(method);
        invocation.setArgs(args);
        invocation.setReference(this.reference);
        //String result = invoke.invoke(invocation);

        invocation.setInvoke(invoke);
        /*获取集群配置*/
        Cluster cluster = Reference.getClusters().get(reference.getCluster());
        /**策略模式：invoke方法里面为什么不直接将 invacation发送到 生产者端，而是创建 Invoke接口
        及其实现类HttpInvoke NettyInvoke RmiInvoke？
        答：通过传过来一个 *Invoke参数，Invoke接口接收，通过该 *Invoke对象里面的方法发送，实现策略模式*/
        String result = cluster.invoke(invocation);
        return result;
    }
}
