package com.housaire.reactor1;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * @author <a href="mailto:zhangkai@chinayie.com">张凯</a>
 * @description:
 * @date 2019/3/12 15:04
 * @see
 * @since 1.0.0
 */
public class SocketReadHandler implements Runnable
{

    private SocketChannel socketChannel;

    public SocketReadHandler(Selector selector, SocketChannel socketChannel) throws IOException
    {
        this.socketChannel = socketChannel;
        this.socketChannel.configureBlocking(false);
        SelectionKey selectionKey = this.socketChannel.register(selector, SelectionKey.OP_READ);
        //同时将SelectionKey标记为可读，以便读取。
        selectionKey.interestOps(SelectionKey.OP_READ);
        //将该socketChannel注册的SelectionKey绑定为本SocketReadHandler
        //下一步有事件触发时，将调用本类的run方法。
        //参看dispatch(SelectionKey key)
        selectionKey.attach(this);
        selector.wakeup();
    }

    /**
     * 处理读取数据
     */
    @Override
    public void run()
    {
        System.out.println("客户端数据写入...");
        ByteBuffer byteBuffer = ByteBuffer.allocate(8192);
        try
        {
            this.socketChannel.read(byteBuffer);
            // 激活线程池 处理这些request
            // requestHandle(new Request(socket,btt));
            ExecutionHandler.getExecutionHandler().execute(byteBuffer, () -> {
                try
                {
                    System.out.println("回写客户端...");
                    this.socketChannel.write(ByteBuffer.wrap("收到了你的请求".getBytes("UTF-8")));
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            });
        }
        catch (IOException e)
        {
            try
            {
                this.socketChannel.close();
            }
            catch (IOException e1)
            {
                e1.printStackTrace();
            }
        }
    }
}
