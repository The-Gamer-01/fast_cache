package com.hyx.remoting.handler;

import com.alibaba.fastjson.JSON;
import com.hyx.manager.FastCache;
import com.hyx.manager.FastCacheManager;
import com.hyx.manager.JsonManager;
import com.hyx.pojo.InsertRequest;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author hyx
 **/

public class ListRequestHandler extends AbstractRequestHandler {
    /**
     * 缓存
     */
    protected FastCache fastCache = FastCacheManager.getInstance().getFastCache();
    
    protected Map<String, Object> JSON_CACHE = JsonManager.instance();
    
    @Override
    public DefaultFullHttpResponse handler(FullHttpRequest httpRequest) {
        String params = httpRequest.content().toString(CharsetUtil.UTF_8);
        List<String> list = null;
        if(JSON_CACHE.containsKey(params)) {
            list = (List<String>) JSON_CACHE.get(params);
        } else {
            list = JSON.parseArray(params, String.class);
        }
        List<String> insertRequests = fastCache.list(list);
        return new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled
                .wrappedBuffer(JSON.toJSON(insertRequests).toString().getBytes()));
    }
}
