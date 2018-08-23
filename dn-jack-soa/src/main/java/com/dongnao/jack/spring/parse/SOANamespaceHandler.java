package com.dongnao.jack.spring.parse;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
import com.dongnao.jack.configBean.Protocol;
import com.dongnao.jack.configBean.Reference;
import com.dongnao.jack.configBean.Registry;
import com.dongnao.jack.configBean.Service;
public class SOANamespaceHandler extends NamespaceHandlerSupport {
    public void init() {
        /**registry为xsd元素名称，通过解析，将soa.xsd中registry元素内容封装到Registry*/
        registerBeanDefinitionParser("registry",
                /**定义parser类 *BeanDefinitionParse(*.class)*/
                new RegistryBeanDefinitionParse(Registry.class));
        registerBeanDefinitionParser("protocol",
                new ProtocolBeanDefinitionParse(Protocol.class));
        registerBeanDefinitionParser("reference",
                new ReferenceBeanDifinitionParse(Reference.class));
        registerBeanDefinitionParser("service", new ServiceBeanDefinitionParse(
                Service.class));
    }
}
