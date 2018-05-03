package com.yonyou.syn;

/**
 * 使用方法： object class method
 * 
 * synchronized底层语义原理
 * Java 虚拟机中的同步(Synchronization)基于进入和退出管程(Monitor)对象实现， 
 * 无论是显式同步(有明确的 monitorenter 和 monitorexit 指令,即同步代码块)还是隐式同步都是如此。
 * 在 Java 语言中，同步用的最多的地方可能是被 synchronized 修饰的同步方法。
 * 同步方法 并不是由 monitorenter 和 monitorexit 指令来实现同步的，
 * 而是由方法调用指令读取运行时常量池中方法的 ACC_SYNCHRONIZED 标志来隐式实现的.
 * 
 * 
 * 
 * https://blog.csdn.net/javazejian/article/details/72828483#synchronized底层语义原理
 * 理解Java对象头与Monitor:
 * 
 * 在JVM中，对象在内存中的布局分为三块区域：对象头、实例数据和对齐填充
 * 实例变量：存放类的属性数据信息，包括父类的属性信息，如果是数组的实例部分还包括数组的长度，
 * 这部分内存按4字节对齐
 * 
 * 填充数据：由于虚拟机要求对象起始地址必须是8字节的整数倍。填充数据不是必须存在的，
 * 仅仅是为了字节对齐，这点了解即可
 * 
 * 而对于顶部，则是Java头对象，它实现synchronized的锁对象的基础，这点我们重点分析它，一般而言，
 * synchronized使用的锁对象是存储在Java对象头里的，jvm中采用2个字来存储对象头(如果对象是数组
 * 则会分配3个字，多出来的1个字记录的是数组长度)，其主要结构是由Mark Word 和 
 * Class Metadata Address 组成
 * 
 * Mark Word:存储对象的hashCode、锁信息或分代年龄或GC标志等信息
 * Class Metadata Address:类型指针指向对象的类元数据，JVM通过这个指针确定该对象是哪个类的实例
 * 
 * 
 * 

 * javap -c  com.paddx.test.concurrent   可以反编译看到结果
 * 
 * 代码块同步原理：
 * monitorenter：  管程
 * 每个对象有一个监视器锁（monitor）。当monitor被占用时就会处于锁定状态，线程执行monitorenter指令
 * 时尝试获取monitor的所有权，过程如下：
 * 1、如果monitor的进入数为0，则该线程进入monitor，然后将进入数设置为1，该线程即为monitor的所有者。
 * 2、如果线程已经占有该monitor，只是重新进入，则进入monitor的进入数加1.
 * 3.如果其他线程已经占用了monitor，则该线程进入阻塞状态，直到monitor的进入数为0，重新尝试获取monitor的所有权。
 * 
 * monitorexit：　
 * 执行monitorexit的线程必须是objectref所对应的monitor的所有者。
 * 指令执行时，monitor的进入数减1，如果减1后进入数为0，那线程退出monitor，
 * 不再是这个monitor的所有者。其他被这个monitor阻塞的线程可以尝试去获取这个 monitor 的所有权。
 * 
 * 
 * 同步方法原理：
 * JVM就是根据该标示符来实现方法的同步的：当方法调用时，
 * 调用指令将会检查方法的 ACC_SYNCHRONIZED 访问标志是否被设置，如果设置了，
 * 执行线程将先获取monitor，获取成功之后才能执行方法体，方法执行完后再释放monitor。
 * 在方法执行期间，其他任何线程都无法再获得同一个monitor对象。 其实本质上没有区别，
 * 只是方法的同步是一种隐式的方式来实现，无需通过字节码来完成
 * 
 * 
 * 
 * @ClassName: Test
 * @Description: TODO(类简要描述，必须以句号为结束)
 * @author caozq
 * @date 2018年4月21日
 */
public class Test {

    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

}
