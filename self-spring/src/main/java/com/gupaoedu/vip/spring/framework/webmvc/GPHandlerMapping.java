package com.gupaoedu.vip.spring.framework.webmvc;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

/**封装ioc容器中 controller对象(使用@Controller注解)，method对象(使用@RequestMappting注解)，url*/
public class GPHandlerMapping {
    /*Controller类对象，这里用于反射*/
    private Object controller;
    /*Controller类中方法对象*/
    private Method method;
    /*Controller类中方法绑定的路径*/
    private Pattern pattern;  //url的封装

    public GPHandlerMapping(Pattern pattern,Object controller, Method method) {
        this.controller = controller;
        this.method = method;
        this.pattern = pattern;
    }

    public Object getController() {
        return controller;
    }

    public void setController(Object controller) {
        this.controller = controller;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }
}
