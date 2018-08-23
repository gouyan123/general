package com.gupaoedu.vip.spring.framework.context;

import com.gupaoedu.vip.spring.demo.action.MyAction;
import com.gupaoedu.vip.spring.demo.service.IQueryService;
import com.gupaoedu.vip.spring.framework.annotation.GPAutowired;
import com.gupaoedu.vip.spring.framework.annotation.GPController;
import com.gupaoedu.vip.spring.framework.annotation.GPService;
import com.gupaoedu.vip.spring.framework.aop.GPAopConfig;
import com.gupaoedu.vip.spring.framework.beans.GPBeanDefinition;
import com.gupaoedu.vip.spring.framework.beans.GPBeanPostProcessor;
import com.gupaoedu.vip.spring.framework.beans.GPBeanWrapper;
import com.gupaoedu.vip.spring.framework.context.support.GPBeanDefinitionReader;
import com.gupaoedu.vip.spring.framework.core.GPBeanFactory;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*IOC容器类*/
public class GPApplicationContext extends GPDefaultListableBeanFactory implements GPBeanFactory {
    private String [] contextConfigLocations;
    private GPBeanDefinitionReader beanDefinitionReader;
    //beanDefinitionMap用来保存配置信息 (key:beanName,value:beanDefinition)
    /*该成员变量已定义在父类 GPDefaultListableBeanFactory中*/
    //private Map<String,GPBeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String,GPBeanDefinition>();
    //注册式单例的容器：直接来 beanCacheMap 中拿对象，有则取走，没有则创建对象并保存在里面
    /*key:className,value:instance*/
    private Map<String,Object> beanCacheMap = new HashMap<String, Object>();
    //用来存储所有的被代理过的对象 (key:beanName,value:beanWrapper)
    private Map<String,GPBeanWrapper> beanWrapperMap = new ConcurrentHashMap<String, GPBeanWrapper>();

    public GPApplicationContext(String... contextConfigLocations) {
        this.contextConfigLocations = contextConfigLocations;
        /**控制反转 IOC 开始于 refresh()方法*/
        refresh();
    }

    public void refresh(){
        /**①定位：定位到配置文件，把配置文件的内容封装到 GPBeanDefinitionReader类的 properties成员变量中*/
        this.beanDefinitionReader = new GPBeanDefinitionReader(this.contextConfigLocations);
        /**②加载：获取指定包下的所有类的 类全名*/
        List<String> classNames = this.beanDefinitionReader.loadBeanDefinitions();
        /**③注册：将 beanName，GPBeanDefinition封装到 beanDefinitionMap*/
        /*<bean id="factoryBeanName" class="className">，BeanDifinition封装的是factoryBeanName className*/
        this.doRegisty(classNames);
        /**至此，容器初始化完毕*/
        /**④依赖注入（lazy-init = false），要是执行依赖注入，在这里自动调用getBean方法*/

        this.doAutowrited();
        //MyAction myAction = (MyAction)this.getBean("myAction");
//        myAction.query(null,null,"任性的Tom老师");
    }

