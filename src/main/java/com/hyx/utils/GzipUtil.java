package com.hyx.utils;

import io.netty.handler.codec.base64.Base64Decoder;
import io.netty.handler.codec.base64.Base64Encoder;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @author hyx
 **/

public class GzipUtil {
    public static String compress(String str) {
        if(str == null || str.length() == 0) {
            return str;
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        GZIPOutputStream gzip = null;
        try {
            gzip = new GZIPOutputStream(outputStream);
            gzip.write(str.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(gzip != null) {
                try {
                    gzip.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return new BASE64Encoder().encode(outputStream.toByteArray());
    }
    
    public static String uncompress(String compressedStr) {
        if(compressedStr == null) {
            return null;
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream in = null;
        GZIPInputStream gzipInputStream = null;
        byte[] compressed = null;
        String decompressedStr = null;
        try {
            compressed = new BASE64Decoder().decodeBuffer(compressedStr);
            in = new ByteArrayInputStream(compressed);
            gzipInputStream = new GZIPInputStream(in);
            byte[] bytes = new byte[1024];
            int offset = -1;
            while((offset = gzipInputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, offset);
            }
            decompressedStr = outputStream.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(gzipInputStream != null) {
                try {
                    gzipInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return decompressedStr;
    }
    
}
