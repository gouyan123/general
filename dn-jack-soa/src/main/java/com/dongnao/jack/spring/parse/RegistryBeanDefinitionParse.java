package com.dongnao.jack.spring.parse;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**Registry标签的解析类*/
public class RegistryBeanDefinitionParse implements BeanDefinitionParser {
    private Class<?> beanClass;
    public RegistryBeanDefinitionParse() {
    }
    /**注册beanClass*/
    public RegistryBeanDefinitionParse(Class<?> beanClass) {
        this.beanClass = beanClass;
    }
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        RootBeanDefinition beanDefinition = new RootBeanDefinition();
        /**反射类对象交给beanDefinition，beanClass会被spring实例化*/
        beanDefinition.setBeanClass(beanClass);
        beanDefinition.setLazyInit(false);
        //从soa.xsd文件中读取到内存中
        String protocol = element.getAttribute("protocol");
        String address = element.getAttribute("address");
        if (protocol == null || "".equals(protocol)) {
            throw new RuntimeException("Regitry protocol 不能为空！");
        }
        if (address == null || "".equals(address)) {
            throw new RuntimeException("Regitry address 不能为空！");
        }
        //从内存中保存到类中
        beanDefinition.getPropertyValues().addPropertyValue("protocol",
                protocol);/*调用beanDefinition里面反射类进行属性赋值*/
        beanDefinition.getPropertyValues().addPropertyValue("address", address);
        parserContext.getRegistry()
                .registerBeanDefinition("Registry" + address, beanDefinition);
        return beanDefinition;
    }
}
