package com.hyx.remoting.handler;

import com.hyx.manager.FastCache;
import com.hyx.manager.FastCacheManager;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

/**
 * @author hyx
 **/

public class ZrmvRequestHandler extends AbstractRequestHandler {
    /**
     * 缓存
     */
    protected FastCache fastCache = FastCacheManager.getInstance().getFastCache();
    
    @Override
    public DefaultFullHttpResponse handler(FullHttpRequest httpRequest) {
        String uri = httpRequest.uri();
        String method = httpRequest.method().name();
        String[] path = uri.split("/");
        String key = path[2];
        String value = path[3];
        fastCache.zrmv(key, value);
        return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
    }
}
