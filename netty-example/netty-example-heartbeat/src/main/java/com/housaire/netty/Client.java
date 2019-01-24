package com.housaire.netty;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;
import org.jboss.netty.handler.logging.LoggingHandler;
import org.jboss.netty.logging.InternalLoggerFactory;
import org.jboss.netty.logging.Slf4JLoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.util.Scanner;

/**
 * @author <a href="mailto:cheungkay@sina.com">张凯</a>
 * @description:
 * @date 2019/1/21 11:22
 * @see
 * @since 1.0.0
 */
public class Client
{

    private static final Logger logger = LoggerFactory.getLogger(Client.class);

    public static void main(String[] args) throws UnsupportedEncodingException
    {
        InternalLoggerFactory.setDefaultFactory(new Slf4JLoggerFactory());
        ClientBootstrap bootstrap = new ClientBootstrap(new NioClientSocketChannelFactory());
        Channel channel = bootstrap.connect(new InetSocketAddress("127.0.0.1", 9090)).getChannel();

        bootstrap.setPipelineFactory(() ->
        {
            ChannelPipeline channelPipeline = Channels.pipeline();
            channelPipeline.addLast("LOGGER", new LoggingHandler());
            channelPipeline.addLast("decoder", new StringDecoder());
            channelPipeline.addLast("encoder", new StringEncoder());
            channelPipeline.addLast("channelHandler", new SimpleChannelHandler()
            {
                @Override
                public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception
                {
                    logger.info("收到服务端[{}]消息：{}", e.getRemoteAddress(), e.getMessage());
                }
            });

            return channelPipeline;
        });

        Scanner in = new Scanner(System.in);
        System.err.println("请输入：");
        while (in.hasNextLine())
        {
            String line = in.nextLine();
            if ("exit".equalsIgnoreCase(line) || "quit".equalsIgnoreCase(line))
            {
                in.close();
                channel.close();
                bootstrap.shutdown();
                break;
            }

            ChannelFuture channelFuture = channel.write(ChannelBuffers.copiedBuffer(line.getBytes("UTF-8")));
            if (channelFuture.isDone())
            {
                System.err.println("发送成功");
            }
        }

    }

}
