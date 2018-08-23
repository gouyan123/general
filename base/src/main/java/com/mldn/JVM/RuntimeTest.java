package com.mldn.JVM;

public class RuntimeTest {
    public static void main(String[] args) {
        Runtime runtime = Runtime.getRuntime();
        long maxMemory = runtime.maxMemory();
        long totalMemory = runtime.totalMemory();
        System.out.println(maxMemory/1024/1024 + "M");
        System.out.println(totalMemory/1024/1024 + "M");
    }
}
