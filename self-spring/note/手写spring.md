```text
学习框架过程：
①理清主线，画类图和方法调用时序图;
②根据时序图，搭建出框架（只有方法之间调用关系，方法实现为空 { }）;
③具体实现各个方法
```

> 手写核心代码
>> 名词解释
>>* className：类全名，例如 com.gupaoedu.Person;
>>* beanName：<bean id="person" class="com.gupaoedu.Person"/>，beanName就是其中的id

> spring容器初始化
```java
/**
* spring开始的类 ClassPathXmlApplicationContext
* 创建 com.gupaoedu.vip.spring.framework.webmvc.servlet.GPDispatcherServlet.java 作为MVC启动入口；
* context包下创建 com.gupaoedu.vip.spring.framework.context.GPApplicationContext.java 类，作为
* 容器，该类顶层接口为 BeanFactory，BeanFactory定义在 core 包中*/
public interface BeanFactory {
    /*根据beanName从IOC容器之中获得一个实例Bean*/
    Object getBean(String beanName);
}
// GPApplicationContext 类实现 BeanFacory 接口，并在类中创建 refresh()方法：
public class GPApplicationContext implements BeanFactory{
    public GPApplicationContext(String... contextConfigLocations) {
        this.contextConfigLocations = contextConfigLocations;
        /**控制反转 IOC 开始于 refresh()方法*/
        refresh();
    }
    public void refresh(){
        /**①定位：定位到配置文件，把配置文件的内容封装到 GPBeanDefinitionReader类的 properties成员变量中*/
        /**②加载：获取指定包下的所有类的 类全名*/
        /**③注册：将 beanName，GPBeanDefinition封装到 beanDefinitionMap*/
        /**④依赖注入（lazy-init = false），要是执行依赖注入，在这里自动调用getBean方法*/
    }
    @Override
    public Object getBean(String beanName) {
        return null;
    }
}

// Servlet要初始化容器即初始化 GPApplicationContext，转到 DispatcherServlet 类的 init()方法：
public class DispatcherServlet extends HttpServlet {
    @Override
    public void init(ServletConfig config) throws ServletException {
        /*初始化IOC容器*/
        /*此处需要IOC容器 ApplicationContext的扫描路径，因此通过构造方法将扫描路径传递过去*/
        /*config对象是对 web.xml 配置文件信息的封装，从里面获得该servlet的parameter的value*/
        GPApplicationContext context = new GPApplicationContext(config.getInitParameter("contextConfigLocation"));
    }
}
// IOC容器 GPApplicationContext中创建构造方法，接收 xml 文件扫描路径，并在构造方法中调用 refresh()方法
public class GPApplicationContext implements BeanFactory{
    private String [] contextConfigLocation;
    public GPApplicationContext(String... contextConfigLocation) {
        this.contextConfigLocation = contextConfigLocation;
        refresh();
    }
}

/**refresh()方法还没开始定义，需要如下定义:
context包下创建support包，并创建 com.gupaoedu.vip.spring.framework.context.support.BeanDefinitionReader类
BeanDefinitionReader类中定义registerBean(String className) 方法，需要在beans包中先定义
BeanDefiniton 类（用来存储配置信息，即将配置信息封装到 BeanDefinition中）和BeanWrapper 类*/
public class BeanDefinition {
    /*beanClassName：类全名，即 包名.类名*/
    private String beanClassName;
    private boolean lazyInit = false;
    /*factoryBeanName：bean在IOC容器中的名字，例 <bean id="factoryBeanName" class="类全名即包.类名"*/
    /*<bean id="factoryBeanName" class="className">*/
    private String factoryBeanName;
    /*setter，getter方法省略*/
}

// BeanDefinitionReader类作用：对配置文件进行查找，读取、解析
public class BeanDefinitionReader {
    //每注册一个className，就返回一个BeanDefinition，我自己包装
    public BeanDefinition registerBean(String className){
        return null;
    }
}

// 回到IOC容器类 GPApplicationContext的 refresh() 方法：
public class GPApplicationContext extends GPDefaultListableBeanFactory implements GPBeanFactory{
    public void refresh(){
        //①定位：定位到配置文件，把配置文件的内容封装到java内存对象中
        this.beanDefinitionReader = new BeanDefinitionReader(this.contextConfigLocations);
        //②加载：加载到指定包下的所有类的 类全名
        List<String> beanDefinitions = this.beanDefinitionReader.loadBeanDefinitions();
        //③注册：注册到beanDefinitionMap（key：id，value：BeanDifinition）
        /*<bean id="factoryBeanName" class="className">，BeanDifinition封装的是factoryBeanName className*/
        doRegisty(beanDefinitions);
        /*至此，容器初始化完毕*/
        //④依赖注入（lazy-init = false），要是执行依赖注入
        //在这里自动调用getBean方法
        //doAutowrited();
    //        MyAction myAction = (MyAction)this.getBean("myAction");
    //        myAction.query(null,null,"任性的Tom老师");
    }
    //①定位操作
    BeanDefinitionReader 读取类 创建构造方法，接收要读取的文件的路径，定位到文件，然后将其内容加载到内存对象
    public BeanDefinitionReader(String... contextConfigLocations) {
        //...
    }
    //②加载操作
    BeanDefinitionReader 读取类 创建 loadBeanDefinitions() 方法，获取 类全名 的list
    public List<String> loadBeanDefinitions(){
        return this.registyBeanClasses;
    }
    //③注册操作
    /**BeanDefinitions：类全名list，注册到beanDefinitionMap（key：id，value：BeanDifinition）中
    GPApplicationContext IOC容器类，创建 doRegisty(List<String> beanDefinitions) 注册方法，将beanName，
    BeanDefinition注册到 map 中*/
    private void doRegisty(List<String> beanDefinitions) {
        //beanName有三种情况:1、默认是类名首字母小写；2、自定义名字；3、接口注入
        try {
            for (String className : beanDefinitions) {
                Class<?> beanClass = Class.forName(className);
                /*如果是一个接口，是不能实例化的，用它实现类来实例化*/
                if(beanClass.isInterface()){ continue; }
                BeanDefinition beanDefinition = this.beanDefinitionReader.registerBean(className);
                if(beanDefinition != null){
                    /*<bean id="factoryBeanName" class="className">*/
                    this.beanDefinitionMap.put(beanDefinition.getFactoryBeanName(),beanDefinition);
                }
                /*如果是一个接口，是不能实例化的，用它实现类来实例化*/
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
}
```
> 至此，IOC容器初始化完毕，即new GPApplicationContext()，并调用refresh()方法，将beanName作为key，beanClass作为value封装到beanDefinitionMap中；


