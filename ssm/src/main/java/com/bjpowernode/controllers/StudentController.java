package com.bjpowernode.controllers;

import com.alibaba.fastjson.JSON;
import com.bjpowernode.beans.Student;
import com.bjpowernode.service.IStudentSerivce;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/test")
public class StudentController {
    @Resource
    private IStudentSerivce studentServiceImpl;

    @RequestMapping("/register.do")
    public String register(String name,Integer age) throws Exception {
        //ModelAndView mv = new ModelAndView();
        Student student = new Student();
        student.setName(name);
        student.setAge(age);
        this.studentServiceImpl.addStudent(student);
        //mv.setViewName("/welcome.jsp");
        return "/welcome.jsp";
    }

    @RequestMapping("/findByName.do")
    public ModelAndView findByName(String name){
        ModelAndView mv = new ModelAndView();
        List<Student> students = this.studentServiceImpl.findByName(name);
        System.out.println("students :" + JSON.toJSONString(students,true));
        mv.addObject("students",students);
        mv.setViewName("forward:/WEB-INF/jsp/show.jsp");
        return mv;
    }

}
