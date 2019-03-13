package com.housaire.reactor2;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author <a href="mailto:cheungkay@sina.com">张凯</a>
 * @description:
 * @date 2019/3/13 09:39
 * @see
 * @since 1.0.0
 */
public abstract class Reactor implements Runnable
{

    private static final Logger log = Logger.getLogger(Reactor.class.getName());

    protected abstract Selector getSelector();

    @Override
    public void run()
    {
        Selector selector = getSelector();
        while (!Thread.interrupted())
        {
            try
            {
                // 执行Selector.select方法时，会将Selector中fd列表的所有fd发送至内核
                // 当fd列表中有可读状态的则返回，否则循环检查
                // 内核处理完返回后，Selector.select方法会对返回的事件进行筛选，将符合的事件添加到SelectionKey集合中
                selector.select();
                // 获取SelectionKey集合(符合事件的SelectionKey)
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> it = selectionKeys.iterator();
                while (it.hasNext())
                {
                    dispatch(it.next());
                }
                // 将SelectionKey集合进行清空
                selectionKeys.clear();
            }
            catch (IOException e)
            {
                log.log(Level.SEVERE, "网络IO异常", e);
            }
        }
    }

    private void dispatch(SelectionKey selectionKey)
    {
        Runnable runnable = (Runnable) selectionKey.attachment();
        runnable.run();
        // 唤醒阻塞在Selector.select上的线程
        getSelector().wakeup();
    }

}
