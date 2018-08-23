package com.bjpowernode.手写Tomcat服务器.servlet;

import java.io.PrintWriter;

/**负责封装响应参数对象*/
public class ResponseObject implements ServletResponse{
	private PrintWriter out;
	public void setWriter(PrintWriter out){
		this.out = out;
	}
	public PrintWriter getWriter(){
		return out;
	}
}
