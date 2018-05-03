package com.yonyou.cas;

import java.util.concurrent.atomic.AtomicReference;

/**
 * CAS: compare and swap 一种乐观锁的形式
 * 
 * 内存 A 期望原始数据B 更新数据c a== b 的时候更新C
 * 
 * 存在的问题：
 * ABA 的问题： 需要在增加 版本号的限制
 * 
 * 循环时间开销太大：
 * 
 * 只能保证一个共享变量的原子操作:
 * 
 * 自旋CAS如果长时间不成功，会给CPU带来非常大的执行开销。
 * 当对一个共享变量执行操作时，我们可以使用循环CAS的方式来保证原子操作，
 * 但是对多个共享变量操作时，循环CAS就无法保证操作的原子性，这个时候就可以用锁，
 * 或者有一个取巧的办法，就是把多个共享变量合并成一个共享变量来操作。
 * 比如有两个共享变量i＝2,j=a，合并一下ij=2a，然后用CAS来操作ij。
 * 从Java1.5开始JDK提供了AtomicReference类来保证引用对象之间的原子性，
 * 你可以把多个变量放在一个对象里来进行CAS操作。
 * 
 * 
 * @ClassName: CasTest
 * @Description: TODO(类简要描述，必须以句号为结束)
 * @author caozq
 * @date 2018年4月12日
 */
public class CasTest {

    public static void main(String[] args) {
        // TODO Auto-generated method stub

        AtomicReference ac = new AtomicReference();
        
    }
    
    
    /**
     * 
     * @Title: classLoad
     * @Description: TODO(方法简要描述，必须以句号为结束)
     * @author: caozq
     * @since: (开始使用的版本)
     * @param flag
     * @return
     */
    private boolean classLoad(boolean flag){
        return flag;
    }
    
    
    /**
     * 类从被加载到虚拟机内存中开始，到卸载出内存为止，它的整个生命周期包括：
     * 加载、验证、准备、解析、初始化、使用和卸载七个阶段。
     * 其中类加载的过程包括了加载、验证、准备、解析、初始化五个阶段。
     * 
     * 
     * 
     * @Title: classLoadProcess
     * @Description: TODO(方法简要描述，必须以句号为结束)
     * @author: caozq
     * @since: (开始使用的版本)
     * @return
     */
    private boolean classLoadProcess(){
        return true;
    }
    
    
    /**
     * jvm  与  jmm 的东西
     * https://blog.csdn.net/lzn51/article/details/71799189
     */
    
    
    
    /**
     * AQS 最好源码解析，没有更好
     * https://www.cnblogs.com/waterystone/p/4920797.html
     * 
     * 抽象队列同步器
     * 
     * ReentrantLock 互斥锁的使用
     * Semaphore    
     * CountDownLatch  并发机制，同步队列
     * 
     * 
     * 主要是有一个  state 状态和 阻塞队列
     */
    
    
    
    /**
     * 
     * Thread  状态转换的详细解析
     * http://www.cnblogs.com/waterystone/p/4920007.html
     * 
     * 就绪  阻塞  运行   完成
     * 
     * sleep 交出CPU，让其做其他的事情，但是不释放锁，其他线程无法访问该对象。
     * 当时间结束进入就绪状态，是一个static  不能对对象加锁。
     * 
     * yield 交出CPU 让其执行其他事情，不释放锁，只能让相同优先级的进程获取CPU
     * 
     * wait:Object，进入和该线程相关的等待池，同时释放锁，等待其他线程的notify notifyall
     * 
     * 
     * join:利用wait() 结束条件：1)时间到了 2)目标函数执行完成
     * interrupt：会对等待中的线程有影响，正在执行中的线程没有影响
     * suspend()/resume()：挂起线程，直到被resume，才会苏醒。jdk7 已不建议使用，
     * 会产生死锁问题
     * 
     * 阻塞 ： 不需要別人参与，有JVM调度器来唤醒自己，不响应中断
     * 等待： 需要别人的唤醒   wait  join 
     */

}
