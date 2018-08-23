package com.bjpowernode.service;

import com.bjpowernode.beans.Student;
import com.bjpowernode.dao.IStudentDao;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

@Service
public class StudentServiceImpl implements IStudentSerivce {
    @Resource
    private IStudentDao studentDao;

    public void addStudent(Student student) {
        this.studentDao.addStudent(student);
    }

    @Override
    public List<Student> findByName(String name) {
        return this.studentDao.findByName(name);
    }
}
