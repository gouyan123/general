package com.gupaoedu.catalina.servlets;

import com.gupaoedu.catalina.http.GPRequest;
import com.gupaoedu.catalina.http.GPResponse;
import com.gupaoedu.catalina.http.GPServlet;

public class MyServlet extends GPServlet {
    @Override
    public void doGet(GPRequest request, GPResponse response) {
        response.write(request.getParameter("name"),200);
    }

    @Override
    public void doPost(GPRequest request, GPResponse response) {
        this.doGet(request,response);
    }
}
