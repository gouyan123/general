package com.bjpowernode.dao;

import com.bjpowernode.beans.Student;
import org.apache.ibatis.annotations.Insert;

import java.io.IOException;

public interface IStudentDAO {
    @Insert(value = {"insert into student(name,age,score) values(#{name},#{age},#{score})"})
    public abstract void insertStu(Student student) throws IOException;
}