控制反转，依赖注入通过 GPApplicationContext IOC容器类的 getBean()开始的，getBean(beanName)读取
BeanDefinition（封装 <bean id="factoryBeanName" class="className">）中封装的内容，利用反射创建
所需的实例，并返回，但是并不是返回最原始的对象，而是使用 BeanWrapper 封装后再返回；

装饰器模式：1、保留原来的OOP关系；2、我需要对它进行扩展，增强（为了以后AOP打基础）；

回到 GPApplicationContext IOC容器类的 getBean(String beanName)方法，getBean()方法依赖instantionBean()
方法，传一个BeanDefinition，就返回一个单例实例Bean，单例实例Bean通过缓存集合
beanCacheMap(key:类全名,value:实例对象) 实现；
```java
public class GPApplicationContext extends GPDefaultListableBeanFactory implements GPBeanFactory{
    private Object instantionBean(BeanDefinition beanDefinition){
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
}
```
```java
// 回到 BeanWrapper 类
public class BeanWrapper {
    /*原始的实例化对象，即直接由 类全名 反射出来的*/
    private Object originalInstance;
    /*包装后的实例化对象*/
    private Object wrapperInstance;

    public BeanWrapper(Object originalInstance, Object wrapperInstance) {
        this.originalInstance = originalInstance;
        this.wrapperInstance = wrapperInstance;
    }
    /*返回代理以后的实例对象的Class，可能会是这个 $Proxy0*/
    public Class<?> getWrappedClass(){
        return this.wrapperInstance.getClass();
    }
}
```
```java
// 因为spring要使用 AOP 因此创建 *.beans.BeanPostProcessor 监听类
/*用作事件监听的*/
public class BeanPostProcessor {
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        return bean;
    }
    public Object postProcessAfterInitialization(Object bean, String beanName){
        return bean;
    }
}

/**用 BeanPostProcessor 类对象作为 BeanWrapper 类的成员变量；
创建 *.core.FactoryBean 类，BeanWrapper 继承 FactoryBean，保证同宗同源*/
public class BeanWrapper extends FactoryBean{
    //...
}
```
回到 GPApplicationContext IOC容器类的 getBean()方法：
/*④依赖注入：依赖注入从 getBean()方法开始*/
/*读取 BeanDefinition（封装配置文件中的 <bean id="factoryBeanName" class="className">）中的内容，
 * 利用反射创建所需的实例，并返回，但是并不是返回最原始的对象，而是使用 BeanWrapper 封装后再返回*/
