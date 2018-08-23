package com.gupaoedu.vip.spring.framework.core;

public interface GPBeanFactory {
    /**
     * 根据beanName从IOC容器之中获得一个实例Bean
     * @param beanName
     * @return
     */
    Object getBean(String beanName);
}
