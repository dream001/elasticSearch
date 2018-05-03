package com.yonyou.cache;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 考虑多个线程访问的时候产生  读写的问题
 * 
 * @ClassName: ReentrantCache
 * @Description: TODO(类简要描述，必须以句号为结束)
 * @author caozq
 * @date 2018年4月23日
 */
public class ReentrantCache {

    Object data;
    volatile boolean cacheValid;
    ReentrantReadWriteLock reen = new ReentrantReadWriteLock();
    
    void prossessCacheDate(){
        reen.readLock().lock();
        if(!cacheValid){
            reen.readLock().unlock();
            reen.writeLock().lock();
            if(!cacheValid){
                data = "";
                cacheValid = true;
            }
            reen.writeLock().unlock();
            reen.readLock().lock();
        }
        System.out.println("begin use data");
        reen.readLock().unlock();
    }


    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

}