/*装饰器模式：1、保留原来的OOP关系；2、我需要对它进行扩展，增强（为了以后AOP打基础）；*/
```java
public class GPApplicationContext extends GPDefaultListableBeanFactory implements GPBeanFactory{
    @Override
    public Object getBean(String beanName) {
        BeanDefinition  beanDefinition = this.beanDefinitionMap.get(beanName);
        String className = beanDefinition.getBeanClassName();
        try{
            /*事件监听器对象*/
            BeanPostProcessor beanPostProcessor = new BeanPostProcessor();
            /*由beanDefinition获得instance*/
            Object instance = this.instantionBean(beanDefinition);
            if(null == instance){ return  null;}
            /*被监听类GPApplicationContext的被监听方法，调用监听器的监听方法，监听方法有 2 种：
            * ①只有 1 个监听方法 handleEvent(Event event) ，监听方法会根据事件参数做相应处理
            * ②有多个监听方法，想做什么处理，就调哪个监听方法*/
            beanPostProcessor.postProcessBeforeInitialization(instance,beanName);
            BeanWrapper beanWrapper = new BeanWrapper(instance);
            beanWrapper.setPostProcessor(beanPostProcessor);
            this.beanWrapperMap.put(beanName,beanWrapper);
            /*在实例初始化以后调用监听方法postProcessAfterInitialization()，做相应操作*/
            beanPostProcessor.postProcessAfterInitialization(instance,beanName);
    //            populateBean(beanName,instance);
            //通过这样一调用，相当于给我们自己留有了可操作的空间
            return this.beanWrapperMap.get(beanName).getWrapperInstance();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
```
④依赖注入方法 doAutowrited();该方法里面再调用 populateBean()方法，该方法给 bean 属性赋值；
****************************************************************************************
依赖注入实质：spring容器初始化产生的对象都是无参构造器创建的，属性都是初始值，还没有赋值，
依赖注入就是给对象成员变量赋值
****************************************************************************************
测试：测试入口 DispatchServlet，启动服务器，服务器创建DispatcherServlet对象，并调用 init()方法，
init()方法中启动 IOC容器 GPApplicationContext，至此，自己写的 spring 框架启动了；然后，
在 com.gupaoedu.vip.spring.demo 包中，使用自己定义的spring框架，创建spring项目；
public class DispatcherServlet extends HttpServlet {
    @Override
    public void init(ServletConfig config) throws ServletException {
        /*相当于把IOC容器初始化了*/
        /*此处需要IOC容器 ApplicationContext的扫描路径，因此通过构造方法将扫描路径传递过去*/
        /*config对象是对 web.xml 配置文件的封装，从里面获得该servlet的parameter*/
        GPApplicationContext context = new GPApplicationContext(config.getInitParameter("contextConfigLocation"));
    }
    ...
}
------------------------手写springMVC---------------------------------------------------
Model：将要传输的数据封装成一个完整的载体；View：用来展示或者输出的模版；Controller：

