package com.housaire.reactor2;

import com.housaire.ExecutionHandler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.logging.Logger;

/**
 * @author <a href="mailto:cheungkay@sina.com">张凯</a>
 * @description:
 * @date 2019/3/13 15:43
 * @see
 * @since 1.0.0
 */
public class SocketReadWriteHandler implements Runnable
{

    private static final Logger log = Logger.getLogger(SocketReadWriteHandler.class.getName());

    private SocketChannel socketChannel;

    public SocketReadWriteHandler(SocketChannel socketChannel)
    {
        this.socketChannel = socketChannel;
    }

    @Override
    public void run()
    {
        System.out.println("客户端数据写入...");
        ByteBuffer byteBuffer = ByteBuffer.allocate(8192);
        try
        {
            this.socketChannel.read(byteBuffer);
            // 激活线程池 处理这些request
            ExecutionHandler.getExecutionHandler().execute(byteBuffer, () -> {
                try
                {
                    log.info("回写客户端...");
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
