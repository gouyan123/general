package com.dongnao.jack.configBean;

import com.dongnao.jack.cluster.Cluster;
import com.dongnao.jack.cluster.FailfastClusterInvoke;
import com.dongnao.jack.cluster.FailoverClusterInvoke;
import com.dongnao.jack.cluster.FailsafeClusterInvoke;
import com.dongnao.jack.invoke.*;
import com.dongnao.jack.loadbalance.LoadBalance;
import com.dongnao.jack.loadbalance.RandomLoadBalance;
import com.dongnao.jack.loadbalance.RoundRobinLoadBalance;
import com.dongnao.jack.proxy.advice.InvokeInvocationHandler;
import com.dongnao.jack.redis.RedisApi;
import com.dongnao.jack.redis.RedisServerRegistry;
import com.dongnao.jack.registry.BaseRegistryDelegate;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Reference extends BaseConfigBean implements Serializable,FactoryBean,ApplicationContextAware,InitializingBean {
    private static final long serialVersionUID = 9147667410124905396L;
    private String id;
    private String intf;/*接口名称的字符串*/
    private String loadbalance;
    private String protocol;
    private String retries;
    private String cluster;
    private ApplicationContext applicationContext;
    //持有接口对象，接口对象.接口方法表示策略模式，此处只调用不实现，具体实现交给实现子类，程序会自动进入实现子类的实现方法中；
    private Invoke invoke;
    /**将从注册中心获取的内容 保存到该列表里面*/
    private List<String> registryInfo = new ArrayList<String>();/*全局配置中心*/
    private static Map<String,Invoke> invokes = new HashMap<String,Invoke>();
    private static Map<String, LoadBalance> loadBalances = new HashMap<String, LoadBalance>();
    private static Map<String, Cluster> clusters = new HashMap<String, Cluster>();
    static {/*策略模式：接口对象调用接口方法，只调用不实现，具体交给子类去实现*/
        invokes.put("http",new HttpInvoke());
        invokes.put("rmi",new RmiInvoke());
        invokes.put("netty",new NettyInvoke());

        loadBalances.put("romdom", new RandomLoadBalance());
        loadBalances.put("roundrob", new RoundRobinLoadBalance());

        clusters.put("failover", new FailoverClusterInvoke());
        clusters.put("failfast", new FailfastClusterInvoke());
        clusters.put("failsafe", new FailsafeClusterInvoke());
    }
    public Reference() {
        System.out.println();
    }
    public List<String> getRegistryInfo() {return registryInfo;}
    public void setRegistryInfo(List<String> registryInfo) {this.registryInfo = registryInfo;}
    public void setId(String id) {
        this.id = id;
    }
    public void setIntf(String intf) {
        this.intf = intf;
    }
    public void setLoadbalance(String loadbalance) {
        this.loadbalance = loadbalance;
    }
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }
    public String getId() {
        return id;
    }
    public String getIntf() {
        return intf;
    }
    public String getLoadbalance() {
        return loadbalance;
    }
    public String getProtocol() {
        return protocol;
    }
    public static Map<String, LoadBalance> getLoadBalances() {return loadBalances;}

    public void setRetries(String retries) {
        this.retries = retries;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }

    public void setInvoke(Invoke invoke) {
        this.invoke = invoke;
    }

    public String getRetries() {
        return retries;
    }

    public String getCluster() {
        return cluster;
    }

    public Invoke getInvoke() {
        return invoke;
    }

    public static Map<String, Cluster> getClusters() {
        return Reference.clusters;
    }

    /*获得一个实例，这个方法是spring初始化时调用的，具体是 getBean(..) 方法里面调用
    * 只要调用 getBean(..)方法，它里面就会调用 getObject()方法
    * getObject()方法的返回值交给 spring 容器进行管理
    * getObject()方法返回的是 intf 这个接口的代理对象，代理再去调用远程生产者
    * */
    @Override
    public Object getObject() throws Exception {
        System.out.println("返回 intf 代理对象");
        if (protocol != null && !"".equals(protocol)){
            this.invoke = invokes.get(protocol);
        }else {
            /*如果<jack:reference 中没有 protocol 属性，将去 <jack:protocol 中找*/
            /*protocol对象在spring容器中，要从容器中获取实例，要拿到sring的上下文，因此Reference要实现ApplicationContextAware接口*/
            Protocol protocol =this.applicationContext.getBean(Protocol.class);/*没有id，则根据类型获得bean*/
            if (protocol != null){
                this.invoke = invokes.get(protocol.getName());
            }else {
                this.invoke = invokes.get("http");/*否则默认http协议*/
            }
        }
        Object proxy = Proxy.newProxyInstance(this.getClass().getClassLoader(),
                new Class<?>[]{Class.forName(intf)},
                new InvokeInvocationHandler(this.invoke, this)
        );
        return proxy;
    }
    /*获取接口类型*/
    @Override
    public Class<?> getObjectType() {
        if (intf != null && !intf.equals("")){
            try {
                return Class.forName(intf);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    //true表示getObject()返回的是单实例
    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        /**getRegistry()从注册中心获取 服务内容*/
        this.registryInfo = BaseRegistryDelegate.getRegistry(this.id, this.applicationContext);/*id继承自父类*/
        System.out.println(this.registryInfo);
        RedisApi.subsribe("channel" + id,new RedisServerRegistry());
    }
}
