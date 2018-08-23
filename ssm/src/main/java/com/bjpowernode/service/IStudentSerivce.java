package com.bjpowernode.service;

import com.bjpowernode.beans.Student;

import java.util.List;

public interface IStudentSerivce {
    public abstract void addStudent(Student student);
    public abstract List<Student> findByName(String name);
}
