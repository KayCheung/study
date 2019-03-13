package com.housaire.reactor1;

import java.io.IOException;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Objects;

/**
 * @author <a href="mailto:cheungkay@sina.com">张凯</a>
 * @description:
 * @date 2019/3/12 14:53
 * @see
 * @since 1.0.0
 */
public class Acceptor implements Runnable
{
    private ServerSocketChannel serverSocketChannel;

    private Selector selector;

    public Acceptor(ServerSocketChannel serverSocketChannel, Selector selector)
    {
        this.serverSocketChannel = serverSocketChannel;
        this.selector = selector;
    }

    @Override
    public void run()
    {
        try
        {
            /*
             * ServerSocketChannel可以设置成非阻塞模式。
             * 在非阻塞模式下，accept() 方法会立刻返回，如果还没有新进来的连接,返回的将是null。
             * 因此，需要检查返回的SocketChannel是否是null.
             */
            System.out.println("新的连接接入...");
            SocketChannel socketChannel = serverSocketChannel.accept();
            /*
             * 调用Handler来处理channel
             * 在SocketReadHandler构造方法中将socketChannel register到Selector,返回selectionKey
             * 再将该socketChannel的selectionKey attach(this); this represent new出来的SocketReadHandler
             */
            if (!Objects.isNull(socketChannel))
            {
                new SocketReadWriteHandler(this.selector, socketChannel);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
