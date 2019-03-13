package com.housaire;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

/**
 * @author <a href="mailto:cheungkay@sina.com">张凯</a>
 * @description:
 * @date 2019/3/12 15:26
 * @see
 * @since 1.0.0
 */
public class Client
{

    public static void main(String[] args) throws IOException
    {
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress("127.0.0.1", 15326));
        OutputStream out = socket.getOutputStream();
        InputStream in = socket.getInputStream();
        String input = null;
        Scanner scanner = new Scanner(System.in);
        do
        {
            System.out.println("请输入：");
            input = scanner.nextLine();
            out.write(input.getBytes("UTF-8"));
            byte[] b = new byte[8192];
            if (in.read(b) > -1)
            {
                System.err.println("服务端回写：" + new String(b, "UTF-8"));
            }
        } while (null != input &&
                !input.equalsIgnoreCase("quit") &&
                !input.equalsIgnoreCase("exit"));
    }

}
