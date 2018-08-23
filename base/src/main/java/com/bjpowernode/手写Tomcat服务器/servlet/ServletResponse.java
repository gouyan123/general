package com.bjpowernode.手写Tomcat服务器.servlet;

import java.io.PrintWriter;

/**
 * 封装响应参数的接口规范
 * @author SUN公司
 * @version	1.0
 * @since	1.0
 *
 */
public interface ServletResponse {
	
	void setWriter(PrintWriter out);
	
	PrintWriter getWriter();
}
