package com.bjpowernode.ioc;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;

public class SomeServiceImpl implements ISomeService,BeanNameAware,BeanFactoryAware,InitializingBean,DisposableBean {
    private String adao;
    private int a = 5;
    {
        System.out.println("动态代码块 a = " + a);
        /*输出结果：先输出动态代码块 a = 5，说明调用构造方法之前，该类就已经分配堆内存空间
         *没有成员变量的类在堆内存中占8个字节，一个成员变量占4个字节
        * */
    }
    /*因为创建bean默认调用无参构造，所以要注入成员变量*/
    public SomeServiceImpl(){
        System.out.println("step1：执行无参构造方法 a = " + a);
    }

    public void setAdao(String adao) {
        this.adao = adao;
        System.out.println("step2：执行setter方法");
    }

    @Override
    public void doSome() {
        System.out.println("step9：执行doSome方法");
    }

    public void setUp(){
        System.out.println("step7：bean初始化后执行，xml中配置init-method=setUp");
    }
    public void tearDown(){
        System.out.println("step11：Bean销毁前执行，xml中配置destory-method=setUp");
    }

    @Override
    public void setBeanName(String s) {
        System.out.println("step3：获取到bean的 id = " + s);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        System.out.println("step4：获取到BeanFactory容器");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("step6：bean已经初始化（初始化指成员变量初始化）完毕");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("step10：实现接口销毁的实现之前");
    }
}
