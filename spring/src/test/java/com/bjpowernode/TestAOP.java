package com.bjpowernode;

import com.bjpowernode.aop.introduction.ISomeService;
import com.bjpowernode.aop.introduction.SomeServiceImpl;
import com.bjpowernode.aop.introduction.SystemService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@RunWith(SpringJUnit4ClassRunner.class)
/*读取配置文件，并将配置文件内bean加载到spring容器中，实际执行 ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext-aop.xml")*/
@ContextConfiguration(value = "classpath:applicationContext-aop.xml")
public class TestAOP {
//    @Autowired
//    @Qualifier(value = "serviceProxy")/*@Autowired与@Qualifier连用，把容器中的serviceProxy注入该类*/
//    private ISomeService serviceProxy;     /*注入即给类成员变量赋值，实现执行 ac.getBean("serviceProxy")*/

    @Autowired
    @Qualifier(value = "someService")
    private ISomeService someService;

    @Test
    public void test01(){
        ISomeService target = new SomeServiceImpl();
        ISomeService proxyInstance = (ISomeService) Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        SystemService.doTx();/*目标方法增强*/
                        Object result = method.invoke(target,args);/*执行目标类方法*/
                        SystemService.doLog();/*目标方法增强*/
                        return result;
                    }
                }
        );
    }
    /*@Test
    public void test02(){
        this.serviceProxy.doFirst();
        System.out.println("===================");
        String result = this.serviceProxy.doSecond();
        System.out.println(result);
    }*/

    /*@Test
    public void test03() throws UserException {
        *//*throws UserException：声明方法可能会发生异常，当发生异常时，将异常抛给调用处处理，若不能处理，程序中断；
         *try{....}catch{....}：try自己捕获异常，并放到catch中，只要catch不抛（throw e），程序不会中断；
        * *//*
        boolean flag = this.serviceProxy.login("bei","bei");
    }*/

   /* @Test
    public void test04(){
        this.serviceProxy.doFirst();
        System.out.println("===================");
        String result = this.serviceProxy.doSecond();
        System.out.println(result);
        System.out.println("===================");
        this.serviceProxy.doThird();
    }*/
    @Test
    public void test05(){
        this.someService.doFirst();
        System.out.println("===================");
        String result = this.someService.doSecond();
        System.out.println(result);
        System.out.println("===================");
        this.someService.doThird();
    }
}
