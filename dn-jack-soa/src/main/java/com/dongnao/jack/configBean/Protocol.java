package com.dongnao.jack.configBean;

import com.dongnao.jack.netty.NettyUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import java.io.Serializable;

public class Protocol extends BaseConfigBean implements Serializable,InitializingBean,
        ApplicationContextAware, ApplicationListener<ContextRefreshedEvent> {
    private static final long serialVersionUID = 7413844440690975357L;
    private String name;
    private String port;
    private String host;
    private String contextpath;
    private static ApplicationContext applicationContext;
    public Protocol() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getName() {
        return name;
    }

    public void setContextpath(String contextPath) {
        this.contextpath = contextPath;
    }

    public String getPort() {
        return port;
    }

    public String getHost() {
        return host;
    }

    public String getContextpath() {
        return contextpath;
    }

    @Override
    public String toString() {
        return "Protocol{" +
                "name='" + name + '\'' +
                ", port='" + port + '\'' +
                ", host='" + host + '\'' +
                ", contextPath='" + contextpath + '\'' +
                '}';
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Protocol.applicationContext = applicationContext;
    }
    /**实现ApplicationListener<ContextRefreshedEvent>接口，覆写onApplicationEvent()方法，当
     * spring容器启动成功时，调用该方法，去启动 netty*/
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (!ContextRefreshedEvent.class.getName().equals(contextRefreshedEvent.getClass()
                .getName())) {
            return;
        }

        if (!"netty".equalsIgnoreCase(name)) {
            return;
        }
        new Thread(new Runnable() {
            public void run() {
                try {
                    NettyUtil.startServer(port);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }).start();
    }
}