首先要配置 web.xml文件，配置 DispatcherServlet 类，前文已经配置好了；
然后，初始化 IOC容器 GPApplicationContext，通过 ApplicationContextAware的onRefresh()方法初始化
，但是由于讲AOP的时候，才有 ApplicationContextAware，因此这里简化了ioc容器的初始化，直接在DispatcherServlet
的init()方法中启动：
@Override
public void init(ServletConfig config) throws ServletException {
    GPApplicationContext context = new GPApplicationContext(config.getInitParameter("contextConfigLocation"));
}
在 init()方法中调用 initStrategies(context) 初始化策略方法，该方法定义如下：
protected void initStrategies(GPApplicationContext context) {
    //有九种策略
    // 针对于每个用户请求，都会经过一些处理的策略之后，最终才能有结果输出
    // 每种策略可以自定义干预，但是最终的结果都是一致
    // ModelAndView
    // =============  这里说的就是传说中的九大组件 ================
    initMultipartResolver(context);//文件上传解析，如果请求类型是multipart将通过MultipartResolver进行文件上传解析
    initLocaleResolver(context);//本地化解析
    initThemeResolver(context);//主题解析

    /** 我们自己会实现 */
    //GPHandlerMapping 用来保存Controller中配置的RequestMapping和Method的一个对应关系
    initHandlerMappings(context);//通过HandlerMapping，将请求映射到处理器
    /** 我们自己会实现 */
    //HandlerAdapters 用来动态匹配Method参数，包括类转换，动态赋值
    initHandlerAdapters(context);//通过HandlerAdapter进行多类型的参数动态匹配

    initHandlerExceptionResolvers(context);//如果执行过程中遇到异常，将交给HandlerExceptionResolver来解析
    initRequestToViewNameTranslator(context);//直接解析请求到视图名

    /** 我们自己会实现 */
    //通过ViewResolvers实现动态模板的解析
    //自己解析一套模板语言
    initViewResolvers(context);//通过viewResolver解析逻辑视图到具体视图实现

    initFlashMapManager(context);//flash映射管理器
}
****************************************************************************************
@interface注解：遍历所有类，通过反射可以知道加了某个注解的类，同时注解也可以携带信息value，跟它注释的
地方绑定在一起；
****************************************************************************************
Controller中需要 ModelAndView 类对象，因此，*.webmvc包中创建 GPModelAndView 类
public class GPModelAndView {
    private String viewName;
    private Map<String,?> model;
    //setter，getter方法省略
}

DispatcherServlet 类中先定义 initHandlerMappings()方法，将Controller中的uri与Method绑定在一起

private void initHandlerMappings(GPApplicationContext context) {

}
该方法需要 GPHandlerMapping类，在 *.webmvc中创建：
public class GPHandlerMapping {
    private Object controller;
    private Method method;
    private Pattern pattern;  //url的封装
}
该方法需要 GPHandlerAdapter类，在 *.webmvc中创建：
该方法需要 GPHandlerAdapter类，在 *.webmvc中创建：
该方法需要 GPViewResolver类，在 *.webmvc中创建：

在 doPost()方法中调用 doDispatch()方法：
DispatcherServlet类中定义 doDispatch()方法：
private void doDispatch(HttpServletRequest req, HttpServletResponse resp) {
    //根据用户请求的URL来获得一个Handler
    GPHandlerMapping handler = this.getHandler(req);
    /*由 handler 获得一个 handler适配器*/
    GPHandlerAdapter ha = getHandlerAdapter(handler);
    /*handler适配器处理请求，并返回 ModelAndView*/
    GPModelAndView mv = ha.handle(req, resp, handler);
    /*真正的输出*/
    this.processDispatchResult(resp, mv);
}
****************************************************************************************
HttpServlet中怎么从 service()方法到 doPost()/doGet()方法的？service()里面调用了doPost()/doGet()方法
HttpServlet在Tomcat服务器里面被创建后，服务器先调用它的init()方法，再调用 service()方法，service方法再调用
doPost()/doGet()方法

