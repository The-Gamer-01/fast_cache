package com.hyx.manager;

import com.hyx.constants.RequestConstant;
import com.hyx.utils.GzipUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.DBFactory;
import org.iq80.leveldb.DBIterator;
import org.iq80.leveldb.Options;
import org.iq80.leveldb.ReadOptions;
import org.iq80.leveldb.Snapshot;
import org.iq80.leveldb.impl.Iq80DBFactory;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * @author hyx
 **/

@Slf4j
@Getter
@Setter
public class LogManager {
    
    private volatile boolean isAllReady;
    
    private volatile boolean isLoading;
    
    private LogManager() {
        this.isAllReady = false;
        this.isLoading = false;
    }
    
    private static LogManager instance = new LogManager();
    
    public static LogManager instance() {
        return instance;
    }
    
    public boolean init(Map<String, String> cache) {
        if(isAllReady) {
            return true;
        }
        if(!isLoading) {
            isLoading = true;
            File logFile = new File("/data");
            DBFactory factory = new Iq80DBFactory();
            Options options = new Options();
            DB db = null;
            try {
                db = factory.open(logFile, options);
                Snapshot snapshot = db.getSnapshot();
                ReadOptions readOptions = new ReadOptions();
                readOptions.fillCache(false);
                readOptions.snapshot(snapshot);
                DBIterator iterator = db.iterator(readOptions);
                while(iterator.hasNext()) {
                    Map.Entry<byte[], byte[]> entry = iterator.next();
                    String key = new String(entry.getKey());
                    String value = new String(entry.getValue());
                    if(key.length() > RequestConstant.COMPRESS_SIZE) {
                        key = GzipUtil.compress(key);
                        value = GzipUtil.compress(value);
                    }
                    log.info("[init][key:{}, valie:{}]", key, value);
                    cache.put(key, value);
                }
                isLoading = false;
                isAllReady = true;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(db != null) {
                    try {
                        db.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return false;
    }
}
