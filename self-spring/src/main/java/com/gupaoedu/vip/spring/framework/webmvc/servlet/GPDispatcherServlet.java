package com.gupaoedu.vip.spring.framework.webmvc.servlet;

import com.gupaoedu.vip.spring.framework.annotation.GPController;
import com.gupaoedu.vip.spring.framework.annotation.GPRequestMapping;
import com.gupaoedu.vip.spring.framework.annotation.GPRequestParam;
import com.gupaoedu.vip.spring.framework.aop.GPAopProxyUtils;
import com.gupaoedu.vip.spring.framework.context.GPApplicationContext;
import com.gupaoedu.vip.spring.framework.webmvc.GPHandlerAdapter;
import com.gupaoedu.vip.spring.framework.webmvc.GPHandlerMapping;
import com.gupaoedu.vip.spring.framework.webmvc.GPModelAndView;
import com.gupaoedu.vip.spring.framework.webmvc.GPViewResolver;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GPDispatcherServlet extends HttpServlet {
    /*GPHandlerMapping最核心的设计，也是最经典的，它牛B到直接干掉了Struts、Webwork等MVC框架*/
    /*GPHandlerMapping里面封装 Controller对象，请求uri的Pattern(正则匹配)，及Method对象*/
    private List<GPHandlerMapping> handlerMappings = new ArrayList<GPHandlerMapping>();
    /*GPHandlerAdapter有一个 handle()方法，返回 ModelAndView*/
    private Map<GPHandlerMapping,GPHandlerAdapter> handlerAdapters = new HashMap<GPHandlerMapping, GPHandlerAdapter>();
    /**GPViewResolver封装html文件的 文件名字，文件对象*/
    private List<GPViewResolver> viewResolvers = new ArrayList<GPViewResolver>();
    @Override
    public void init(ServletConfig config) throws ServletException {
        /**浏览器请求url发送到Tomcat服务器，Tomcat找到该url对应的 servlet，并调用其构造方法，
         * 初始化方法 init()，服务方法 service(ServletRequest,ServletResponse)，service()
         * 方法里面会调用 doGet()或者doPost()方法*/
        /*config对象是对 web.xml 配置文件的封装，从里面获得该servlet的parameter*/
        GPApplicationContext context = new GPApplicationContext(config.getInitParameter("contextConfigLocation"));
        /*调用初始化策略方法*/
        this.initStrategies(context);
        /*Tomcat服务器调完servlet的init()方法后，当有请求时，会调servlet的service()方法，service()
        * 里面调用 doPost()，doPost()里面调用 doDispatch()方法*/
    }
    protected void initStrategies(GPApplicationContext context) {
        //有九种策略
        // 针对于每个用户请求，都会经过一些处理的策略之后，最终才能有结果输出
        // 每种策略可以自定义干预，但是最终的结果都是一致
        // ModelAndView

        // =============  这里说的就是传说中的九大组件 ================
        this.initMultipartResolver(context);//文件上传解析，如果请求类型是multipart将通过MultipartResolver进行文件上传解析
        this.initLocaleResolver(context);//本地化解析
        this.initThemeResolver(context);//主题解析

        /** 我们自己会实现 */
        /**GPHandlerMapping 封装Controller对象，Method对象，及 url*/
        this.initHandlerMappings(context);//通过HandlerMapping，将请求映射到处理器
        /** 我们自己会实现 */
        /**HandlerAdapters 用来动态匹配Method参数，包括类转换，动态赋值*/
        this.initHandlerAdapters(context);//通过HandlerAdapter进行多类型的参数动态匹配

        this.initHandlerExceptionResolvers(context);//如果执行过程中遇到异常，将交给HandlerExceptionResolver来解析
        this.initRequestToViewNameTranslator(context);//直接解析请求到视图名

        /** 我们自己会实现 */
        //通过ViewResolvers实现动态模板的解析
        //自己解析一套模板语言
        this.initViewResolvers(context);//通过viewResolver解析逻辑视图到具体视图实现

        this.initFlashMapManager(context);//flash映射管理器
    }
    /**将 ioc 容器中所有的对象(加了@Controller注解的) controller，method对象，url
     * 封装到 GPHandlerMappting 类中，然后添加到 handlerMappings 列表中 */
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
        try {
            String [] beanNames = context.getBeanDefinitionNames();
            for (String beanName : beanNames){
                /*springMVC阶段*/
                /*Object controller = context.getBean(beanName);*/
                /*springAOP阶段*/
                Object proxy = context.getBean(beanName);
                Object controller = null;
                controller = GPAopProxyUtils.getTargetObject(proxy);
                Class<?> clazz = controller.getClass();
                /*只对加了@GPController注释的类进行操作*/
                /*当加入aop代理GPAopProxy后，返回的不是原始bean对象了，而是其代理对象*/
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**将GPHandlerMapping和handlerAdapters*/
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
                @GPAutowired
                IModifyService modifyService;

                @GPRequestMapping("/query.json")
                public GPModelAndView query(HttpServletRequest request, HttpServletResponse response,
                                            @GPRequestParam("name") String name){
                String result = this.queryService.query(name);
                System.out.println(result);
                return out(response,result);
          }
	    }*/
        for (GPHandlerMapping handlerMapping : this.handlerMappings){
            //每一个方法有一个参数列表，那么这里保存的是形参列表
            /*key:paramName,value:index参数在参数列表中的位置*/
            Map<String,Integer> paramMapping = new HashMap<String, Integer>();
            /**不管几维数组，都看成一维数组 [...,...,...]，多维数组只是一维数组里面每个位置存的都是
             * 一个数组 [[...],[...],[...]]*/
            /*pa是一个一维数组，数组长度代表参数个数，里面每个参数也是一个数组(长度不定)，存的是 Annotation*/
            Annotation[][] pa = handlerMapping.getMethod().getParameterAnnotations();
            System.out.println("**********" + handlerMapping.getMethod().getName());
            for (int i=0;i<pa.length;i++){
                for (int j=0;j<pa[i].length;j++){
                    System.out.print("pa[" + i + "][" + j + "] = " + pa[i][j]);
                }
                System.out.println("");
            }
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

    /**initViewResolvers() 将layouts目录下所有html文件的，文件名字，文件 封装到 GPViewResolver中，
     * 再将其封装到列表中*/
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

        String templateRootPath = this.getClass().getClassLoader().getResource(templateRoot).getFile();
        File templateRootDir = new File(templateRootPath);
        /**将 layouts 目录下所有html文件的，文件名字，文件 封装到 GPViewResolver中，再将其封装到列表中*/
        for (File template : templateRootDir.listFiles()) {
            this.viewResolvers.add(new GPViewResolver(template.getName(),template));
        }
    }

    private void initFlashMapManager(GPApplicationContext context) {}
    private void initRequestToViewNameTranslator(GPApplicationContext context) {}
    private void initHandlerExceptionResolvers(GPApplicationContext context) {}
    private void initThemeResolver(GPApplicationContext context) {}
    private void initLocaleResolver(GPApplicationContext context) {}
    private void initMultipartResolver(GPApplicationContext context) {}

    /**当浏览器发送请求url到Tomcat时，Tomcat会调用dispatcherServlet的 service(request,response)
     * 方法，service()方法里面根据请求类型，调用doGet(req,resp)或doPost(req,resp)方法，doPost()
     * 方法再调用 doDispatch(req,resp)方法*/
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            /**调用 doDispatch()方法*/
            this.doDispatch(req, resp);
        }catch (Exception e){
            /*调用doDispatch(req, resp)发送异常时，返回错误页面*/
            resp.getWriter().write("<font size='25' color='blue'>500 Exception</font><br/>Details:<br/>" + Arrays.toString(e.getStackTrace()).replaceAll("\\[|\\]","")
                    .replaceAll("\\s","\r\n") +  "<font color='green'><i>Copyright@GupaoEDU</i></font>");
            e.printStackTrace();
        }
    }
    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        /*根据用户请求的URL来获得一个Handler*/
        GPHandlerMapping handlerMapping = this.getHandlerMapping(req);
        if(handlerMapping == null){
            resp.getWriter().write("<font size='25' color='red'>404 Not Found</font><br/><font color='green'><i>Copyright@GupaoEDU</i></font>");
            return;
        }
        /*由 handler 获得一个 handler适配器*/
        GPHandlerAdapter ha = this.getHandlerAdapter(handlerMapping);
        /*handler适配器处理请求，并返回 ModelAndView*/
        GPModelAndView mv = ha.handle(req, resp, handlerMapping);
        /**将 ModelAndView对象中指定的页面封装到字符串中，然后写入输出流resp中*/
        this.processDispatchResult(resp, mv);
    }

    private void processDispatchResult(HttpServletResponse resp, GPModelAndView mv) throws Exception {
        /*调用viewResolver的resolveView方法*/
        if(null == mv){ return;}
        if(this.viewResolvers.isEmpty()){ return;}
        for (GPViewResolver viewResolver: this.viewResolvers) {
            if(!mv.getViewName().equals(viewResolver.getViewName())){ continue; }
            /*viewResolver(mv)方法作用：将 ModelAndView对象里面指定的页面转换成一个字符串，然后用输出流输出*/
            String out = viewResolver.viewResolver(mv);
            if(out != null){
                resp.getWriter().write(out);
                break;
            }
        }
    }
    /**根据用户请求 url 获得GPHandlerMapping对象，封装 */
    private GPHandlerMapping getHandlerMapping(HttpServletRequest req) {
        if(this.handlerMappings.isEmpty()){ return  null;}
        String url = req.getRequestURI();
        String contextPath = req.getContextPath();
        url = url.replace(contextPath,"").replaceAll("/+","/");
        for (GPHandlerMapping handlerMapping : this.handlerMappings) {
            Matcher matcher = handlerMapping.getPattern().matcher(url);
            if(!matcher.matches()){ continue;}
            return handlerMapping;
        }
        return null;
    }

    private GPHandlerAdapter getHandlerAdapter(GPHandlerMapping handlerMapping) {
        if(this.handlerAdapters.isEmpty()){return  null;}
        return this.handlerAdapters.get(handlerMapping);
    }
}
