package com.dongnao.mark.future;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**将 Callable接口对象封装到 FutureTask中，并将FutureTask作为任务提交给线程池，线程池中工作线程触发
 * call()方法，重点，提交FutureTask的线程不用等待返回结果，直接调用FutureTask.get()方法，即可获得返
 * 回结果*/
public class FutureSample02 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FutureTask<Integer> futureTask = new FutureTask<Integer>(new ComputerTask(0,"gy"));
        ExecutorService pool = Executors.newFixedThreadPool(5);
        pool.submit(futureTask);
        Integer result = futureTask.get();
        System.out.println("result = " + result);
        System.out.println("先做其他事情");
        pool.shutdown();
    }
}