public class HttpServlet extends GenericServlet{
   @Override
   public void service(ServletRequest req, ServletResponse resp) throws ServletException, IOException {
       HttpServletRequest request = (HttpServletRequest)req;
       HttpServletResponse response = (HttpServletResponse)resp;
       /*再定义一个service方法*/
       this.service(request,response);
   }
   /*service方法重载*/
   public void service(HttpServletRequest request,HttpServletResponse response){
       String method = request.getMethod();
       if ("GET".equals(method)){
           this.doGet(request,response);
       }else if ("POST".equals(method)){
           this.doPost(request,response);
       }
   }
   public void doPost(HttpServletRequest request,HttpServletResponse response){

   }
   public void doGet(HttpServletRequest request,HttpServletResponse response){

   }
}
****************************************************************************************
回到 initHandlerMappings(context)方法，将Controller中配置的RequestMapping中的 uri 和 Method
进行一一对应，将Controller，uri，Method封装到了 GPHandlerMapping中；

客户端请求发送过来后，服务器会调用相应 servlet的 service()方法，service()方法里面调用 doPost()/doGet()
方法，doPost()方法如下：
@Override
protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

}
DispatcherServlet的成员变量：
public class DispatcherServlet extends HttpServlet {
    /*GPHandlerMapping最核心的设计，也是最经典的，它牛B到直接干掉了Struts、Webwork等MVC框架*/
    /*GPHandlerMapping里面封装 Controller对象，请求uri的Pattern(正则匹配)，及Method对象*/
    private List<GPHandlerMapping> handlerMappings = new ArrayList<GPHandlerMapping>();
    /*GPHandlerAdapter有一个 handle()方法，返回 ModelAndView*/
    private Map<GPHandlerMapping,GPHandlerAdapter> handlerAdapters = new HashMap<GPHandlerMapping, GPHandlerAdapter>();
    /*GPViewResolver 将一个静态文件变成一个动态文件*/
    private List<GPViewResolver> viewResolvers = new ArrayList<GPViewResolver>();
}
GPViewResolver 将一个静态文件变成一个动态文件，怎么做的呢？
public class GPViewResolver {
    private String viewName;
    private File templateFile;
    public GPViewResolver(String viewName,File templateFile){
        this.viewName = viewName;
        this.templateFile = templateFile;
    }
}
在maven项目的 resources 目录中创建 layouts 目录，里面创建 first.html页面；
至此，方法调用流程结构定义好了，但是方法具体实现还没定义，下面开始具体定义方法；
initHandlerMappings()：将 @RequestMapping中的 uri与@RequestMapping所注释的方法，还有所在的类封装
到 GPHandlerMapping对象中，然后保存到 handlerMappings list列表中：

private void initHandlerMappings(GPApplicationContext context) {
    /*Controller层模型
    * @Controller
    * public class LoginAction{
    *   @RequestMapping("/login.action")
    *   public ModelAndView login(request,response){
    *       ...
    *   }
    *   ...
    *   @RequestMapping("/logout.action")
     *   public ModelAndView logout(request,response){
     *       ...
     *   }
    * }
    * */
    //首先从容器中取到所有的实例
    String [] beanNames = context.getBeanDefinitionNames();
    for (String beanName : beanNames){
        Object controller = context.getBean(beanName);
        Class<?> clazz = controller.getClass();
        /*只对加了@GPController注释的类进行操作*/
        if(!clazz.isAnnotationPresent(GPController.class)){continue;}
        String baseUrl = "";
        if(clazz.isAnnotationPresent(GPRequestMapping.class)){
            GPRequestMapping requestMapping = clazz.getAnnotation(GPRequestMapping.class);
            baseUrl = requestMapping.value();
        }
        //扫描所有的public方法
        Method[] methods = clazz.getMethods();
        for (Method method: methods) {
            /*只对加了@GPRequestMapping注释的方法进行操作进行操作*/
            if(!method.isAnnotationPresent(GPRequestMapping.class)){ continue;}
            GPRequestMapping requestMapping = method.getAnnotation(GPRequestMapping.class);
            String regex = ("/" + baseUrl +requestMapping.value().replaceAll("\\*",".*")).replaceAll("/+","/");
            Pattern pattern = Pattern.compile(regex);
            this.handlerMappings.add(new GPHandlerMapping(pattern,controller,method));
            System.out.println("Mapping: " + regex + " , " + method);
        }
    }
}

