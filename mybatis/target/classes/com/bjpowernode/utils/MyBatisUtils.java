package com.bjpowernode.utils;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;

public class MyBatisUtils {
    private static SqlSessionFactory factory;

    /*获取sqlSession连接对象*/
    public static SqlSession getSqlSession()  {
        SqlSession  sqlSession = null;
        try{
            if (factory == null){//factory为null才创建，否则不创建，避免重复创建factory
                URL url = MyBatisUtils.class.getResource("/mybatis.xml");
                String path = url.getFile();
                File file = new File(path);
                InputStream inputStream = new FileInputStream(file);

                factory = new SqlSessionFactoryBuilder().build(inputStream);
            }
            sqlSession = factory.openSession();
        }catch (Exception e){
            e.printStackTrace();
        }finally {

        }
        return sqlSession;
    }
}
