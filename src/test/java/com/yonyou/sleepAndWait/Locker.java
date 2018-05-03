package com.yonyou.sleepAndWait;

public class Locker {

    private static String locker = "";

    public static void main(String[] args) {
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    // 等待主线程获取锁
                    System.out.println("@@@@@" + this.getClass().getName() + System.currentTimeMillis());
                    Thread.sleep(3000);
                    System.out.println(this.getClass().getName() + System.currentTimeMillis());

                    // 请求locker对象的内部锁
                    synchronized (locker) {
                        System.out.println("Get locker");

                    }
                } catch (InterruptedException e) {
                    // TODO: handle exception
                }
            }
        }, "Blocked Thread");
        thread.start();

        // 请求locker内部对象锁
        synchronized (locker) {
            // 始终持有locker对象的内部锁
            while (true) {
                ;;
            }
        }

    }
}
