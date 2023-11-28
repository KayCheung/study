package com.housaire;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.concurrent.Callable;

/**
 * @author <a href="mailto:cheungkay@sina.com">张凯</a>
 * 仅供测试使用
 * @date 2019/3/12 15:16
 * @see
 * @since 1.0.0
 */
public class RequestHandler implements Callable<String>
{

    private ByteBuffer byteBuffer;

    public RequestHandler(ByteBuffer byteBuffer)
    {
        this.byteBuffer = byteBuffer;
    }

    @Override
    public String call() throws Exception
    {
        try
        {
            byteBuffer.flip();
            byte[] array = new byte[byteBuffer.limit()];
            byteBuffer.get(array, byteBuffer.arrayOffset(), byteBuffer.limit());
            String request = new String(array, "UTF-8");
            System.err.println(request);
            if (null != request && request.startsWith("callback"))
            {
                return "callback";
            }
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
