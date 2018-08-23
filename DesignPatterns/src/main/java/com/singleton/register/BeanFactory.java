package com.singleton.register;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
//Spring中的做法，就是用这种注册式单例
public class BeanFactory {
    private BeanFactory(){}
    //ConcurrentHashMap 线程安全
    private static Map<String,Object> ioc = new ConcurrentHashMap<String,Object>();
    public static Object getBean(String beanName){
        if(!ioc.containsKey(beanName)){
            Object obj = null;
            try {
                obj = Class.forName(beanName).newInstance();
                ioc.put(beanName,obj);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return obj;
        }else{
            return ioc.get(beanName);
        }
    }
}
