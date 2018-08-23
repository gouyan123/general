package resourcePath;

import java.io.*;
import java.net.URL;
import java.util.Properties;

public class GetReaourceDemo {
    public static void main(String[] args) throws IOException {
        URL url = GetReaourceDemo.class.getResource("/jdbc_mysql.properties");//获取test.txt文件url
        String path = url.getFile();//获取资源全路径
        System.out.println("path : " + path);
        File file = new File(path);//创建文件对象
        InputStream inputStream = new FileInputStream(file);//创建文件输入流
        Reader reader = new InputStreamReader(inputStream);//字节输入流转为字符输入流
        BufferedReader br = new BufferedReader(reader);//在reader对象外面包一层BufferedReader增强功能；
        String temp = null;
        /*
        * 输入输出流相当于一个管道，程序是输入流输出流的中转站，即输入流内容流入程序，程序通过输出流流到目的地
        * 输入流：内容先装入管道，当输入流执行read()方法的时候，管道里面内容流到程序中，管道中不再有该内容
        * 输出流：输出流执行write()方法，向管道装入内容，然后流到目的地
        *
        * */
//        while ( (temp = br.readLine()) != null){//读取文件到程序
//            System.out.println(temp);
//        }
        Properties properties = new Properties();
        properties.load(inputStream);
        System.out.println("======" + properties.getProperty("jdbc.driver"));
        System.out.println(properties);
    }
}