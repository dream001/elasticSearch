package com.yonyou.lock;

import java.util.Random;

/**
 * ReentrantReadWriteLock与ReentrantLock
 * 
 * ReentrantLock
 * 实现了标准的互斥操作，也就是一次只能有一个线程持有锁，也即所谓独占锁的概念。
 * 前面的章节中一直在强调这个特点。显然这个特点在一定程度上面减低了吞吐量，
 * 实际上独占锁是一种保守的锁策略，在这种情况下任何“读/读”，“写/读”，
 * “写/写”操作都不能同时发生。但是同样需要强调的一个概念是，锁是有一定的开销的，
 * 当并发比较大的时候，锁的开销就比较客观了。所以如果可能的话就尽量少用锁，
 * 非要用锁的话就尝试看能否改造为读写锁
 * 
 * 
 * ReadWriteLock 描述的是：一个资源能够被多个读线程访问，或者被一个写线程访问，
 * 但是不能同时存在读写线程。也就是说读写锁使用的场合是一个共享资源被大量读取操作，
 * 而只有少量的写操作（修改数据）.清单0描述了ReentrantReadWriteLock的API。
 * 
 * 
 * https://my.oschina.net/adan1/blog/158107
 * 
 * http://www.cnblogs.com/xrq730/p/4979021.html
 * https://blog.csdn.net/fw0124/article/details/6672522
 * @ClassName: ReentrantLockTest
 * @Description: TODO(类简要描述，必须以句号为结束)
 * @author caozq
 * @date 2018年4月11日
 */
public class ReentrantLockTest {
    

    public static void main(String[] args) {

        new Thread(){
            public void run(){
                System.out.println("----");
            }
        }.start();
        
        
        
        
    }
}
