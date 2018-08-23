package com.dongnao.mark.future;

import java.util.concurrent.*;

/**将 Callable接口对象作为任务提交给线程池，线程池中工作线程触发 call()方法，返回 Future对象，重点，
 * 提交Callable的线程不用等待返回结果，直接调用Future.get()方法，即可获得返回结果*/
public class FutureSample03 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(5);
        Future<Integer> future = pool.submit(new ComputerTask(0,"gy"));
        Integer result = future.get();
        System.out.println("result = " + result);
        System.out.println("先做其他事情");
        pool.shutdown();
    }
}
