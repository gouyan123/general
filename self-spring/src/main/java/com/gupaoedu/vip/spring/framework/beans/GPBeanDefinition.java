package com.gupaoedu.vip.spring.framework.beans;

/*用来存储xml配置文件的信息，即将xml中的配置内容加载到内存中，封装到BeanDedinition类中*/
public class GPBeanDefinition {
    /*beanClassName：类全名，即 包名.类名*/
    private String beanClassName;
    private boolean lazyInit = false;
    /*factoryBeanName：bean在IOC容器中的名字，例 <bean id="factoryBeanName" class="类全名即包.类名"*/
    /*<bean id="factoryBeanName" class="className">*/
    private String factoryBeanName;

    public String getBeanClassName() {
        return beanClassName;
    }

    public void setBeanClassName(String beanClassName) {
        this.beanClassName = beanClassName;
    }

    public boolean isLazyInit() {
        return lazyInit;
    }

    public void setLazyInit(boolean lazyInit) {
        this.lazyInit = lazyInit;
    }

    public String getFactoryBeanName() {
        return factoryBeanName;
    }

    public void setFactoryBeanName(String factoryBeanName) {
        this.factoryBeanName = factoryBeanName;
    }
}
