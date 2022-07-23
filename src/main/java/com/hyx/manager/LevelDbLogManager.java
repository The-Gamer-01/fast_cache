package com.hyx.manager;

import com.hyx.pojo.InsertRequest;
import com.hyx.utils.GzipUtil;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.DBFactory;
import org.iq80.leveldb.Options;
import org.iq80.leveldb.WriteBatch;
import org.iq80.leveldb.WriteOptions;
import org.iq80.leveldb.impl.Iq80DBFactory;
import org.omg.PortableInterceptor.INACTIVE;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * @author hyx
 **/

public class LevelDbLogManager {
    
    private static final LevelDbLogManager INSTANCE = new LevelDbLogManager();
    
    public static LevelDbLogManager instance() {
        return INSTANCE;
    }
    
    private DBFactory factory;
    
    private Options options;
    
    private DB db;
    
    private List<InsertRequest> batchList;
    
    private WriteBatch writeBatch;
    
    private static final Integer MAX_CAPACITY = 1 << 8;
    
    private LevelDbLogManager() {
        init();
    }
    
    private void init() {
        try {
            this.factory = new Iq80DBFactory();
            this.options = new Options();
            this.db = factory.open(new File("/data"), options);
            this.batchList = new ArrayList<>();
            this.writeBatch = db.createWriteBatch();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void writeKeyValue(String key, String value) throws IOException {
        batchList.add(new InsertRequest(key, value));
    }
    
    public void delKeyValue(String key) {
        db.delete(key.getBytes());
    }
    
    public void batchWriteKeyValue(List<InsertRequest> insertRequests) throws IOException {
        batchList.addAll(insertRequests);
    }
    
    public void check() {
        if(batchList.size() >= MAX_CAPACITY) {
            for (InsertRequest insertRequest : batchList) {
                String key = insertRequest.getKey();
                String value = insertRequest.getValue();
                writeBatch.put(key.getBytes(), value.getBytes());
            }
            db.write(writeBatch);
            batchList.clear();
        }
    }
}
