package com.bjpowernode.dao;

import com.bjpowernode.beans.Student;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IStudentDao {
    public abstract void addStudent(Student student);
    public abstract List<Student> findByName(String name);
}
