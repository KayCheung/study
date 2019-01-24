package com.housaire.netty;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;
import org.jboss.netty.handler.logging.LoggingHandler;
import org.jboss.netty.logging.InternalLoggerFactory;
import org.jboss.netty.logging.Slf4JLoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * @author <a href="mailto:zhangkai@chinayie.com">张凯</a>
 * @description:
 * @date 2019/1/21 11:22
 * @see
 * @since 1.0.0
 */
public class Server
{

    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    public static void main(String[] args) throws InterruptedException
    {
        InternalLoggerFactory.setDefaultFactory(new Slf4JLoggerFactory());
        ServerBootstrap bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory());

        bootstrap.setPipelineFactory(() ->
        {
            ChannelPipeline channelPipeline = Channels.pipeline();
            channelPipeline.addLast("LOGGER", new LoggingHandler());
            channelPipeline.addLast("decoder", new StringDecoder());
            channelPipeline.addLast("encoder", new StringEncoder());
            channelPipeline.addLast("channelHandler", new SimpleChannelHandler()
            {
                @Override
                public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
                {
                    logger.info("[{}] 连接到服务器", e.getChannel().getRemoteAddress());
                }

                @Override
                public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception
                {
                    logger.info("[{}] 退出服务器", e.getChannel().getRemoteAddress());
                }

                @Override
                public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception
                {
                    logger.info("收到客户端[{}]消息：{}", e.getRemoteAddress(), e.getMessage());
                    ctx.getChannel().write(ChannelBuffers.copiedBuffer("收到".getBytes("UTF-8")));
                }

                @Override
                public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception
                {
                    logger.error(e.getCause().getMessage());
                }
            });

            return channelPipeline;
        });

        bootstrap.bind(new InetSocketAddress(9090));
        logger.info("服务端启动成功");
    }

}
