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
    
    public void writeKeyValue(String key, String value) {
        DBFactory factory = new Iq80DBFactory();
        Options options = new Options();
        DB db = null;
        try {
            db = factory.open(new File("/data"), options);
            WriteOptions writeOptions = new WriteOptions().sync(true);
            db.put(key.getBytes(), value.getBytes(), writeOptions);
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
    
    public void delKeyValue(String key) {
        DBFactory factory = new Iq80DBFactory();
        Options options = new Options();
        DB db = null;
        try {
            db = factory.open(new File("/data"), options);
            db.delete(key.getBytes());
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
    
    public void batchWriteKeyValue(List<InsertRequest>insertRequests) {
        DBFactory factory = new Iq80DBFactory();
        Options options = new Options();
        DB db = null;
        try {
            db = factory.open(new File("/data"), options);
            WriteBatch writeBatch = db.createWriteBatch();
            for (InsertRequest insertRequest : insertRequests) {
                String key = GzipUtil.compress(insertRequest.getKey());
                String value = GzipUtil.compress(insertRequest.getValue());
                writeBatch.put(key.getBytes(), value.getBytes());
            }
            db.write(writeBatch);
            writeBatch.close();
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
}
