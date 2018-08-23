package com.gouyan;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
public class MyNamespaceHandler extends NamespaceHandlerSupport {
    @Override
    public void init() {
        /**将gouyan.xsd中<xsd:element name="user">元素对象的解析交给UserBeanDefinitionParser类
         * 对象*/
        registerBeanDefinitionParser("user", new UserBeanDefinitionParser(User.class));
    }
}