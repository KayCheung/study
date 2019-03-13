package com.housaire;

import java.nio.ByteBuffer;
import java.util.concurrent.*;

/**
 * @author <a href="mailto:cheungkay@sina.com">张凯</a>
 * 仅供测试使用
 * @date 2019/3/12 15:20
 * @see
 * @since 1.0.0
 */
public final class ExecutionHandler
{

    private final static ExecutionHandler executionHandler = new ExecutionHandler();

    private ExecutorService executorService = Executors.newFixedThreadPool(10);

    private ExecutionHandler() {}

    public static ExecutionHandler getExecutionHandler()
    {
        return executionHandler;
    }

    public void execute(ByteBuffer byteBuffer, Runnable callback)
    {
        Future<String> future = executorService.submit(new RequestHandler(byteBuffer));
        try
        {
            String response = future.get();
//            if (null != response && response.trim().length() > 0)
//            {
                callback.run();
//            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

}