initHandlerAdapters()方法如下：

private void initHandlerAdapters(GPApplicationContext context) {
    /*反射有 2 个参数数组：①参数类型的数组 Class<?>[] 里面存 int.class,String.class 等;
    * ②参数值的列表 Object[] args*/
    /*把所有的方法的参数，实现动态配置；参数分为命名参数和非命名参数
    * 命名参数：@GPRequestParam("name") String name
    * 非命名参数：HttpServletRequest request；request和response从doService()中取得；非命名参数自动赋值；
    * @GPRequestParam("name") String n 作用：@GPRequestParam("name")接收请求头中参数名称
    * 为name的参数的值，保存到后面的变量 n 中；
    @GPController
      @GPRequestMapping("/web")
      public class MyAction {
            @GPAutowired
            IQueryService queryService;
            @GPAutowired IModifyService modifyService;
            @GPRequestMapping("/query.json")
            public GPModelAndView query(HttpServletRequest request, HttpServletResponse response,
                                        @GPRequestParam("name") String name){
            String result = queryService.query(name);
            System.out.println(result);
            return out(response,result);
      }
    }*/

    for (GPHandlerMapping handlerMapping : this.handlerMappings){
        //每一个方法有一个参数列表，那么这里保存的是形参列表
        /*key:paramName,value:index参数在参数列表中的位置*/
        Map<String,Integer> paramMapping = new HashMap<String, Integer>();
        /**/
        Annotation[][] pa = handlerMapping.getMethod().getParameterAnnotations();
        /*i表示第 i 个参数*/
        /*处理命名参数*/
        for (int i = 0; i < pa.length ; i ++) {
            for (Annotation a : pa[i]) {
                if(a instanceof GPRequestParam){
                    String paramName = ((GPRequestParam) a).value();
                    if(!"".equals(paramName.trim())){
                        paramMapping.put(paramName,i);
                    }
                }
            }
        }
        //接下来，我们处理非命名参数，这里只处理Request和Response
        Class<?>[] paramTypes = handlerMapping.getMethod().getParameterTypes();
        for (int i = 0;i < paramTypes.length; i ++) {
            /*反射参数类型 int.class String.class 等*/
            Class<?> type = paramTypes[i];
            if(type == HttpServletRequest.class ||
                    type == HttpServletResponse.class){
                paramMapping.put(type.getName(),i);
            }
        }
        this.handlerAdapters.put(handlerMapping,new GPHandlerAdapter(paramMapping));
        /*调用 handler()方法的时候就会赋值*/
    }
}

/*initViewResolvers() 解决页面名字和模板文件关联的问题*/
private void initViewResolvers(GPApplicationContext context) {
    /*每个页面对应 1 个页面 名字，每个页面名字对应一个模版，模版路径配置在application.properties
    * 内容：templateRoot=layouts；*/
    /*Controller模版：PageAction类
      @GPController
      @GPRequestMapping("/")
      public class PageAction {
          @GPAutowired
          IQueryService queryService;
          @GPRequestMapping("/first.html") 此处只是一个普通请求字符串，不代表请求页面
          public GPModelAndView query(@GPRequestParam("teacher") String teacher){
              String result = queryService.query(teacher);
              Map<String,Object> model = new HashMap<String,Object>();
              model.put("teacher", teacher);
              model.put("data", result);
              model.put("token", "123456");
              //重点重点：将页面名字"first.html"与 模版 layouts 中的页面关联起来；
              return new GPModelAndView("first.html",model);
          }
      }*/
    /*获取 application.properties 中key为templateRoot的value，即 layouts*/
    String templateRoot = context.getConfig().getProperty("templateRoot");
    /*???*/
    String templateRootPath = this.getClass().getClassLoader().getResource(templateRoot).getFile();
    File templateRootDir = new File(templateRootPath);
    for (File template : templateRootDir.listFiles()) {
        this.viewResolvers.add(new GPViewResolver(template.getName(),template));
    }

}

