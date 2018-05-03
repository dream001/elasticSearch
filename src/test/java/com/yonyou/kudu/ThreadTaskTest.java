package com.yonyou.kudu;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ThreadTaskTest {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ThreadTaskTest.threadTaskList();
    }

    private static void threadTaskList() throws InterruptedException, ExecutionException {
        int maxThreads = 5;
        Map<String, Future<Boolean>> results = new HashMap<String, Future<Boolean>>();

        ExecutorService executor = Executors.newFixedThreadPool(maxThreads);
        for (int i = 0; i < 5; i++) {
            Future<Boolean> future = executor.submit(()->{
                System.out.println(Thread.currentThread().getName() + " Start... waiting 5000");
                Thread.sleep(5000);
                System.out.println(
                        Thread.currentThread().getName() + " thread_id=" + Thread.currentThread().getId() + " Finished.");
                return true;
            });
            results.put("thread-" + i, future);
        }
        for (Entry<String, Future<Boolean>> entry : results.entrySet()) {
            String key = entry.getKey();
            Future<Boolean> value = entry.getValue();
            System.out.println(key + "\t" + value.get());
        }
        executor.shutdown();
    }

}
