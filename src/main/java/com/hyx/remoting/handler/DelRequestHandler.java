package com.hyx.remoting.handler;

import com.hyx.constants.RequestConstant;
import com.hyx.constants.Response;
import com.hyx.manager.FastCache;
import com.hyx.manager.FastCacheManager;
import com.hyx.manager.LevelDbLogManager;
import com.hyx.utils.GzipUtil;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

/**
 * @author hyx
 **/

public class DelRequestHandler extends AbstractRequestHandler {
    /**
     * 缓存
     */
    protected FastCache fastCache = FastCacheManager.getInstance().getFastCache();
    
    private void writeLog(String key) {
        LevelDbLogManager instance = LevelDbLogManager.instance();
        instance.delKeyValue(key);
    }
    
    @Override
    public DefaultFullHttpResponse handler(FullHttpRequest httpRequest) {
        String uri = httpRequest.uri();
        String[] path = uri.split("/");
        String key = path[2];
        if(key.length() > RequestConstant.COMPRESS_SIZE) {
            key = GzipUtil.compress(key);
        }
        fastCache.del(key);
//        writeLog(key);
        return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
    }
}
