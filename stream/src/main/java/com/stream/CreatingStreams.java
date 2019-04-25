package com.stream;

import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CreatingStreams {
    public static void main(String[] args) {
        Stream<String> echo = Stream.generate(new Supplier<String>(){
            @Override
            public String get() {
                return "Echo";
            }
        });
        Supplier<String> supplier = () -> {return "Echo";};
        Stream<String> echos = Stream.generate(() -> {
            return "Echo";
        });
        show("echo",echos);
        Stream<Double> random = Stream.generate(()->{
            return Math.random();
        });
        Stream<Double> randoms = Stream.generate(Math::random);
        show("random",randoms);
        Stream<String> words = Stream.of("hello", "apple", "China", "Computer");
//        show("words",words);
        Stream<String> mapWords = words.map(new Function<String, String>() {
            @Override
            public String apply(String s) {
                return s.toLowerCase();
            }
        });
//        Stream<String> mapWords = words.map(String::toLowerCase);
        show("words",mapWords);

        Stream<String> uniqueWords = Stream.of("hello","hello","world").distinct();
        show("uniqueWords",uniqueWords);

//        Stream<String> longestFirst = Stream.of("a","bb","ccc").sorted(new Comparator<String>() {
//            @Override
//            public int compare(String o1, String o2) {
//                if (o1.length() > o2.length()) return -1;
//                if (o1.length() < o2.length()) return 1;
//                return 0;
//            }
//        });
        Stream<String> longestFirst = Stream.of("a","bb","ccc").sorted(Comparator.comparing(String::length).reversed());
        show("longestFirst",longestFirst);
//        Stream<String> peek = Stream.of("haha","hehe").peek(new Consumer<String>() {
//            @Override
//            public void accept(String s) {
//                System.out.println(s);
//            }
//        });
        Stream<String> peek = Stream.of("haha","hehe").peek(System.out::println);
        show("peek",peek);

    }
    public static <T> void show(String title,Stream<T> stream){
        final int SIZE = 10;
        List<T> firstElements = stream.limit(SIZE + 1).collect(Collectors.toList());
        System.out.print(title + ": ");
        for (int i=0;i<firstElements.size();i++){
            if (i>0) System.out.println(", ");
            if (i<SIZE) System.out.println(firstElements.get(i));
            else System.out.println("...");
        }
        System.out.println();
    }
}
