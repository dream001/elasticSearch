package com.yonyou.sleepAndWait;

public class SleepThread implements Runnable{

    private Service2 service;
    
    public SleepThread(Service2 service) {
        this.service = service;
    }
    
    public void run(){
        service.mSleep();
    }
}
