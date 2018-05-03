package com.yonyou.sleepAndWait;

public class WaitThread implements Runnable{

    private Service2 service;
    
    public WaitThread(Service2 service){
        this.service = service;
    }

    @Override
    public void run() {
       service.mWait();
        
    }
}
