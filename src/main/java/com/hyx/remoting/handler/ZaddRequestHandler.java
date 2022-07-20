package com.hyx.remoting.handler;

import com.alibaba.fastjson.JSON;
import com.hyx.manager.FastCache;
import com.hyx.manager.FastCacheManager;
import com.hyx.pojo.InsertRequest;
import com.hyx.pojo.ZaddRequest;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hyx
 **/

public class ZaddRequestHandler extends AbstractRequestHandler {
    
    /**
     * 缓存
     */
    protected FastCache fastCache = FastCacheManager.getInstance().getFastCache();
    
    @Override
    public DefaultFullHttpResponse handler(FullHttpRequest httpRequest) {
        String uri = httpRequest.uri();
        String[] path = uri.split("/");
        String key = path[2];
        String params = httpRequest.content().toString(CharsetUtil.UTF_8);
        ZaddRequest zaddRequest = JSON.parseObject(params, ZaddRequest.class);
        fastCache.zadd(key, zaddRequest);
        return new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
    }
}
