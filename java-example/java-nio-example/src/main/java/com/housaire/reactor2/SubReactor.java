package com.housaire.reactor2;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

/**
 * @author <a href="mailto:zhangkai@chinayie.com">张凯</a>
 * @description:
 * @date 2019/3/13 16:13
 * @see
 * @since 1.0.0
 */
public class SubReactor extends Reactor
{

    // 子Selector，主要监听所有成功连接的客户端套接口的读事件
    private Selector selector;

    private CountDownLatch openSelector = new CountDownLatch(1);

    public synchronized void register(SocketChannel socketChannel) throws IOException
    {
        if (Objects.isNull(selector))
        {
            selector = Selector.open();
        }
        socketChannel.configureBlocking(false);
        // 将新的套接口SocketChannel注册到选择器Selector上
        SelectionKey selectionKey = socketChannel.register(selector, SelectionKey.OP_READ);
        // 添加附加对象
        selectionKey.attach(new SocketReadHandler(socketChannel));
        openSelector.countDown();
    }

    @Override
    protected Selector getSelector()
    {
        try
        {
            openSelector.await();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        return selector;
    }

}
