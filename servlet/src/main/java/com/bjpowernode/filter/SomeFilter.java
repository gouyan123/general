package com.bjpowernode.filter;

import javax.servlet.*;
import java.io.IOException;
import java.util.Enumeration;

public class SomeFilter implements Filter {
    private FilterConfig filterConfig;/*只初始化一次，不存在修改问题，不存在线程不安全问题*/
    public SomeFilter() {
        System.out.println("创建 someFilter");
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("********** init **********");
        this.filterConfig = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("********** doFilter：before**********");

        System.out.println("********** doFilter：after**********");
        /*获取filterName*/
        String filterName = this.filterConfig.getFilterName();
        System.out.println("filterName = " + filterName);
        /*获取初始化参数名称*/
        Enumeration<String> names = this.filterConfig.getInitParameterNames();
        /*变量枚举*/
        while (names.hasMoreElements()){
            String name = names.nextElement();
            Object value = this.filterConfig.getInitParameter(name);
            System.out.println("InitName = " + name + " InitValue = " + value);
        }
        /*获取全局域*/
        ServletContext sc = this.filterConfig.getServletContext();
        System.out.println("ServletContext = " + sc);
        chain.doFilter(request,response);/*请求发给下一个资源*/
    }

    @Override
    public void destroy() {
        System.out.println("********** destroy **********");
    }
}
