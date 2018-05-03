package com.yonyou.cache;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.xml.crypto.Data;

public class RWDictionary {

    private final Map map = new TreeMap();
    private final ReentrantReadWriteLock rw = new ReentrantReadWriteLock();
    private final Lock readLock = rw.readLock();
    private final Lock writeLock = rw.writeLock();
    
    
    public Object get(String key){
        readLock.lock();
        try{
            return map.get(key);
        }finally{
            readLock.unlock();
        }
    }
    
    public String[] allKeys() {  
        readLock.lock();  
        try { return (String[]) map.keySet().toArray(); }  
        finally { readLock.unlock(); }  
    }  
    public Data put(String key, Data value) {  
        writeLock.lock();  
        try { return (Data) map.put(key, value); }  
        finally { writeLock.unlock(); }  
    }  
    public void clear() {  
        writeLock.lock();  
        try { map.clear(); }  
        finally { writeLock.unlock(); }  
    }  
            
    public static void main(String[] args) {

    }

}
