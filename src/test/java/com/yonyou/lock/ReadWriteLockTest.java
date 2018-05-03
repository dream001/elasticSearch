package com.yonyou.lock;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ReentrantReadWriteLock
 * 问题抛出：
 * 对于多个读 一个写的并发问题：
 * 老的对象的释放问题（对象不再heap 中，无法直接释放）：finalize synchronized 等待读操作完事在写
 * 解决方案：
 * 读之前：没有其他线程的写锁；没有写请求或者有写请求，但调用线程和持有锁的线程是同一个
 * 写之前：没有其他线程的读锁；没有其他线程的写锁
 * 小结：
 * 对于大量读，少量写的时候，线程之间没有竞争，所以比 synchronized 要好很多
 * 如果精准的控制缓存，绝对是一个好的方案
 * 读写锁：分为读锁和写锁，多个读锁不互斥，读锁与写锁互斥，这是由jvm自己控制的，
 * 你只要上好相应的锁即可。如果你的代码只读数据，可以很多人同时读，但不能同时写，
 * 那就上读锁；如果你的代码修改数据，只能有一个人在写，且不能同时读取，那就上写锁。
 * 总之，读的时候上读锁，写的时候上写锁！
 * 
 * 总结：
 * (a).重入方面其内部的WriteLock可以获取ReadLock，但是反过来ReadLock想要获得WriteLock则永远都不要想。
 * (b).WriteLock可以降级为ReadLock，顺序是：先获得WriteLock再获得ReadLock，然后释放WriteLock，这时候线程将保持Readlock的持有。反过来ReadLock想要升级为WriteLock则不可能，为什么？参看(a)，呵呵.
 * (c).ReadLock可以被多个线程持有并且在作用时排斥任何的WriteLock，而WriteLock则是完全的互斥。这一特性最为重要，因为对于高读取频率而相对较低写入的数据结构，使用此类锁同步机制则可以提高并发量。
 * (d).不管是ReadLock还是WriteLock都支持Interrupt，语义与ReentrantLock一致。
 * (e).WriteLock支持Condition并且与ReentrantLock语义一致，而ReadLock则不能使用Condition，否则抛出UnsupportedOperationException异常。
 * 
 * @ClassName: ReadWriteLockTest
 * @Description: TODO(类简要描述，必须以句号为结束)
 * @author caozq
 * @date 2018年4月11日
 */
public class ReadWriteLockTest {

    public static void main(String[] args) {

        final Queue3 q3 = new Queue3();
//        for (int i = 0; i < 3; i++) {
//            new Thread() {
//                public void run() {
//                    while (true) {
//                        q3.get();
//                    }
//                }
//
//            }.start();
//        }
//        for (int i = 0; i < 3; i++) {
//            new Thread() {
//                public void run() {
//                    while (true) {
//                        q3.put(new Random().nextInt(10000));
//                    }
//                }
//
//            }.start();
//        }
        
        
        
        ExecutorService executor = Executors.newCachedThreadPool();
        
        for (int i = 0; i < 3; i++) {  
            executor.execute(new Runnable() {  
                  
                @Override  
                public void run() {  
                    for (int j = 0; j < 5; j++)  
                        q3.put(new Random().nextInt(1000));  
                }  
            });  
        }  
          
        for (int i = 0; i < 3; i++) {  
            executor.execute(new Runnable() {  
                  
                @Override  
                public void run() {  
                    for (int j = 0; j < 5; j++)  
                        q3.get();  
                }  
            });  
        }  
        
        
        executor.shutdown();  

    }

}
