package com.hyx.remoting.handler;

import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;

/**
 * 请求处理策略对象接口
 * @author hyx
 **/

public interface RequestHandler {
    
    /**
     * 根据不用参数调用不同方法
     * @param httpRequest 请求
     * @return 响应
     */
    DefaultFullHttpResponse handler(FullHttpRequest httpRequest);
}
