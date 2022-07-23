package com.hyx.manager;

import com.hyx.constants.RequestConstant;
import com.hyx.pojo.InsertRequest;
import com.hyx.pojo.ZaddRequest;
import com.hyx.utils.GzipUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * @author hyx
 **/

public class FastCache {
    
    /**
     * 普通缓存
     */
    private final Map<String, String> simpleCache = new HashMap<String, String>(1 << 24, (float) 0.01);
    
    private final Map<String, ConcurrentSkipListSet<ZaddRequest>> zsetCache = new ConcurrentHashMap<>();
    
    private final LogManager LOG_MANAGER = LogManager.instance();
    
    public Map<String, String> getSimpleCache() {
        return simpleCache;
    }
    
    private FastCache() {}
    
    private final static FastCache fastCache = new FastCache();
    
    public static FastCache instance() {
        return fastCache;
    }
    
    /**
     * 初始化
     * @return
     */
    public boolean init() {
        return LOG_MANAGER.init(simpleCache);
    }
    
    /**
     * 添加key-value
     * @param key key值
     * @param value value值
     * @return 是否添加成功
     */
    public boolean add(String key, String value) {
        simpleCache.put(key, value);
        return true;
    }
    
    /**
     * 查询缓存
     * @param key key值
     * @return value值
     */
    public String query(String key) {
        return simpleCache.get(key);
    }
    
    /**
     * 删除缓存
     * @param key key值
     * @return 是否删除成功
     */
    public boolean del(String key) {
        return simpleCache.remove(key) != null;
    }
    
    /**
     * 列表列举
     * @param list 需要列举的key值列表
     * @return 需要列举的value值列表
     */
    public List<String> list(List<String> list) {
        List<String> values = new ArrayList<>();
        list.stream().forEach((key) -> {
            String value = null;
//            if(key.length() > RequestConstant.COMPRESS_SIZE) {
//                key = GzipUtil.compress(key);
//                value = GzipUtil.uncompress(simpleCache.get(key));
//            } else {
//                value = simpleCache.get(key);
//            }
            value = simpleCache.get(key);
            values.add(value);
        });
        return values;
    }
    
    /**
     * 批量插入
     * @param insertRequests 批量插入
     * @return 是否批量插入成功
     */
    public boolean batch(List<InsertRequest> insertRequests) {
        insertRequests.stream().forEach((insertRequest) -> {
            String key = insertRequest.getKey();
            String value = insertRequest.getValue();
//            if(key.length() > RequestConstant.COMPRESS_SIZE) {
//                key = GzipUtil.compress(insertRequest.getKey());
//                value = GzipUtil.compress(insertRequest.getValue());
//            }
            simpleCache.put(key, value);
        });
        return true;
    }
    
    /**
     * zadd添加
     * @param key
     * @param zaddRequest
     * @return
     */
    public boolean zadd(String key, ZaddRequest zaddRequest) {
        ConcurrentSkipListSet<ZaddRequest> zset = zsetCache.getOrDefault(key, new ConcurrentSkipListSet<>((request1, request2) -> {
            String value1 = request1.getValue();
            String value2 = request2.getValue();
            if(value1.equals(value2)) {
                return 0;
            } else {
                return request1.getScore() - request2.getScore();
            }
        }));
        zset.removeIf(request -> request.getValue().equals(zaddRequest.getValue()));
        zset.add(zaddRequest);
        zsetCache.put(key, zset);
        return true;
    }
    
    /**
     * 有序集范围查询
     * @param key
     * @param minScore
     * @param maxScore
     * @return
     */
    public List<ZaddRequest> zrange(String key, Integer minScore, Integer maxScore) {
        ConcurrentSkipListSet<ZaddRequest> zset = zsetCache.get(key);
        if(zset == null) {
            return null;
        } else {
            List<ZaddRequest> requests = new ArrayList<>();
            zset.stream().forEach(request -> {
                if(request.getScore() >= minScore && request.getScore() <= maxScore) {
                    requests.add(request);
                }
            });
            return requests;
        }
    }
    
    /**
     * 删除有序集合中的元素
     * @param key
     * @param value
     * @return
     */
    public Boolean zrmv(String key, String value) {
        ConcurrentSkipListSet<ZaddRequest> zset = zsetCache.get(key);
        if(zset == null) {
            return false;
        } else {
            zset.removeIf(request -> request.getValue().equals(value));
            return true;
        }
    }
    
}
