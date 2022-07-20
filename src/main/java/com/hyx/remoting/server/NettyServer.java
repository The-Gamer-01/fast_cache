package com.hyx.remoting.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * @author hyx
 **/

@Slf4j
public class NettyServer {
    
    private static final Integer PORT = 8080;
    
    private static final Integer cpuNum = Runtime.getRuntime().availableProcessors();
    
    public void open() {
        EventLoopGroup bossGroup = Epoll.isAvailable() ? new EpollEventLoopGroup(1) : new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = Epoll.isAvailable() ? new EpollEventLoopGroup(1) : new NioEventLoopGroup(1);
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(Epoll.isAvailable() ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(PORT))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(new HttpServerCodec());
                            pipeline.addLast("httpAggregator", new HttpObjectAggregator(512 * 1024));
                            pipeline.addLast(DispatcherHandler.instance());
                        }
                    })
                    .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .option(EpollChannelOption.TCP_QUICKACK, Boolean.TRUE);
            ChannelFuture future = serverBootstrap.bind().sync();
            if(future.isSuccess()) {
                log.info("线程在{}端口启动！", PORT);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
