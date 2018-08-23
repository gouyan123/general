package com.bjpowernode.controllers;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class MyControler implements Controller {
    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        ModelAndView mv = new ModelAndView();
        /*底层原理：request.setAttribute()*/
        mv.addObject("msg","welcom");
        /*完整路径 /WEB-INF/jsp/welcome.jsp 前缀后缀由框架根据springmvc.xml配置文件添加*/
        mv.setViewName("welcome");
        return mv;
    }
}
