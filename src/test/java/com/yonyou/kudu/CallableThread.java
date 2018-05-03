package com.yonyou.kudu;

import java.util.concurrent.Callable;

public class CallableThread implements Callable<String>{

    private String str;  
    
    public CallableThread(String str) {  
        this.str = str;  
    }  
  
    // 需要实现Callable的Call方法  
    public String call() throws Exception {  
        return str;  
    }
    
}
