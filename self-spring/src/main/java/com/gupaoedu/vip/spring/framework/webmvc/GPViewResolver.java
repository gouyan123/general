package com.gupaoedu.vip.spring.framework.webmvc;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//设计这个类的主要目的是：
//1、将一个静态文件变为一个动态文件
//2、根据用户传送参数不同，产生不同的结果
//最终输出字符串，交给Response输出
/**封装 页面名字，相应页面文件*/
public class GPViewResolver {

    private String viewName;
    private File templateFile;

    public GPViewResolver(String viewName,File templateFile){
        this.viewName = viewName;
        this.templateFile = templateFile;
    }
    /*viewResolver(mv)方法作用：将 ModelAndView对象中指定的页面转换成一个字符串，然后用输出流输出*/
    public String viewResolver(GPModelAndView mv) throws Exception{
        StringBuffer sb = new StringBuffer();
        RandomAccessFile ra = new RandomAccessFile(this.templateFile,"r");
        try {
            String line = null;
            while (null != (line = ra.readLine())) {
                line = new String(line.getBytes("ISO-8859-1"), "utf-8");
                Matcher m = this.matcher(line);
                /**如果输入字符串与正则表达式不匹配，那么看输入字符串的子字符串是否有符合正则表达式的*/
                while (m.find()) {
                    for (int i = 1; i <= m.groupCount(); i++) {
                        /**m.group(i)取出符合正则表达式的子字符串，即把￥{}中间的这个字符串给取出来*/
                        String paramName = m.group(i);
                        Object paramValue = mv.getModel().get(paramName);
                        if (null == paramValue) {
                            continue;
                        }
                        /*把 ￥{id} 替换成id的值，页面只认识字符串*/
                        line = line.replaceAll("￥\\{" + paramName + "\\}", paramValue.toString());
                        line = new String(line.getBytes("utf-8"), "ISO-8859-1");
                    }
                }
                sb.append(line);
            }
        }finally {
            ra.close();
        }
        return sb.toString();
    }

    private Matcher matcher(String str){
        /**创建匹配模式对象 pattern，regex为正则表达式*/
        Pattern pattern = Pattern.compile("￥\\{(.+?)\\}",Pattern.CASE_INSENSITIVE);
        /**创建比较器对象 matcher，其中 str 为输入字符串*/
        Matcher matcher = pattern.matcher(str);
        return  matcher;
    }


    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

}
