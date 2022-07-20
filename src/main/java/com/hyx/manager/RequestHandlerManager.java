package com.hyx.manager;

import com.hyx.remoting.handler.AddRequestHandler;
import com.hyx.remoting.handler.BatchRequestHandler;
import com.hyx.remoting.handler.DelRequestHandler;
import com.hyx.remoting.handler.InitHandler;
import com.hyx.remoting.handler.ListRequestHandler;
import com.hyx.remoting.handler.QueryRequestHandler;
import com.hyx.remoting.handler.RequestHandler;
import com.hyx.remoting.handler.ZaddRequestHandler;
import com.hyx.remoting.handler.ZrangeRequestHandler;
import com.hyx.remoting.handler.ZrmvRequestHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hyx
 **/

public class RequestHandlerManager {
    
    private final Map<String, RequestHandler> requestHandlerMap = new ConcurrentHashMap<>();
    
    private RequestHandlerManager() {
        requestHandlerMap.put("add", new AddRequestHandler());
        requestHandlerMap.put("del", new DelRequestHandler());
        requestHandlerMap.put("list", new ListRequestHandler());
        requestHandlerMap.put("query", new QueryRequestHandler());
        requestHandlerMap.put("batch", new BatchRequestHandler());
        requestHandlerMap.put("init", new InitHandler());
        requestHandlerMap.put("zadd", new ZaddRequestHandler());
        requestHandlerMap.put("zrange", new ZrangeRequestHandler());
        requestHandlerMap.put("zrmv", new ZrmvRequestHandler());
    }
    
    private static final RequestHandlerManager INSTANCE = new RequestHandlerManager();
    
    public static RequestHandlerManager INSTANCE() {
        return INSTANCE;
    }
    
    public DefaultFullHttpResponse handler(FullHttpRequest fullHttpRequest) {
        String uri = fullHttpRequest.uri();
        String[] path = uri.split("/");
        return requestHandlerMap.get(path[1]).handler(fullHttpRequest);
    }
}
