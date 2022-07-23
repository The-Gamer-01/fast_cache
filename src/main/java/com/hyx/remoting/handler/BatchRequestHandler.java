package com.hyx.remoting.handler;

import com.alibaba.fastjson.JSON;
import com.hyx.constants.Response;
import com.hyx.manager.FastCache;
import com.hyx.manager.FastCacheManager;
import com.hyx.manager.LevelDbLogManager;
import com.hyx.pojo.InsertRequest;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

import java.io.IOException;
import java.util.List;

/**
 * @author hyx
 **/

public class BatchRequestHandler extends AbstractRequestHandler {
    /**
     * 缓存
     */
    protected FastCache fastCache = FastCacheManager.getInstance().getFastCache();
    
    private void writeLog(List<InsertRequest> insertRequests) throws IOException {
        LevelDbLogManager instance = LevelDbLogManager.instance();
        instance.batchWriteKeyValue(insertRequests);
    }
    
    @Override
    public DefaultFullHttpResponse handler(FullHttpRequest httpRequest) throws IOException {
        String params = httpRequest.content().toString(CharsetUtil.UTF_8);
        List<InsertRequest> insertRequests = JSON.parseArray(params, InsertRequest.class);
        fastCache.batch(insertRequests);
        writeLog(insertRequests);
        return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
    }
}
