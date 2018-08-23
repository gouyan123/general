package com.gupaoedu.catalina.http;

public abstract class GPServlet {
	public abstract void doGet(GPRequest request,GPResponse response);
	public abstract void doPost(GPRequest request,GPResponse response);
}
