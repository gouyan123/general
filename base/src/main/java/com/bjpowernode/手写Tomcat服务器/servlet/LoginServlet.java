package com.bjpowernode.手写Tomcat服务器.servlet;

import java.io.PrintWriter;

/**处理登录业务的java程序，该java程序由webApp开发人，由web服务器开人员负责调用*/
public class LoginServlet implements Servlet{
	//处理业务的核心类
	public void service(ServletRequest request,ServletResponse response){
		System.out.println("正在验证身份，请稍等....");
		//获取响应流对象
		PrintWriter out = response.getWriter();
		out.print("<html>");
		out.print("<head>");
		out.print("<title>正在验证</title>");
		out.print("<meta content='text/html;charset=utf-8'/>");
		out.print("</head>");
		out.print("<body>");
		out.print("<center><font size='35px' color='blue'>正在验证身份，请稍等....</font></center>");
		out.print("</body>");
		out.print("</html>");
	}
}
