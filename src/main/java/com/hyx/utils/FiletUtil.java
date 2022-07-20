package com.hyx.utils;

import io.netty.channel.Channel;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.DBFactory;
import org.iq80.leveldb.DBIterator;
import org.iq80.leveldb.Logger;
import org.iq80.leveldb.Options;
import org.iq80.leveldb.ReadOptions;
import org.iq80.leveldb.Snapshot;
import org.iq80.leveldb.impl.Iq80DBFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.Map;
import java.util.Random;

/**
 * 文件相关工具类
 * @author hyx
 **/

public class FiletUtil {
    
    public static void main(String[] args) {
    
        String chars = "abcdefghijklmnopqrstuvwxyz1234567890";
        Random random = new Random();
        
        File writeFile = new File("F:/tianchi/data/log.csv");
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(writeFile, true));
    
            int keyLen = 37;
            int valueLen = 10;
            int size = 10000;
            for(int i = 0; i < size; i++) {
                StringBuilder sb = new StringBuilder();
                for(int j = 0; j < keyLen; j++) {
                    int index = random.nextInt(chars.length());
                    sb.append(chars.charAt(index));
                }
                String key = sb.toString();
                sb = new StringBuilder();
                for(int j = 0; j < valueLen; j++) {
                    int index = random.nextInt(chars.length());
                    sb.append(chars.charAt(index));
                }
                String value = sb.toString();
                writer.write(key + "," + value);
                writer.newLine();
            }
            writer.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