DispatcherServlet类中方法的执行顺序：Tomcat服务器调完servlet的init()方法后，当有请求时，会调
servlet的service()方法，service()里面调用 doPost()，doPost()里面调用 doDispatch()方法，
doDispatch()是 springmvc核心处理部分，即 如下部分
@Controller
@RequestMapping("/web")
public class MyAction{
    @RequestMapping("/first.action")
    public void myfun(){
        ...
    }
}

doDispatch()方法：
private void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws Exception {
    /*根据用户请求的URL来获得一个Handler*/
    GPHandlerMapping handler = this.getHandler(req);
    /*由 handler 获得一个 handler适配器*/
    GPHandlerAdapter ha = this.getHandlerAdapter(handler);
    /*handler适配器处理请求，并返回 ModelAndView*/
    GPModelAndView mv = ha.handle(req, resp, handler);
    /*真正的输出*/
    this.processDispatchResult(resp, mv);
}

测试：
请求 demo 包中 MyAction 类中的 add()方法：
启动tomcat服务器，链接为 http://localhost:8080/web/addTeacher.json?name="Tom"&addr="Hunan"
请求 demo 包中 PageAction 类的 query()方法
启动tomcat服务器，链接为 http://localhost:8080/first.html

-------------------------手写实现springAOP---------------------------------------
到 beans 包的 BeanWrapper 类，将由类全名创建的原始类赋值给 originalInstance和wrapperInstance，
如下，其中 wrapperInstance 是 originalInstance的包装类；
现在，要将动态代理的代码添加进来，将AOP融入到 ioc容器即 GPApplicationContext中去
public BeanWrapper(Object instance) {
    this.originalInstance = instance;
    this.wrapperInstance = this.aopProxy.getProxy(instance);
}
将所有类的类名前面加 GP，包括 beans包，context.support包，core包，webmvc.servlet包

创建 framework.aop 包
在context 包中创建抽象类 GPAbstractApplicationContext，被 GPApplicationContext 继承，里面定义
2个方法，onRefresh()，refreshBeanFactory()，注意，refreshBeanFactory()抽象方法不是在GPApplicationContext
中直接实现，还有 1 个类 GPDefaultListableBeanFactory继承GPAbstractApplicationContext，很多功能
都是在这个 GPDefault*类里面完成的，aop会调用aop的 refreshBeanFactory()，xml会调用xml的refreshBeanFactory()
注意，现在 GPApplicationContext 不直接继承 GPAbstractApplicationContext 了，而是继承 GPDefaultListableBeanFactory

要想获得 ioc容器对象，通过什么方式实现？
实现 GPApplicationContextAware 接口，IOC容器 applicationContext实例化以后，会自动调用
setApplicationContext(GPApplicationContext applicationContext)方法，将容器设置到其他类中；

改造 ioc容器 GPApplicationContext中的 refresh()方法里面的 doAutowrited()方法里面的 getBean()
方法里面的 new GPBeanWrapper(instance)，进入 GPBeanWrapper类的构造方法
public GPBeanWrapper(Object instance) {
    this.originalInstance = instance;
    this.wrapperInstance = instance;
}
将 this.originalInstance = instance;中的 instance换成代理对象，使用Proxy.newInstance();但是直
接在这里使用不方便，封装到 aop 包的 GPAopProxy类(实际是接口，此处简化为类了)中
public class GPAopProxy implements InvocationHandler{
    /*代理类实现代理接口 InvocationHandler的代理方法 invoke(proxy,method,args)其中method为目标类方法对象*/
    /*代理类实现代理接口 InvocationHandler的代理方法invoke()，并持有目标类对象*/
    private Object target;
    /*对目标方法进行增强*/
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return null;
    }
    //把原生的对象传进来，并获得代理对象
    public Object getProxy(Object instance){
        this.target = instance;
        Class<?> clazz = instance.getClass();
        return Proxy.newProxyInstance(clazz.getClassLoader(),clazz.getInterfaces(),this);
    }
}

