package com.housaire;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        ServerBootstrap bootstrap = new ServerBootstrap();
        EventLoopGroup parentGroup = new NioEventLoopGroup(); // 用来监听accept新客户端
        EventLoopGroup worker = new NioEventLoopGroup(); // 用来处理accept进来的客户端的IO读写事件
        bootstrap.group(parentGroup, worker)
                 .channel(NioServerSocketChannel.class)
                 .childHandler(new ChannelInitializer<NioSocketChannel>() {
                     protected void initChannel(NioSocketChannel ch) {
                         ch.pipeline().addFirst(new ChannelInboundHandlerAdapter()
                         {
                             @Override
                             public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
                             {
                                 System.err.println(ctx.channel().unsafe().remoteAddress() +  "断开了连接");
                             }
                         });
                         ch.pipeline().addLast(new IdleStateHandler(3, 3, 3));
                         ch.pipeline().addLast(new StringDecoder());
                         ch.pipeline().addLast(new SimpleChannelInboundHandler<String>() {
                             @Override
                             protected void channelRead0(ChannelHandlerContext ctx, String msg) {
                                 System.out.println(msg);
                                 ctx.write("");
                             }
                         });
                     }
                 })
                 .bind(15326);
    }
}
