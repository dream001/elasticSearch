package com.yonyou.sleepAndWait;

public class Service2 {

    public void mSleep(){
        synchronized(this){
            try{
                System.out.println(this.getClass().getName() + " Sleep之前当前时间："+System.currentTimeMillis());
                Thread.sleep(5*1000);
                System.out.println(this.getClass().getName() +  " Sleep 之后时间："+System.currentTimeMillis());
            }
            catch(Exception e){
                System.out.println(e);
            }
        }
    }

    public void mWait(){
        synchronized(this){
            System.out.println(this.getClass().getName() +  " Wait 。结束时间："+System.currentTimeMillis());

        }
    }
    
    
    public static void main(String[] args) {
        
        Service2 mService = new Service2();

        Thread sleepThread = new Thread(new SleepThread(mService));
        Thread waitThread = new Thread(new WaitThread(mService));
        sleepThread.start();
        waitThread.start();
    }
}
