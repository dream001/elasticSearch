package com.yonyou.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 概念：
 * Channels and Buffers（通道和缓冲区）：
 * 标准的IO基于字节流和字符流进行操作的，而NIO是基于通道（Channel）和缓冲区（Buffer）
 * 进行操作，数据总是从通道读取到缓冲区中，或者从缓冲区写入到通道中
 * Asynchronous IO（异步IO）：
 * Java NIO可以让你异步的使用IO，例如：当线程从通道读取数据到缓冲区时，线程还是可以进行
 * 其他事情。当数据被写入到缓冲区时，线程可以继续处理它。从缓冲区写入通道也类似。
 * Selectors（选择器）：
 * Java NIO引入了选择器的概念，选择器用于监听多个通道的事件（比如：连接打开，数据到达）。
 * 因此，单个的线程可以监听多个数据通道。
 * 使用场景：
 * NIO
 * 优势在于一个线程管理多个通道；但是数据的处理将会变得复杂；
 * 如果需要管理同时打开的成千上万个连接，这些连接每次只是发送少量的数据，采用这种
 * 传统的IO
 * 适用于一个线程管理一个通道的情况；因为其中的流数据的读取是阻塞的；
 * 如果需要管理同时打开不太多的连接，这些连接会发送大量的数据；
 * Channel:FileChannel,DatagramChannel,SocketChannel,ServerSocketChannel
 * Buffer:ByteBuffer,CharBuffer,DoubleBuffer,FloatBuffer,IntBuffer,LongBuffer,ShortBuffer
 * Selector:
 * @ClassName: Concept
 * @Description: TODO(类简要描述，必须以句号为结束)
 * @author caozq
 * @date 2018年4月24日
 */
public class Concept {

    private static String path;
    
    static{
        path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
    }

    public static void test1() {
        RandomAccessFile aFile = null;
        try {
            aFile = new RandomAccessFile(path + "data/nio-data.txt", "rw");
            FileChannel inChannel = aFile.getChannel();  // 从一个InputStream outputstream中获取channel
            // create buffer with capacity of 48 bytes
            ByteBuffer buf = ByteBuffer.allocate(48);
            int bytesRead = inChannel.read(buf); // read into buffer.
            while (bytesRead != -1) {
                buf.flip();  // make buffer ready for read
                while (buf.hasRemaining()) {
                    System.out.print((char) buf.get()); // read 1 byte at a time
                }
                buf.clear(); // make buffer ready for writing
                bytesRead = inChannel.read(buf);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                aFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    
    
    
    
    
    /**
     * InputStream 或者Reader
     * 每一次都一行都需要判断是否全部读取，都有一个阻塞。
     * 一旦读取之后就不知道他之前的东西了，有一个覆盖的操作
     * 
     * 
     * @Title: test2
     * @Description: TODO(方法简要描述，必须以句号为结束)
     * @author: caozq
     * @since: (开始使用的版本)
     */
    private static void test2(){
        File file = new File(path + "data/nio-data.txt");
        InputStream input = new InputStream() {
            @Override
            public int read() throws IOException {
                // TODO Auto-generated method stub
                return 0;
            }
        };

        BufferedReader reader = new BufferedReader(new InputStreamReader(input));   

        try {
            String nameLine   = reader.readLine();
            String ageLine    = reader.readLine(); 
            String emailLine  = reader.readLine(); 
            String phoneLine  = reader.readLine();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
        
    }

    public static void main(String[] args) {
        test1();

    }

}
