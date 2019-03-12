package com.housaire.reactor;

import java.nio.ByteBuffer;
import java.util.concurrent.*;

/**
 * @author <a href="mailto:zhangkai@chinayie.com">张凯</a>
 * @description:
 * @date 2019/3/12 15:20
 * @see
 * @since 1.0.0
 */
public final class ExecutorHandler
{

    private final static ExecutorHandler executorHandler = new ExecutorHandler();

    private ExecutorService executorService = Executors.newFixedThreadPool(10);

    private ExecutorHandler() {}

    public static ExecutorHandler getExecutorHandler()
    {
        return executorHandler;
    }

    public void execute(ByteBuffer byteBuffer, Runnable callback)
    {
        Future<String> future = executorService.submit(new RequestHandler(byteBuffer));
        try
        {
            String response = future.get();
            callback.run();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

}
