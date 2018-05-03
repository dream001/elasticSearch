package com.yonyou.io;

import java.util.Iterator;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;

/**
 * netty 中ByteBuf 的类型：
 * Heap Buffer 堆缓冲区：ByteBuf将数据存储在JVM的堆空间，通过将数据存储在数组中实现的
 * 优点是：由于数据存储在JVM的堆中可以快速创建和快速释放，并且提供了数组的直接快速访问的方法
 * 缺点是：每次读写数据都要先将数据拷贝到直接缓冲区再进行传递
 * Direct Buffer 直接缓冲区：在堆之外直接分配内存，直接缓冲区不会占用堆的容量
 * 优点是：在使用Socket传递数据时性能很好，由于数据直接在内存中，不存在从JVM拷贝数据到直接缓冲区的过程，性能好
 * 缺点是：因为Direct Buffer是直接在内存中，所以分配内存空间和释放内存比堆缓冲区更复杂和慢
 * Composite Buffer 复合缓冲区：Netty提供了Composite ByteBuf来处理复合缓冲区。
 * 例如：一条消息由Header和Body组成，将header和body组装成一条消息发送出去
 * 复合缓冲区就类似于一个ByteBuf的组合视图，在这个视图里面我们可以创建不同的ByteBuf
 * (可以是不同类型的)。 这样，复合缓冲区就类似于一个列表，我们可以动态的往里面添加和
 * 删除其中的ByteBuf，JDK里面的ByteBuffer就没有这样的功能
 * ByteBuf 与JDK中的 ByteBuffer 的最大区别之一就是：
 * 1）netty的ByteBuf采用了读/写索引分离，一个初始化的ByteBuf的readerIndex和writerIndex都处于0位置
 * 2）当读索引和写索引处于同一位置时，如果我们继续读取，就会抛出异常IndexOutOfBoundsException
 * 3）对于ByteBuf的任何读写操作都会分别单独的维护读索引和写索引。maxCapacity最大容量默认的限制就是Integer.MAX_VALUE
 * @ClassName: NettyTest
 * @Description: TODO(类简要描述，必须以句号为结束)
 * @author caozq
 * @date 2018年4月24日
 */
public class NettyTest {


    public static void test() {
        // 组合缓冲区
        CompositeByteBuf compBuf = Unpooled.compositeBuffer();
        // 堆缓冲区
        ByteBuf heapBuf = Unpooled.buffer(8);
        // 直接缓冲区
        ByteBuf directBuf = Unpooled.directBuffer(16);
        // 添加ByteBuf到CompositeByteBuf
        compBuf.addComponents(heapBuf, directBuf);
        // 删除第一个ByteBuf
        compBuf.removeComponent(0);
        Iterator<ByteBuf> iter = compBuf.iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next().toString());
        }

        // 使用数组访问数据
        if (!compBuf.hasArray()) {
            int len = compBuf.readableBytes();
            byte[] arr = new byte[len];
            compBuf.getBytes(0, arr);
        }
    }


    /**
     * 随机访问索引
     * @Title: RandWR
     * @Description: TODO(方法简要描述，必须以句号为结束)
     * @author: caozq
     * @since: (开始使用的版本)
     */
    private static void RandWR() {
        // 创建一个16字节的buffer,这里默认是创建heap buffer
        ByteBuf buf = Unpooled.buffer(16);
        // 写数据到buffer
        for (int i = 0; i < 16; i++) {
            buf.writeByte(i + 1);
        }
        // 读数据
        for (int i = 0; i < buf.capacity(); i++) {
            System.out.print(buf.getByte(i) + ", ");
        }
    }
    
    
    /**
     * ByteBuffer的字节数组是被定义成final的，也就是长度固定。
     * 一旦分配完成就不能扩容和收缩，灵活性低，而且当待存储的对象字节很大可能出现数组越界，
     * 用户使用起来稍不小心就可能出现异常。如果要避免越界，在存储之前就要只要需求字节大小，
     * 如果buffer的空间不够就创建一个更大的新的ByteBuffer，再将之前的Buffer中数据复制过去，这样的效率是奇低的
     * ByteBuffer只用了一个position指针来标识位置，读写模式切换时需要调用flip()函数
     * 和rewind()函数，使用起来需要非常小心，不然很容易出错误
     * 
     * 
     * ByteBuf是吸取ByteBuffer的缺点之后重新设计，存储字节的数组是动态的，最大是Integer
     * .MAX_VALUE。这里的动态性存在write操作中，write时得知buffer不够时，会自动扩容
     * ByteBuf的读写索引分离，使用起来十分方便。此外ByteBuf还新增了很多方便实用的功能
     * 
     * 
     * @Title: compareByteBufAndByteBuffer
     * @Description: TODO(方法简要描述，必须以句号为结束)
     * @author: caozq
     * @since: (开始使用的版本)
     */
    private static void compareByteBufAndByteBuffer(){
        
    }


    public static void main(String[] args) {
        test();
    }

}
