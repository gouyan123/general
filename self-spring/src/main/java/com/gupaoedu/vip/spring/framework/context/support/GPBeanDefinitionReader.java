package com.gupaoedu.vip.spring.framework.context.support;

import com.gupaoedu.vip.spring.framework.beans.GPBeanDefinition;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

//用对配置文件进行查找，读取、解析
public class GPBeanDefinitionReader {
    /*将配置文件中的数据封装到 对象 config 中*/
    private  Properties propertiesConfig = new Properties();
    /*扫描的包的路径，只扫描该包里面的类，获取类全名*/
    private final String SCAN_PACKAGE = "scanPackage";
    /*registyBeanClasses 类全名*/
    private List<String> registyBeanClasses = new ArrayList<String>();
    public GPBeanDefinitionReader() {
    }
    /*①定位                               接收要读取的文件的路径*/
    public GPBeanDefinitionReader(String... contextConfigLocations) {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(contextConfigLocations[0].replace("classpath:",""));
        try {
            /*将 application.properties配置文件中内容，封装到 Properties config中*/
            this.propertiesConfig.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(null != is){is.close();}
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        /**扫描指定包里面所有 *.class 文件，拼接出所有类的类全名(包名 + 文件名)，保存到 registyBeanClasses 列表中*/
        this.doScanner(this.propertiesConfig.getProperty(SCAN_PACKAGE));
    }
    /*②加载：返回指定包下所有类的 类全名*/
    public List<String> loadBeanDefinitions(){
        return this.registyBeanClasses;
    }
    /*每注册一个className，registyBeanClasses就返回一个BeanDefinition，BeanDefinition由我自己
    * 包装*/
    /*③注册*/
    public GPBeanDefinition registerBean(String className){
        if(this.registyBeanClasses.contains(className)){
            GPBeanDefinition beanDefinition = new GPBeanDefinition();
            /*<bean id="factoryBeanName" class="className">*/
            beanDefinition.setBeanClassName(className);
            beanDefinition.setFactoryBeanName(lowerFirstCase(className.substring(className.lastIndexOf(".") + 1)));
            return beanDefinition;
        }
        return null;
    }
    public Properties getConfig(){
        return this.propertiesConfig;
    }
    /**获取指定包里面所有类的 类全名(包名 + 文件名)，保存到 registyBeanClasses map中*/
    private void doScanner(String packageName) {
        URL url = this.getClass().getClassLoader().getResource( packageName.replaceAll("\\.","/"));
        File classDir = new File(url.getFile());
        for (File file : classDir.listFiles()){
            if(file.isDirectory()){
                doScanner(packageName + "." +file.getName());
            }else {
                this.registyBeanClasses.add(packageName + "." + file.getName().replace(".class",""));
            }
        }
    }
    /*首字母小写*/
    private String lowerFirstCase(String str){
        char [] chars = str.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }
}
