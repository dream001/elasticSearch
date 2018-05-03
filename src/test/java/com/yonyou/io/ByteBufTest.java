package com.yonyou.io;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;


/**
 * 总结：
 * 1.ByteBuffer必须自己长度固定，一旦分配完成，它的容量不能动态扩展和收缩；
 * ByteBuf默认容器大小为256，支持动态扩容，在允许的最大扩容范围内（Integer.MAX_VALUE）。
 * 2.ByteBuffer只有一个标识位置的指针，读写的时候需要手动的调用flip()和rewind()等，
 * 否则很容易导致程序处理失败。而ByteBuf有两个标识位置的指针，一个写writerIndex，
 * 一个读readerIndex,读写的时候不需要调用额外的方法。
 * 3.NIO的SocketChannel进行网络读写时，操作的对象是JDK标准的java.nio.byteBuffer。
 * 由于Netty使用统一的ByteBuf替代JDK原生的java.nio.ByteBuffer，
 * 所以ByteBuf中定义了ByteBuffer nioBuffer()方法将ByteBuf转换成ByteBuffer。
 * @ClassName: ByteBufTest
 * @Description: TODO(类简要描述，必须以句号为结束)
 * @author caozq
 * @date 2018年4月24日
 */
public class ByteBufTest {

    public static void main(String[] args) {

        // 实例初始化
        ByteBuf buffer = Unpooled.buffer(100);
        String value = "学习ByteBuf";
        buffer.writeBytes(value.getBytes());
        System.out.println("获取readerIndex:" + buffer.readerIndex());
        System.out.println("获取writerIndex:" + buffer.writerIndex());

        byte[] vArray = new byte[buffer.writerIndex()];
        buffer.readBytes(vArray);
        System.out.println("获取readerIndex:" + buffer.readerIndex());
        System.out.println("获取writerIndex:" + buffer.writerIndex());
        System.out.println(new String(vArray));
    }

}
