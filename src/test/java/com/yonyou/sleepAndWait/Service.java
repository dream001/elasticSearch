package com.yonyou.sleepAndWait;

public class Service {

    public void mSleep(){
        synchronized (this){
            try{
                System.out.println(this.getClass().getName()+"-- mSleep start");
                Thread.sleep(10000);
                this.notifyAll();
                System.out.println(this.getClass().getName() + " 唤醒等待 。 结束时间："+System.currentTimeMillis());
            }catch(Exception e){
                e.getStackTrace();
            }
            
        }
    }
    
    
    public void mWait(){
        synchronized (this) {
            try{
                System.out.println(this.getClass().getName() + " 等待开始 。 当前时间："+System.currentTimeMillis());
                this.wait();
                //this.notifyAll();
            }catch(Exception e){
                e.getStackTrace();
            }
        }
    }
    
    
    
    
    
    public static void main(String[] args) {
        
        Service2 mService = new Service2();
        
        Thread sleepThread = new Thread(new SleepThread(mService));
        Thread waitThread = new Thread(new WaitThread(mService));
        
        waitThread.start();
        sleepThread.start();
        
        
        
    }
    
    
    
}
