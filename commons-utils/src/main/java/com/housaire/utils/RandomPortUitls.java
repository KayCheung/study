package com.housaire.utils;

import java.io.IOException;
import java.net.ServerSocket;

public class RandomPortUitls
{
    public static int randomPort() {
        ServerSocket serverSocket = null;
        int port = 0;
        try
        {
            serverSocket = new ServerSocket(0);
            port = serverSocket.getLocalPort();
        }
        catch (IOException e) { }
        finally
        {
            if(null != serverSocket) {
                try
                {
                    serverSocket.close();
                }
                catch (IOException e1) { }
            }
        }
        return port;
    }
}
