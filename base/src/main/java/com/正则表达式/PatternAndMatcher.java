package com.正则表达式;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternAndMatcher {
    public static void main(String[] args) {
        /**创建匹配模式类Patternl对象 p */
        Pattern p=Pattern.compile("\\d+");
        /**创建比较器类Matcher对象 m */
        Matcher m=p.matcher("我的QQ是:456456 我的电话是:0532214 我的邮箱是:aaa123@aaa.com");
        /**m.find()判断p.matcher(input:"")输入字符串里面是否有匹配Pattern.compile(regex)中
         * 正则表达式的子字符串*/
        /**如果 m.matchers()返回 true，即输入字符串完全匹配正则表达式，则m.find()会返回false*/
        /**如果输入字符串与正则表达式不匹配，那么看输入字符串的子字符串是否有符合正则表达式的*/
        while(m.find()) {
            /**m.group()输出 输入字符串中匹配正则表达式的子字符串*/
            System.out.println(m.group());
            System.out.print("start:"+m.start());
            System.out.println(" end:"+m.end());
        }
        /**m.matchers()输入字符串本身是否匹配正则表达式*/
        System.out.println(m.matches());
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher("123456");
        System.out.println(matcher.matches());
        while (matcher.find()){
            System.out.println(matcher.group());
        }
    }
}
