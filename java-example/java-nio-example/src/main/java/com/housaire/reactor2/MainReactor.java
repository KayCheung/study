package com.housaire.reactor2;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author <a href="mailto:cheungkay@sina.com">张凯</a>
 * @description:
 * @date 2019/3/13 16:34
 * @see
 * @since 1.0.0
 */
public class MainReactor extends Reactor
{
    private static final Logger log = Logger.getLogger(MainReactor.class.getName());

    // 主Selector，主要监听服务端套接口新客户端的连接事件
    private Selector selector;

    private ServerSocketChannel serverSocketChannel;

    public MainReactor(int port) throws IOException
    {

        // 创建选择器Selector，open方法并非单例的(每次调用open都会产生一个新的Selector)
        // 用来监听一个或多个套接口的状态
        selector = Selector.open();
        // 创建套接字服务端通道 ServerSocketChannel
        serverSocketChannel = ServerSocketChannel.open();
        // 设置为非阻塞模式，
        serverSocketChannel.configureBlocking(false);
        // 将套接口绑定到指定的端口
        serverSocketChannel.bind(new InetSocketAddress(port));
        // 将套接口注册到多路复用选择器上Selector, 并指定感兴趣的事件(对所有连接到该套接口的客户端进行监听。)
        // 返回注册在Selector上的SelectionKey(SelectionKey可以理解为套接口注册在Selector上的一个事件)
        // 调用注册方法时，会首先判断当前套接口是否已经注册，如果已经注册则直接返回
        // 否则，会生成一个新的SelectionKey，并将这个SelectionKey添加到Selector的fd列表中
        // 执行Selector.select方法时，会将Selector中fd列表的所有fd发送至内核
        // 内核处理完返回后，Selector.select方法会对返回的事件进行筛选，将符合的事件添加到SelectionKey集合中
        // 接着通过ServerSocketChannel.accept方法接收新进来的SocketChannel（会创建一个新的FD）
        SelectionKey selectionKey = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        // 设置附加对象
        selectionKey.attach(new Acceptor(5));
    }

    @Override
    protected Selector getSelector()
    {
        return selector;
    }

    private class Acceptor implements Runnable
    {

        private final SubReactor[] subReactors;

        private final AtomicInteger i;

        public Acceptor(int num)
        {
            i = new AtomicInteger();
            subReactors = new SubReactor[num];
            // 定义线程池来处理所有注册在子Selector上的套接口
            ExecutorService executorService = Executors.newFixedThreadPool(num);
            for (int i = 0; i < num; i++)
            {
                subReactors[i] = new SubReactor();
                executorService.execute(subReactors[i]);
            }
        }

        @Override
        public void run()
        {
            try
            {
                // 通过ServerSocketChannel.accept方法接收新进来的SocketChannel（会创建一个新的FD）
                SocketChannel socketChannel = serverSocketChannel.accept();
                if (!Objects.isNull(socketChannel))
                {
                    log.info("新的连接接入...");
                    SubReactor subReactor = subReactors[getAndIncrement() % subReactors.length];
                    subReactor.register(socketChannel);
                }
            }
            catch (IOException e)
            {
                log.log(Level.SEVERE, "网络IO异常", e);
            }
        }

        private int getAndIncrement() {
            for (; ; ) {
                int current = i.get();
                int next = (current >= Integer.MAX_VALUE ? 0 : current + 1);
                if (i.compareAndSet(current, next)) {
                    return current;
                }
            }
        }

    }

    public static void main(String[] args) throws IOException {
        new Thread(new MainReactor(19090)).start();
    }

}
