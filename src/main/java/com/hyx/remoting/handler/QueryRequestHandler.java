package com.hyx.remoting.handler;

import com.hyx.constants.RequestConstant;
import com.hyx.constants.Response;
import com.hyx.manager.FastCache;
import com.hyx.manager.FastCacheManager;
import com.hyx.utils.GzipUtil;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import lombok.extern.slf4j.Slf4j;

/**
 * @author hyx
 **/

@Slf4j
public class QueryRequestHandler extends AbstractRequestHandler {
    /**
     * 缓存
     */
    protected FastCache fastCache = FastCacheManager.getInstance().getFastCache();
    
    @Override
    public DefaultFullHttpResponse handler(FullHttpRequest httpRequest) {
        String uri = httpRequest.uri();
        String[] path = uri.split("/");
        String key = path[2];
        String value = null;
        if(key.length() > RequestConstant.COMPRESS_SIZE) {
            key = GzipUtil.compress(key);
            value = GzipUtil.uncompress(fastCache.query(key));
        } else {
            value = fastCache.query(key);
        }
        log.info("[info][query key:{}, value:{}]", key, value);
        if(value != null) {
            return new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer(value.getBytes()));
        } else {
            return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND);
        }
    }
}
