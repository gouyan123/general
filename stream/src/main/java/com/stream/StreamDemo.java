package com.stream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class StreamDemo {
    public static void main(String[] args) {
        Stream<String> words = Stream.of("hello", "apple", "China", "Computer");
        //将List集合转换成 并行Stream，即并行RDD，每个RDD上面保存一部分数据，并行计算，最后汇总
        long count =  words.filter(w -> w.length() > 5).count();
        System.out.println(count);
    }
}
