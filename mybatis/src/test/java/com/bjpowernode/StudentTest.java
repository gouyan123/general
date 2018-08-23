package com.bjpowernode;

import com.bjpowernode.beans.Student;
import com.bjpowernode.dao.IStudentDAO;
import com.bjpowernode.dao.StudentDAOImpl;
import com.bjpowernode.utils.MyBatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class StudentTest {
    private IStudentDAO studentDAO;
    private SqlSession sqlSession;
    @Before
    public void before(){
        this.sqlSession = MyBatisUtils.getSqlSession();
        //this.studentDAO = new StudentDAOImpl();根据 test.insertStu找到对应sql语句
        this.studentDAO = this.sqlSession.getMapper(IStudentDAO.class);
    }
    @After
    public void after(){
        if (this.sqlSession != null){
            this.sqlSession.close();
        }
    }
    @Test
    public void testInsert() throws IOException {
        Student student = new Student("Jordan",33, (double) 99);
        //   相当于mapper.xml命名空间
        this.studentDAO.insertStu(student);
        this.sqlSession.commit();
    }
}
