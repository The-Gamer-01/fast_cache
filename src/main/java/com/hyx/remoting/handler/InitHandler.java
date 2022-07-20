package com.hyx.remoting.handler;

import com.hyx.constants.Response;
import com.hyx.manager.FastCache;
import com.hyx.manager.FastCacheManager;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

/**
 * @author hyx
 **/

public class InitHandler extends AbstractRequestHandler {
    /**
     * 缓存
     */
    protected FastCache fastCache = FastCacheManager.getInstance().getFastCache();
    
    @Override
    public DefaultFullHttpResponse handler(FullHttpRequest httpRequest) {
        boolean init = fastCache.init();
        if(init) {
            return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer("ok".getBytes()));
        } else {
            return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST);
        }
    }
}
