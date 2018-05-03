//package com.yonyou.io;
//
//import java.nio.ByteBuffer;
//
//
///**
// * 几个结构，是从  Buffer中继承过来的
// * capacity ： 这个Buffer最多能放多少数据。capacity在buffer被创建的时候指定
// * limit：在Buffer上进行的读写操作都不能越过这个下标。
// * 当写数据到buffer中时，limit一般和capacity相等，
// * 当读数据时，limit代表buffer中有效数据的长度
// * 
// * position：读/写操作的当前下标。
// * 当使用buffer的相对位置进行读/写操作时，读/写会从这个下标进行，
// * 并在操作完成后，buffer会更新下标的值
// * 
// * mark：一个临时存放的位置下标。调用mark()会将mark设为当前的position的值，
// * 以后调用reset()会将position属性设置为mark的值。mark的值总是小于等于position的值，
// * 如果将position的值设的比mark小，当前的mark值会被抛弃掉
// * 
// * @ClassName: ByteBufferTest
// * @Description: TODO(类简要描述，必须以句号为结束)
// * @author caozq
// * @date 2018年4月24日
// */
//public class ByteBufferTest {
//
//    /**
//     * 基本结构
//     */
//    private static  int position = 0;  
//    private static  int limit ;
//    private static  int capacity;  
//    private static  int mark = -1;  
//    
//    /**
//     * 注意  flip  前后   limit
//     * @Title: main
//     * @Description: TODO(方法简要描述，必须以句号为结束)
//     * @author: caozq
//     * @since: (开始使用的版本)
//     * @param args
//     */
//    public static void main(String[] args) {
//       //实例初始化  
//        ByteBuffer buffer = ByteBuffer.allocate(100);  
//        String value ="Netty";  
//        buffer.put(value.getBytes()); 
//        /**
//         * 将 limit = position;
//         * position = 0;
//         */
//        buffer.flip();  
//        byte[] vArray = new byte[buffer.remaining()];  
//        buffer.get(vArray);  
//        System.out.println(new String(vArray)); 
//
//    }
//    
//    /**
//     * 一般在把数据写入Buffer前调用
//     * @Title: clear
//     * @Description: TODO(方法简要描述，必须以句号为结束)
//     * @author: caozq
//     * @since: (开始使用的版本)
//     */
//    
//    public static void clear(){
//        position = 0;  
//        limit = capacity;  
//        mark = -1;  
//    }
//    
//    /**
//     * 一般在从Buffer读出数据前调用
//     * @Title: flip
//     * @Description: TODO(方法简要描述，必须以句号为结束)
//     * @author: caozq
//     * @since: (开始使用的版本)
//     */
//    public final void flip() {  
//        limit = position;  
//        position = 0;  
//        mark = -1;  
//    } 
//    
//    /**
//     * 重新读取的时候调用
//     * @Title: rewind
//     * @Description: TODO(方法简要描述，必须以句号为结束)
//     * @author: caozq
//     * @since: (开始使用的版本)
//     */
//    public final void rewind() {  
//        position = 0;  
//        mark = -1;  
//    } 
//    
//    
//    /**
//     * HeapByteBuffer和DirectByteBuffer的总结：前者是内存的分派和回收速度快，
//     * 可以被JVM自动回收，缺点是如果进行Socket的I/O读写，需要额外做一次内存拷贝，
//     * 将堆内存对应的缓存区复制到内核中，性能会有一定程序的下降；后者非堆内存，
//     * 它在堆外进行内存的分配，相比堆内存，它的分配和回收速度会慢一些，
//     * 但是它写入或者从Socket Channel中读取时，由于少了一次内存复制，速度比堆内存快。
//     * 经验表明，最佳实践是在I/O通信线程的读写缓冲区使用DirectByteBuffer，后端业务消息
//     * 的编码模块使用HeapByteBuffer，这样的组合可以达到性能最优。
//     * 
//     * 
//     * @Title: allocate
//     * @Description: TODO(方法简要描述，必须以句号为结束)
//     * @author: caozq
//     * @since: (开始使用的版本)
//     * @param capacity
//     * @return
//     */
//    public static ByteBuffer allocate(int capacity) {  
//        if (capacity < 0)  
//            throw new IllegalArgumentException();  
//        return new HeapByteBuffer(capacity, capacity);  
//    } 
//    
//    public static ByteBuffer allocateDirect(int capacity) {  
//        return new DirectByteBuffer(capacity);  
//    }
//}
