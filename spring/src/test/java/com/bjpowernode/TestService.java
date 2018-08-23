package com.bjpowernode;


import com.bjpowernode.ioc.ISomeService;
import com.bjpowernode.ioc.ServiceFactory;
import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class TestService {
    @Test
    public void test01(){
        /*对象存在容器里，去容器里面取对象，所以先创建容器ApplicationContext
         * ApplicationContext最终实现BeanFactory接口
         * 接口只是在执行线程中调用方法，真正实现方法的是类，统一传递接口参数并用接口参数调用方法可以降低耦合度
         * */
        ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
        ISomeService someService = (ISomeService) ac.getBean("someService");
        someService.doSome();
    }
    @Test
    public void test02(){
        /*BeanFactory是接口，查其继承关系，中间接口，类等都只会调用，不干活的，直接看最底层实现类
         *XmlBeanFactory为最底层实现类，创建对象，依赖构造方法，该类构造方法需要Resource参数
         * ctr + 单击 查看Resouce源码，Resouce是一个接口，根据继承关系，直接找到它的最底层实现类
         * */
        Resource resource = new ClassPathResource("applicationContext.xml");
        BeanFactory bf = new XmlBeanFactory(resource);
        ISomeService someService = (ISomeService) bf.getBean("someService");
        System.out.println("test02");
        someService.doSome();
    }
    /*动态工厂创建Bean*/
    @Test
    public void test03(){

        ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
        ISomeService someService = (ISomeService) ac.getBean("someService");
        System.out.println("test03");
        someService.doSome();
    }

    @Test
    public void test04() {
        try {
            Class clz = Class.forName("com.bjpowernode.ioc.ServiceFactory");
            Method method = clz.getDeclaredMethod("getSomeService",null);
            Constructor constructor = clz.getDeclaredConstructor();
            ServiceFactory factory = (ServiceFactory) constructor.newInstance();
            ISomeService someService = (ISomeService) method.invoke(factory,null);
            System.out.println("test04");
            someService.doSome();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*2.2.6 定制Bean的生命始末*/
    @Test
    public void test05(){

        ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
        ISomeService someService = (ISomeService) ac.getBean("someService");
        someService.doSome();
        //执行销毁方法满足 2 个条件：①Bean是单例singleton；②手工关闭容器；
        ((ClassPathXmlApplicationContext)ac).close();
    }
    /*2.2.7 Bean的生命周期，共 11 步*/
    @Test
    public void test06(){
        ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
        ISomeService someService = (ISomeService) ac.getBean("someService");
        someService.doSome();
        //执行销毁方法满足 2 个条件：①Bean是单例singleton；②手工关闭容器；
        ((ClassPathXmlApplicationContext)ac).close();
    }
}
