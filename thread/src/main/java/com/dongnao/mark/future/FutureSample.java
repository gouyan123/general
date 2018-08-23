package com.dongnao.mark.future;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class FutureSample {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FutureSample futureSample = new FutureSample();
        /*创建任务列表*/
        List<FutureTask<Integer>> taskList = new ArrayList<FutureTask<Integer>>();
        /*Executors工厂类创建线程连接池*/
        ExecutorService pool = Executors.newFixedThreadPool(5);
        for (int i=0;i<10;i++){
            /*FutureTask是Future接口的实现类，有 get()方法，获得任务线程对象的返回结果*/
            FutureTask<Integer> ft = new FutureTask<Integer>(new ComputerTask(i,"gy_" + i));
            taskList.add(ft);
            /*提交任务*/
            pool.submit(ft);/*绝对重点：主线程提交完任务线程对象后，不必等待返回结果，而是通过ft.get()获取结果*/
        }
        System.out.println("主线程已经提交任务，做自己的事情");
        Integer totalResult = 0;
        for (FutureTask<Integer> ft : taskList){
            totalResult += ft.get();
        }
        System.out.println("totalResult = " + totalResult);
        /*关闭线程池*/
        pool.shutdown();
    }
}