    /*③注册*/
    /*BeanDefinitions：类全名list，注册到beanDefinitionMap（key：id，value：BeanDifinition）中*/
    /**将 beanName，className(类全名)封装到 GPBeanDefinition中，再将 beanName，GPBeanDefinition封装到 beanDefinitionMap 中*/
    private void doRegisty(List<String> classNames) {
        //beanName有三种情况:1、默认是类名首字母小写；2、自定义名字；3、接口注入
        try {
            for (String className : classNames) {
                Class<?> beanClass = Class.forName(className);
                /*如果是一个接口，是不能实例化的，用它实现类来实例化*/
                if(beanClass.isInterface()){ continue; }
                /**将 beanName，className 封装到 GPBeanDefinition中*/
                GPBeanDefinition beanDefinition = this.beanDefinitionReader.registerBean(className);
                if(beanDefinition != null){
                    /*<bean id="factoryBeanName" class="className">*/
                    /**beanDefinitionMap 封装 beanName，GPBeanDefinition*/
                    this.beanDefinitionMap.put(beanDefinition.getFactoryBeanName(),beanDefinition);
                }
                /**如果是一个接口，是不能实例化的，用它实现类来实例化*/
                Class<?>[] interfaces = beanClass.getInterfaces();
                for (Class<?> i: interfaces) {
                    //如果是多个实现类，只能覆盖
                    //为什么？因为Spring没那么智能，就是这么傻
                    //这个时候，可以自定义名字
                    this.beanDefinitionMap.put(i.getName(),beanDefinition);
                }
                //到这里为止，容器初始化完毕
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /*④依赖注入：依赖注入从 getBean()方法开始*/
    /*读取 GPBeanDefinition（封装 <bean id="factoryBeanName" class="className">） 中封装的内容，
     * 利用反射创建所需的实例，并返回，但是并不是返回最原始的对象，而是使用 GPBeanWrapper 封装后再返回*/
    /*装饰器模式：1、保留原来的OOP关系；2、我需要对它进行扩展，增强（为了以后AOP打基础）；*/
    /**依赖注入 DI 开始于 getBean(beannnName)*/
    /**将 beanName，beanWrapper存到 beanWrapperMap中，并返回 beanWrapper*/
    @Override
    public Object getBean(String beanName) {
        /**GPBeanDefinition封装配置文件中 beanName className*/
        GPBeanDefinition beanDefinition = super.beanDefinitionMap.get(beanName);
        String className = beanDefinition.getBeanClassName();
        try{
            /*事件监听器对象*/
            GPBeanPostProcessor beanPostProcessor = new GPBeanPostProcessor();
            /*由beanDefinition获得未被代理的原始对象instance*/
            Object instance = this.instantionBean(beanDefinition);
            if(null == instance){ return  null;}
            /*被监听类GPApplicationContext的被监听方法，调用监听器的监听方法，监听方法有 2 种：
            * ①只有 1 个监听方法 handleEvent(Event event) ，监听方法会根据事件参数做相应处理
            * ②有多个监听方法，想做什么处理，就调哪个监听方法*/
            beanPostProcessor.postProcessBeforeInitialization(instance,beanName);
            GPBeanWrapper beanWrapper = new GPBeanWrapper(instance);
            /**this.instantionAopConfig(beanDefinition) 将 AOP 配置文件中的内容封装到
             * GPAopConfig 类中，并返回 GPAopConfig 对象*/
            beanWrapper.setAopConfig(this.instantionAopConfig(beanDefinition));

            beanWrapper.setPostProcessor(beanPostProcessor);
            this.beanWrapperMap.put(beanName,beanWrapper);
            /*在实例初始化以后调用监听方法postProcessAfterInitialization()，做相应操作*/
            beanPostProcessor.postProcessAfterInitialization(instance,beanName);
//            populateBean(beanName,instance);/*存在问题：接口实现类没有实例化，使用递归解决*/
            //通过这样一调用，相当于给我们自己留有了可操作的空间
            return this.beanWrapperMap.get(beanName).getWrapperInstance();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    /**取出BeanDefinition里面封装的className，反射创建一个实例对象*/
    private Object instantionBean(GPBeanDefinition beanDefinition){
        Object instance = null;
        String className = beanDefinition.getBeanClassName();
        try{
            /*缓存：缓存就是一个集合，直接去缓存中取，有则取出，没有则创建，并保存到集合里面*/
            if(this.beanCacheMap.containsKey(className)){
                instance = this.beanCacheMap.get(className);
            }else{
                Class<?> clazz = Class.forName(className);
                instance = clazz.newInstance();
                this.beanCacheMap.put(className,instance);
            }
            return instance;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    /*④依赖注入：开始执行自动化的依赖注入*/
    private void doAutowrited() {
        for(Map.Entry<String,GPBeanDefinition> beanDefinitionEntry : super.beanDefinitionMap.entrySet()){
            String beanName = beanDefinitionEntry.getKey();
            /*非延时加载情况下*/
            if(!beanDefinitionEntry.getValue().isLazyInit()){
                /**调用 getBean()时，已经把对应 beanName，beanWrapper封装到 beanWrapperMap里面了*/
                Object obj = this.getBean(beanName);
            }
        }
        /*所有类都实例化以后，再去注入，此处只是为了让代码正常运行，实际中不是都实例化后再注入*/
        /**beanWrapperMap 封装 beanName，原始对象的包装对象*/
        for(Map.Entry<String,GPBeanWrapper> beanWrapperEntry : this.beanWrapperMap.entrySet()){
            this.populateBean(beanWrapperEntry.getKey(),beanWrapperEntry.getValue().getOriginalInstance());
        }
    }
    /**依赖注入方法 populateBean()：将instance对象中使用@Autowired注解的成员变量进行赋值*/
    public void populateBean(String beanName,Object instance){
        Class clazz = instance.getClass();
        /*只要加了@GPController注解和@GPService注解的类*/
        if(!(clazz.isAnnotationPresent(GPController.class) ||
                clazz.isAnnotationPresent(GPService.class))){
            return;
        }
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(GPAutowired.class)){ continue; }
            GPAutowired autowired = field.getAnnotation(GPAutowired.class);
            String autowiredBeanName = autowired.value().trim();
            if("".equals(autowiredBeanName)){
                autowiredBeanName = field.getType().getName();
            }
            field.setAccessible(true);
            try {
                //System.out.println("=======================" +instance +"," + autowiredBeanName + "," + this.beanWrapperMap.get(autowiredBeanName));

                field.set(instance,this.beanWrapperMap.get(autowiredBeanName).getWrapperInstance());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
    /*获取 beanName 数组*/
    public String[] getBeanDefinitionNames() {
        return this.beanDefinitionMap.keySet().toArray(new String[this.beanDefinitionMap.size()]);
    }
    /*根据路径，定位到 application.propertis 并将里面内容加载到 Properies 对象中*/
    public Properties getConfig(){
        return this.beanDefinitionReader.getConfig();
    }

    /**将 AOP 配置文件中的内容封装到 GPAopConfig 类中*/
    private GPAopConfig instantionAopConfig(GPBeanDefinition beanDefinition) throws  Exception{
        GPAopConfig aopConfig = new GPAopConfig();
        /*从application.properties配置文件中获取 expression表达式内容*/
        String expression = this.beanDefinitionReader.getConfig().getProperty("pointCut");
        /*方法名称的字符串数组*/
        String[] before = this.beanDefinitionReader.getConfig().getProperty("aspectBefore").split("\\s");
        /*方法名称的字符串数组*/
        String[] after = this.beanDefinitionReader.getConfig().getProperty("aspectAfter").split("\\s");
        String className = beanDefinition.getBeanClassName();
        Class<?> clazz = Class.forName(className);
        Pattern pattern = Pattern.compile(expression);
        /*配置文件中：类全名 空格 方法名*/
        Class aspectClass = Class.forName(before[0]);
        //在这里得到的方法都是原生的方法
        for (Method m : clazz.getMethods()){
            /* ..*Service表示任意以Service结尾的类 第1个点表示分隔符 第2个点表示任意字符；*/
            /* public .* com\.gupaoedu\.vip\.spring\.demo\.service\..*Service\..*\(.*\)  */
            /* m.toString() : public java.lang.String com.gupaoedu.vip.spring.demo.service.impl.ModifyService.add(java.lang.String,java.lang.String)*/
            Matcher matcher = pattern.matcher(m.toString());
            /*能匹配上，即配置文件中给某个方法（切入点方法 需要增强的方法）匹配了一个切面类里的切面方法*/
            if(matcher.matches()){
                //能满足切面规则的类，添加的AOP配置中
                //m:目标方法对象,aspectClass.newInstance():目标类对象,new Method[]:切面类中的切面方法
                aopConfig.put(m,aspectClass.newInstance(),new Method[]{aspectClass.getMethod(before[1]),aspectClass.getMethod(after[1])});
            }
        }
        return  aopConfig;
    }
    public static void main(String[] args) {
        GPApplicationContext context = new GPApplicationContext("classpath:application.properties");
        /*springMVC测试*/
        /*MyAction myAction = (MyAction) context.getBean("myAction");*/
        /*springAOP测试*/
        IQueryService queryService = (IQueryService) context.getBean("queryService");
        System.out.println(queryService.query("James"));
    }
}
