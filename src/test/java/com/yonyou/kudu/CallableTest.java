package com.yonyou.kudu;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CallableTest {

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        // 线程池
        ExecutorService pool = Executors.newFixedThreadPool(10);

        Callable<String> c1 = new CallableThread("线程1");
        Callable<String> c2 = new CallableThread("线程2");

        // 表示异步计算的结果
        Future<String> f1 = pool.submit(c1);
        Future<String> f2 = pool.submit(c2);

        // 这里要等线程1运行完,f1.get()得到值后,才会走System.out.println(f2.get());
        System.out.println(f1.get());
        System.out.println(f2.get());

        // 关闭线程池
        pool.shutdown();
    }
}
