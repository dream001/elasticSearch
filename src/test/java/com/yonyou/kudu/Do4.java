package com.yonyou.kudu;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Do4 implements Callable<String>{

    
    private int id;
    public Do4(int id){
        this.id = id;
    }
    
    
    public static void main(String[] args) {
        
        ExecutorService exec = Executors.newCachedThreadPool();//工头
        ArrayList<Future<String>> results = new ArrayList<Future<String>>();//
        for(int i = 0 ; i < 10 ;i++){
            results.add(exec.submit(new Do4(i)));//submit返回一个Future，代表了即将要返回的结果
        }
    }
    
    @Override
    public String call() throws Exception {
        // TODO Auto-generated method stub
        return "result of taskWithResult "+id;
    }  
}
