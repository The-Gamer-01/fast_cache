package com.hyx.remoting.handler;

import com.alibaba.fastjson.JSON;
import com.hyx.constants.RequestConstant;
import com.hyx.manager.FastCache;
import com.hyx.manager.FastCacheManager;
import com.hyx.manager.JsonManager;
import com.hyx.manager.LevelDbLogManager;
import com.hyx.pojo.InsertRequest;
import com.hyx.utils.GzipUtil;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.DBFactory;
import org.iq80.leveldb.Options;
import org.iq80.leveldb.impl.Iq80DBFactory;

import java.util.Map;

/**
 * 增加缓存handler
 * @author hyx
 **/

@Slf4j
public class AddRequestHandler extends AbstractRequestHandler {
    /**
     * 缓存
     */
    protected FastCache fastCache = FastCacheManager.getInstance().getFastCache();
    
    protected Map<String, Object> JSON_CACHE = JsonManager.instance();
    
    private void writeLog(String key, String value) {
        LevelDbLogManager instance = LevelDbLogManager.instance();
        instance.writeKeyValue(key, value);
    }
    
    @Override
    public DefaultFullHttpResponse handler(FullHttpRequest httpRequest) {
        String params = httpRequest.content().toString(CharsetUtil.UTF_8);
        InsertRequest insertRequest = null;
        if(JSON_CACHE.containsKey(params)) {
            insertRequest = (InsertRequest) JSON_CACHE.get(params);
        } else {
            insertRequest = JSON.parseObject(params, InsertRequest.class);
            JSON_CACHE.put(params, insertRequest);
        }
        if(insertRequest != null && insertRequest.getKey() != null && insertRequest.getValue() != null) {
            log.info("[info][add key:{}, value:{}]", insertRequest.getKey(), insertRequest.getValue());
            String key = insertRequest.getKey();
            String value = insertRequest.getValue();
            if(key.length() > RequestConstant.COMPRESS_SIZE) {
                key = GzipUtil.compress(insertRequest.getKey());
                value = GzipUtil.compress(insertRequest.getValue());
            }
            fastCache.add(key, value);
            //            writeLog(key, value);
            return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        } else {
            return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST);
        }
    }
}
