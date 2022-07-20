package com.hyx.manager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hyx
 **/

public class FastCacheManager {
    
    /**
     * 具体缓存类
     */
    private FastCache fastCache = FastCache.instance();
    
    /**
     * 单例实例
     */
    private static final FastCacheManager INSTANCE = new FastCacheManager();
    
    /**
     * 私有构造方法
     */
    private FastCacheManager() {}
    
    /**
     * 获取实例
     * @return 单例实例
     */
    public static FastCacheManager getInstance() {
        return INSTANCE;
    }
    
    /**
     * 获取缓存类
     * @return 缓存类
     */
    public FastCache getFastCache() {
        return fastCache;
    }
}
