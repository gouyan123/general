package com.gupaoedu.vip.spring.framework.aop;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

//只是对application中的expression的封装
//目标代理对象的一个方法要增强
//由用自己实现的业务逻辑去增强
//配置文件的目的：告诉Spring，哪些类的哪些方法需要增强，增强的内容是什么
/**封装配置文件中的内容 切入点方法对象 切面类方法对象*/
public class GPAopConfig {
    //以目标对象需要增强的Method作为key，需要增强的代码内容作为value
    /*key:目标方法即切入点方法对象,value:GPAspect对象(封装切面类对象和切面类中切面方法对象)*/
    private Map<Method,GPAspect> points = new HashMap<Method, GPAspect>();
    /*target：目标方法对象及切入点方法对象，aspect：切面类对象，aspectMethods：切面类中切面方法对象数组*/
    public void put(Method target,Object aspect,Method[] aspectMethods){
        this.points.put(target,new GPAspect(aspect,aspectMethods));
    }
    /*根据目标方法即需要增强的方法即切入点方法，获得它的切面类及切面类中的切面方法*/
    public GPAspect get(Method method){
        return this.points.get(method);
    }
    /*method：目标方法即切入点方法*/
    public boolean contains(Method method){
        return this.points.containsKey(method);
    }

    //对增强的代码的封装
    /*封装切面类对象和切面类中的方法对象*/
    public class GPAspect{
        /*切面类对象*/
        private Object aspect; //待会将LogAspet这个对象赋值给它
        /*切面类中的切面方法对象*/
        private Method[] aspectMethods;//会将LogAspet的before方法和after方法赋值进来

        public GPAspect(Object aspect,Method[] aspectMethods){
            this.aspect = aspect;
            this.aspectMethods = aspectMethods;
        }

        public Object getAspect() {
            return aspect;
        }

        public Method[] getAspectMethods() {
            return aspectMethods;
        }
    }

}
