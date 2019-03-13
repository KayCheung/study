package com.housaire.reactor1;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.concurrent.Callable;

/**
 * @author <a href="mailto:zhangkai@chinayie.com">张凯</a>
 * @description:
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
            String request = new String(byteBuffer.array(), "UTF-8");
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
