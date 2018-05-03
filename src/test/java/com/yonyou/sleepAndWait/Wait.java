package com.yonyou.sleepAndWait;

public class Wait {

    private static String locker = "";
    
    public static void main(String[] args) {
        
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    synchronized (locker) {
                        System.out.println("wait locker");
                        locker.wait();
                    }
                } catch (InterruptedException e) {
                    // TODO: handle exception
                }

            }
        }, "Blocked Thread");
        thread.start();

        // 等待Blocked线程执行wait方法
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // 获取locker对象锁
        synchronized (locker) {
            System.out.println("stay locker");
            // 保持锁
            while (true)
                ;
        }

    }

}
