package com.bjpowernode.dao;

import com.bjpowernode.beans.Student;
import com.bjpowernode.utils.MyBatisUtils;
import org.apache.ibatis.session.SqlSession;

import java.io.InputStream;

public class StudentDAOImpl implements IStudentDAO {
    private SqlSession sqlSession;
    @Override
    public void insertStu(Student student)  {
        InputStream inputStream = null;
        try {
//            inputStream = Resources.getResourceAsStream("mybatis.xml");
//            SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(inputStream);
//            this.sqlSession= factory.openSession();
            this.sqlSession = MyBatisUtils.getSqlSession();
            //test为命名空间，程序会自动去读取命名空间为test的*mapper.xml映射文件，并读取里面id为insertStu的内容；
            this.sqlSession.insert("test.insertStu",student);
            /*注意：只执行数据库操作语句，不提交commit，数据库不会变化*/
            this.sqlSession.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (this.sqlSession != null){
                /*close了就不用rollback了*/
                this.sqlSession.close();
            }
        }

    }
}
