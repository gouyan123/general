package com.bjpowernode.手写Tomcat服务器.servlet;
/**负责封装请求参数对象*/
public interface ServletRequest {
	/**获取单个参数的值*/
	String getParameterValue(String key);
	/**获取多选框的值*/
	String[] getParameterValues(String key);
}
