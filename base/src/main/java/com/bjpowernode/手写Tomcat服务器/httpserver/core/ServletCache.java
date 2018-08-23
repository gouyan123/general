package com.bjpowernode.手写Tomcat服务器.httpserver.core;

import com.bjpowernode.手写Tomcat服务器.servlet.Servlet;

import java.util.HashMap;
import java.util.Map;

/**Servlet对象缓存池*/
public class ServletCache {
	private static Map<String,Servlet> servletMap = new HashMap<String, Servlet>();
	
	public static void put(String urlPattern,Servlet servlet){
		servletMap.put(urlPattern, servlet);
	}
	
	public static Servlet get(String urlPattern){
		return servletMap.get(urlPattern);
	}
}