GPBeanWrapper类增加 aopProxy成员变量如下，修改构造方法如下
/*GPAopProxy类封装getProxy(originInstance)方法，取得原始类对象的代理类对象*/
private GPAopProxy aopProxy = new GPAopProxy();
public GPBeanWrapper(Object instance) {
    this.originalInstance = instance;
    this.wrapperInstance = aopProxy.getProxy(instance);
}

spring中的 aop 需要 expression 表达式进行配置，因此在 resources/application.properties中配置
//配置切入点（及需要增强的方法）
pointCut=public .* com\.gupaoedu\.vip\.spring\.demo\.service\..*Service\..*\(.*\)
//定义切面，在之前调用切面方法（切面类的方法）
aspectBefore=com.gupaoedu.vip.spring.demo.aspect.LogAspect before
//定义切面，在之后调用切面方法（切面类的方法）
aspectAfter=com.gupaoedu.vip.spring.demo.aspect.LogAspect after

此时还没有切面类，需要定义 ，创建 /demo/aspect/LogAspect类，打印日志
public class LogAspect {
    //在调用一个方法之前，执行before方法
    public void before(){
        //这个方法中的逻辑，是由我们自己写的
        System.out.println("Invoker Before Method!!!");
    }
    //在调用一个方法之后，执行after方法
    public void after(){
        System.out.println("Invoker After Method!!!");
    }
}

aop包中定义 GPAopConfig 类，封装并解析 aop 的配置信息；

GPAopProxy类中定义成员变量 private GPAopConfig aopConfig，及set方法；

回到 GPAopProxy类的 invoke 方法，定义如代码；

回到ioc容器 GPApplicationContext的 getBean(beanName) 方法，如下，实际返回的是一个bean的代理对象
return this.beanWrapperMap.get(beanName).getWrapperInstance();

getBean()方法的 GPBeanWrapper beanWrapper = new GPBeanWrapper(instance);后面添加
beanWrapper.setAopConfig(this.instantionAopConfig(beanDefinition));
并创建 instantionAopConfig(beanDefinition)方法

测试：GPApplicationContext类中debug启动主方法，断点设置在 doAutowrited()方法中的 getBean()处，
getBean()返回结果 {$Proxy0@590} "com.gupaoedu.vip.spring.demo.service.impl.QueryService@135"
说明getBean()返回的已经是原始类bean的代理对象了
public static void main(String[] args) {
   GPApplicationContext context = new GPApplicationContext("classpath:application.properties");
}

此时遇到的问题：
/*当加入aop代理GPAopProxy后，返回的不是原始bean对象了，而是其代理对象*/
if(!clazz.isAnnotationPresent(GPController.class)){continue;}

因为 Controller层不参与aop代理，也不使用代理对象，只有 Service层才参与aop代理，使用代理对象，但是现在
getBean()返回的都是代理对象，因此aop包下创建 GPProxyUtils 类，作用：由代理类对象获得其代理的原始目标
类对象；

测试：启动tomcat服务器，浏览器输入 http://localhost:8080/web/query.json?name=Tom
控制台输出结果：说明在执行 Service 层的目标方法之前执行了 切面类中的切面方法before()，目标方法执行之后
执行了切面类中的切面方法after()
$$$$$$$$$$$$$$$$$$$$Invoker Before Method!!!
[Ljava.lang.Object;@67015f8a
$$$$$$$$$$$$$$$$$$$$Invoker After Method!!!
{name:"Tom",time:"2018-05-24 20:29:52"}

