package com.hyx.remoting.server;

import com.hyx.manager.RequestHandlerManager;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @author hyx
 **/

@Slf4j
@ChannelHandler.Sharable
public class DispatcherHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    
    private DispatcherHandler() {}
    
    private static final DispatcherHandler INSTANCE = new DispatcherHandler();

    public static DispatcherHandler instance() {
        return INSTANCE;
    }
    
    private RequestHandlerManager requestHandlerManager = RequestHandlerManager.INSTANCE();
    
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest request)
            throws IOException {
        DefaultFullHttpResponse response = requestHandlerManager.handler(request);
        channelHandlerContext.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
}
