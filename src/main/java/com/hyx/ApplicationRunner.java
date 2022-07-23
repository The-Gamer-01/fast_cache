package com.hyx;

import com.hyx.manager.FastCache;
import com.hyx.manager.LevelDbLogManager;
import com.hyx.manager.LogManager;
import com.hyx.remoting.server.NettyServer;

/**
 * @author hyx
 **/

public class ApplicationRunner {
    
    public static void main(String[] args) {
        NettyServer nettyServer = new NettyServer();
        nettyServer.open();
//        new Thread(() -> {
//            LogManager.instance().init(FastCache.instance().getSimpleCache());
//        });
        new Thread(() -> {
            while(true) {
                LevelDbLogManager.instance().check();
            }
        }).start();
    }
}
