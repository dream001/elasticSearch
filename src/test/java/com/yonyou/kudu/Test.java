package com.yonyou.kudu;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Test {


    private int value;

    public final int getValue() {
        return value;
    }


    /**
     * cas 处理
     * 处理器自动保证基本内存操作的原子性 ，一个字节的处理可以实现，复杂的东西没法保证比如跨总线宽度，跨多个缓存行，跨页表的访问
     * 使用总线锁保证原子性：使用处理器提供的一个LOCK＃信号
     * 使用缓存锁保证原子性 ：某一个时刻对某个内存地址只能有一个线程访问
     * 存在的问题：
     * aba 问题 ： 增加一个版本号
     * 循环时间太长：如果JVM能支持处理器提供的pause指令那么效率会有一定的提升
     * 只能保证一个共享变量的原子操作：
     * concurrent包的实现：
     * long和double类型变量的非原子性 32位机器都不是原子操作
     * 和jvm的位数有关系
     * volatile本身不保证获取和设置操作的原子性，仅仅保持修改的可见性
     * 缓存一致性问题：多个线程对同一个数据进行处理的时候会出现：最出名的就是Intel 的MESI协议
     * MESI协议保证了每个缓存中使用的共享变量的副本是一致的。它核心的思想是：当CPU写数据时，
     * 如果发现操作的变量是共享变量，即在其他CPU中也存在该变量的副本，会发出信号通知其他CPU
     * 将该变量的缓存行置为无效状态，因此当其他CPU需要读取这个变量时，发现自己缓存中缓存该变量的缓存
     * 行是无效的，那么它就会从内存重新读取
     * ******内存和缓存的不一致性问题*******
     * 原子性：long 的高位和低位问题
     * 可见性：多个线程访问同一个变量，一个修改了 其他的第一时间就可以
     * 有序性：指令重排和指令挡板
     * 注意，为了获得较好的执行性能，Java内存模型并没有限制执行引擎使用处
     * 理器的寄存器或者高速缓存来提升指令执行速度，也没有限制编译器对指令进行
     * 重排序。也就是说，在java内存模型中，也会存在缓存一致性问题和指令重排序的
     * 问题
     * Java内存模型规定所有的变量都是存在主存当中（类似于前面说的物理内
     * 存），每个线程都有自己的工作内存（类似于前面的高速缓存）。线程对
     * 变量的所有操作都必须在工作内存中进行，而不能直接对主存进行操作。
     * 并且每个线程不能访问其他线程的工作内存。
     * java 本身对原子性 可见性 有序性的操作
     * 原子性：只有赋值和获取值，其他的都不是严格意义的 可以通过synchronized和Lock来实现
     * 可见性：Java提供了volatile关键字来保证可见性 可以通过synchronized和Lock来实现
     * 有序性：编译器和指令器对执行重排 有一个happens-before 8条原则
     * 程序的顺序
     * 锁定原则 lock unlock
     * 读写原则 write read
     * 传递原则 a-b-c a-c
     * 线程启动规则
     * 线程中断规则
     * 线程终结规则：线程中所有的操作都先行发生于线程的终止检测，我们可以通过
     * Thread.join()方法结束、Thread.isAlive()的返回值手段检测到线程已经终止执行
     * 对象终结规则：一个对象的初始化完成先行发生于他的finalize()方法的开始
     * volatile :
     * 1)可见性 ： 会将值强制写入主存 会导致其他线程中缓存行无效
     * Lock synchronized AtomicInteger 三种方式实现原子性
     * 2)禁止指令重排，一定程度上面 读写操作 执行顺序
     * 4.volatile的原理和实现机制
     * 观察加入volatile关键字和没有加入volatile关键字时所生成的汇编代码发现，
     * 加入volatile关键字时，会多出一个lock前缀指令（内存屏障）
     * 
     * 
     * 
     * Lock 和 Synchronized 的比较
     * 
     * 
     * 
     * @Title: incrementAndSet
     * @Description: TODO(方法简要描述，必须以句号为结束)
     * @author: caozq
     * @since: (开始使用的版本)
     * @return
     */
    public final int incrementAndSet() {

        for (;;) {
            int currentVal = getValue();
            int nextVal = currentVal + 1;
            if (compareAndSet(currentVal, nextVal)) return nextVal;
        }
    }

    public final boolean compareAndSet(int expect, int update) {
        return true;
    }


    private Object instance = null;
    private static final int _1MB = 1024 * 1024;
    private byte[] bigSize = new byte[2 * _1MB];


    public static void main(String[] args) {
        // TODO Auto-generated method stub
        System.out.println(System.getProperty("java.vm.name"));
        System.out.println(nn());


        Test objectA = new Test();
        Test objectB = new Test();
        objectA.instance = objectB;
        objectB.instance = objectA;
        objectA = null;
        objectB = null;

        System.gc();
        
        



    }


    public volatile int a = 0;


    public int add(int a) {
        Lock lock = new ReentrantLock();
        lock.lock();
        try {
            a++;
        } catch (Exception e) {
            e.getStackTrace();
        } finally {
            lock.unlock();
        }
        return a;

    }

    public static String nn() {

        Lock lock = new ReentrantLock();
        lock.lock();

        try {
            String a = Thread.currentThread().getStackTrace()[1].getMethodName();
            return a;
        } catch (Exception e) {
            e.getStackTrace();
        } finally {
            lock.unlock();
        }


        return "";


    }

}
