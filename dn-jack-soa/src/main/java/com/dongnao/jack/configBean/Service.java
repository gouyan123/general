package com.dongnao.jack.configBean;

import com.dongnao.jack.redis.RedisApi;
import com.dongnao.jack.registry.BaseRegistryDelegate;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.io.Serializable;
import java.security.SecureRandom;

public class Service extends BaseConfigBean implements InitializingBean,ApplicationContextAware{
    private static final long serialVersionUID = -958612407085199402L;
    private String intf;
    private String ref;
    private String protocol;
    private static ApplicationContext applicationContext;
    public Service() {
    }
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }
    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        Service.applicationContext = applicationContext;
    }


    public void setIntf(String intf) {
        this.intf = intf;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getIntf() {
        return intf;
    }

    public String getRef() {
        return ref;
    }

    public String getProtocol() {
        return protocol;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        /**创建 BaseRegistryDelegate类，将服务内容注册到注册中心*/
        BaseRegistryDelegate.registry(this.ref,Service.applicationContext);
        RedisApi.publish("channel" + ref,"里面内容跟redis节点内容一致");
    }
}
