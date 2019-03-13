package com.housaire.reactor1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Objects;
import java.util.Set;

/**
 * @author <a href="mailto:cheungkay@sina.com">张凯</a>
 * 经典的网络服务在每个线程中完成对数据的处理：
 * 但这种模式在用户负载增加时，性能将下降非常的快。
 * 系统运行的性能瓶颈通常在I/O读写，包括对端口和文件的操作上，过去，在打 开一个I/O通道后，
 * read()将一直等待在端口一边读取字节内容，如果没有内容进来，read()也是傻傻的等，
 * 这会影响我们程序继续做其他事情，那 么改进做法就是开设线程，让线程去等待，但是这样做也是相当耗费资源（传统socket通讯服务器设计模式） 的。
 *
 * Java NIO非堵塞技术实际是采取Reactor模式，或者说是Observer模式为我们监察I/O端口，
 * 如果有内容进来，会自动通知我们，这样，我们就不必开启多个线程死等，从外界看，实现了流畅的I/O读写，不堵塞了。
 * NIO 有一个主要的类Selector,这个类似一个观察者 ，只要我们把需要探知的 socketchannel告诉Selector,
 * 我们接着做别的事情，当有事件发生时，他会通知我们，传回一组SelectionKey,我们读取这些 Key,就会获得我们刚刚注册过的socketchannel,
 * 然后，我们从这个Channel中读取数据，放心，包准能够读到，接着我们可以处理这些数据。
 * Selector内部原理实际是在做一个对所注册的channel的轮询访问 ，不断的轮询(目前就这一个算法)，一旦轮询到一个channel有所注册的事情发生，
 * 比如数据来了，他就会站起来报告，交出一把钥匙，
 * 让我们通过这把钥匙（SelectionKey 表示 SelectableChannel 在 Selector 中的注册的标记。 ）来读取这个channel的内容。
 *
 * 反应器模式
 * 用于解决多用户访问并发问题
 * 举个例子：餐厅服务问题
 * 传统线程池做法：来一个客人(请求)去一个服务员(线程)
 * 反应器模式做法：当客人点菜的时候，服务员就可以去招呼其他客人了，等客人点好了菜，直接招呼一声“服务员”
 * @date 2019/3/12 14:36
 * @see
 * @since 1.0.0
 */
public class Reactor implements Runnable
{
    private ServerSocketChannel serverSocketChannel;

    //同步事件分离器，阻塞等待Handles中的事件发生
    private Selector selector;

    public Reactor(int port) throws IOException
    {
        selector = Selector.open();
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(port));

        /*
         * ServerSocketChannel可以设置成非阻塞模式。
         * 在非阻塞模式下，accept() 方法会立刻返回，如果还没有新进来的连接,返回的将是null。
         * 因此，需要检查返回的SocketChannel是否是null.
         */
        serverSocketChannel.configureBlocking(false);
        /*
         * 向selector注册该serverSocketChannel
         * SelectionKey.OP_ACCEPT —— 接收连接继续事件，表示服务器监听到了客户连接，服务器可以接收这个连接了
         * SelectionKey.OP_CONNECT —— 连接就绪事件，表示客户与服务器的连接已经建立成功
         * SelectionKey.OP_READ —— 读就绪事件，表示通道中已经有了可读的数据，可以执行读操作了（通道目前有数据，可以进行读操作了）
         * SelectionKey.OP_WRITE —— 写就绪事件，表示已经可以向通道写数据了（通道目前可以用于写操作）
         * 这里 注意，下面两种，SelectionKey.OP_READ ，SelectionKey.OP_WRITE ，
         * 1.当向通道中注册SelectionKey.OP_READ事件后，如果客户端有向缓存中write数据，下次轮询时，则会 isReadable()=true；
         * 2.当向通道中注册SelectionKey.OP_WRITE事件后，这时你会发现当前轮询线程中isWritable()一直为ture，如果不设置为其他事件
         */
        SelectionKey selectionKey = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        /*
         * 利用selectionKey的attache功能绑定Acceptor 如果有事情，触发Acceptor
         * 该selectionKey为serverSocketChannel的selectionKey
         * attach的为new Acceptor(this)
         * 用于void dispatch(SelectionKey key)中获取key.attachment()
         * 将被本类中的run()方法的selectionKeys.clear(); 清空
         * 第二次的selector.selectedKeys();获取到的将会是socketChannel注册的OP_READ的selectionKey(attach的为SocketReadHandler)
         */
        selectionKey.attach(new Acceptor(serverSocketChannel, selector));
    }

    @Override
    public void run()
    {
        try
        {
            while (!Thread.interrupted())
            {
                System.err.println(Thread.currentThread().getName() + " 执行Selector.select");
                selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                // Selector如果发现channel有OP_ACCEPT或READ事件发生，下列遍历就会进行。
                for (SelectionKey selectionKey : selectionKeys)
                {
                    /*
                     * 第一次触发此方法，获取(OP_ACCEPT)selectionKey.attachment()为new Acceptor(this)
                     * Acceptor run()方法里面为 new SocketReadHandler(reactor.selector, socketChannel);
                     * 在SocketReadHandler构造方法中将socketChannel register到Selector,返回selectionKey
                     * 再将该socketChannel的selectionKey attach(this); this represent new出来的SocketReadHandler
                     *
                     * 第二次触发此方法，获取(OP_READ)selectionKey.attachment()为new出来的SocketReadHandler
                     * SocketReadHandler run()方法里面为 socketChannel.read(inputBuffer); 实际处理的逻辑代码
                     */
                    System.err.println(Thread.currentThread().getName() + " 处理SelectionKey");
                    dispatch(selectionKey);
                }
                /*
                 * selectionKeys.clear();  将selectionKeys清空,
                 * Acceptor类中的run()>>>new SocketReadHandler()构造方法中的 selector.wakeup()>>>再次触发selector.select();
                 * Set<SelectionKey> selectionKeys= selector.selectedKeys();
                 * 第一次遍历的selectionKeys里面只有一个就是OP_ACCEPT的selectionKey,attachment为Acceptor对象
                 * 第二次遍历的selectionKeys里面只有一个就是OP_READ的selectionKey,attachment为SocketReadHandler对象
                 */
                selectionKeys.clear();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 运行Acceptor或SocketReadHandler
     */
    void dispatch(SelectionKey selectionKey)
    {
        //本例第一次此方法执行key为serverSocketChannel注册的selectionKey,key.attachment()为Acceptor对象
        //本例第二次此方法执行key为socketChannel注册的selectionKey,key.attachment()为SocketReadHandler对象
        Object acceptor = selectionKey.attachment();
        if (!Objects.isNull(acceptor) && acceptor instanceof Runnable)
        {
            /*
             * 第一次执行Acceptor的run(),run()方法将调用SocketReadHandler构造方法
             * 在SocketReadHandler构造方法中将向selector注册socketChannel,并attach(SocketReadHandler对象)
             * 第二次执行SocketReadHandler的run(),处理具体逻辑代码
             */
            ((Runnable) acceptor).run();
        }
    }

}
