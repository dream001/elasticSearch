package com.yonyou.lock;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CacheDemo {

    private Map<String, Object> map = new HashMap<String, Object>();//缓存器
    private ReadWriteLock rwl = new ReentrantReadWriteLock();
    
    
    public static void main(String[] args) {
//        ReadWriteLock lock = new ReentrantReadWriteLock(); 
//        Lock readlock = lock.readLock();
//        readlock.lock();
//        Lock writelock = lock.writeLock();
//        writelock.lock();
        CacheDemo demo = new CacheDemo();
        demo.map.put("key", "value");
        //demo.map.put("key2", "value2");
       
        System.out.println("----" + demo.get("key"));
        
        System.out.println("++++" + demo.get("key2"));
        
        
    }
    
    
    
    
    public Object get(String id){
        Object value = null;
        rwl.readLock().lock();//首先开启读锁，从缓存中去取
        try{
            value = map.get(id); 
            if(value == null){  //如果缓存中没有释放读锁，上写锁
                rwl.readLock().unlock();
                rwl.writeLock().lock();
                try{
                    if(value == null){
                        value = "valueWrite";  //此时可以去数据库中查找，这里简单的模拟一下
                        map.put(id, value);
                    }
                }finally{
                    rwl.writeLock().unlock(); //释放写锁
                }
                rwl.readLock().lock(); //然后再上读锁
            }
        }finally{
            rwl.readLock().unlock(); //最后释放读锁
        }
        return value;
    }

}
