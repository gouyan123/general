package com.stream;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OptionalTest {
    public static void main(String[] args) {
        Stream<String> words = Stream.of("a","bb","ccc","dddd");
//        Optional<String> optionValue = words.filter(s -> s.contains("d")).findFirst();
//        System.out.println(optionValue);
//        words.forEach(System.out::println);
        //collect()是触发算子，将流中记录的数据保存到集合Collection，流Stream只能被触发一次；
        List<String> list = words.collect(Collectors.toList());
    }
}
