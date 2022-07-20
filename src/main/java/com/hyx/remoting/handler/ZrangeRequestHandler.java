package com.hyx.remoting.handler;

import com.alibaba.fastjson.JSON;
import com.hyx.manager.FastCache;
import com.hyx.manager.FastCacheManager;
import com.hyx.pojo.ZaddRequest;
import com.hyx.pojo.ZrangeRequest;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

import java.util.List;

/**
 * @author hyx
 **/

public class ZrangeRequestHandler extends AbstractRequestHandler {
    
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
        ZrangeRequest zrangeRequest = JSON.parseObject(params, ZrangeRequest.class);
        List<ZaddRequest> zrange = fastCache.zrange(key, zrangeRequest.getMin_score(), zrangeRequest.getMax_score());
        if(zrange != null) {
            return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled
                    .wrappedBuffer(zrange.toString().getBytes()));
        } else {
            return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND);
        }
    }
}
