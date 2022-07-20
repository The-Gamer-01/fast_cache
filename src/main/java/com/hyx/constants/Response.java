package com.hyx.constants;

import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

/**
 * @author hyx
 **/

public enum Response {
    /**
     * OK且不带返回值，状态码为200
     */
    OK(new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK)),
    /**
     * 请求失败状态码为400
     */
    BAD_REQUEST(new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST)),
    /**
     * 查询失败未找到，状态码为404
     */
    NOT_FOUND(new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND));
    
    DefaultFullHttpResponse response;
    
    public DefaultFullHttpResponse getResponse() {
        return response;
    }
    
    Response(DefaultFullHttpResponse fullHttpResponse) {
        this.response = fullHttpResponse;
    }
}
